package com.digitalhouse.fotofleet.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.digitalhouse.fotofleet.dtos.ImageDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.models.ProductImage;
import com.digitalhouse.fotofleet.repositories.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private Logger logger = LoggerFactory.getLogger(ProductImageService.class);
    private final ProductImageRepository productImageRepository;
    private final AmazonS3 s3Client;
    @Value("${aws.bucket}")
    private String bucket;

    public List<ImageDto> listImagesByProductId(Integer productId) {
        List<ProductImage> productImages = productImageRepository.listByProductId(productId);
        List<ImageDto> imageDtos = new ArrayList<>();

        if (!productImages.isEmpty()) {
            for (ProductImage productImage : productImages) {
                imageDtos.add(new ImageDto(productImage.getImageUrl(), productImage.getPrimary()));
            }
        }

        return imageDtos;
    }

    public Optional<ProductImage> getPrimaryImageByProductId(Integer productId) {
        return productImageRepository.findPrimaryImageByProductId(productId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductImage upload(Product product, MultipartFile file, Boolean primary) throws BadRequestException {
        if (primary) {
            Optional<ProductImage> pi = getPrimaryImageByProductId(product.getProductId());
            if (pi.isPresent()) throw new BadRequestException("Ya existe una imágen primaria para este producto");
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String mimeType = null;

        try {
            byte[] bytes = file.getBytes();
            ImageFormat mime = Imaging.guessFormat(bytes);
            if (mime == ImageFormats.JPEG || mime == ImageFormats.PNG) {
                mimeType = file.getContentType();
            } else {
                mimeType = "NO IMAGE TYPE";
            }
        } catch (IOException e) {
            throw new BadRequestException("Error al procesar la imagen");
        }

        if (
           (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png")) &&
           (mimeType.equals("image/jpg") || mimeType.equals("image/jpeg") || mimeType.equals("image/png"))
        ) {
            File fileObject = convertMultipartFileToFile(file);
            String fileName = product.getName().concat("_" + LocalDateTime.now().toString()).concat("." + extension).replace(" ", "_").toLowerCase();
            s3Client.putObject(new PutObjectRequest(bucket, fileName, fileObject));

            S3Object object = s3Client.getObject(bucket, fileName);
            String url = s3Client.getUrl(bucket, object.getKey()).toString();
            fileObject.delete();

            return productImageRepository.save(new ProductImage(product, fileName, url, primary));
        } else {
            throw new BadRequestException("Error, solo se admiten archivos jpeg, jpg o png");
        }
    }

    @Transactional(rollbackFor = BadRequestException.class)
    public void uploadImagesForProduct(Product product, MultipartFile primaryImage, List<MultipartFile> secondaryImages) throws BadRequestException {
        if (secondaryImages.size() > 6) throw new BadRequestException("Error, excede el límite máximo de 6 imágenes secundarias");

        upload(product, primaryImage, true);
        for (MultipartFile file : secondaryImages) {
            upload(product, file, false);
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            logger.error("Error al convertir el MultipartFile en File", e);
        }

        return convertedFile;
    }
}
