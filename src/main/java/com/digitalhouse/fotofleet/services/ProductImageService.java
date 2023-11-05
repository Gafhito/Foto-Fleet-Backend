package com.digitalhouse.fotofleet.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.digitalhouse.fotofleet.dtos.ImageDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.models.ProductImage;
import com.digitalhouse.fotofleet.repositories.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private Logger logger = LoggerFactory.getLogger(ProductImageService.class);
    private final ProductImageRepository productImageRepository;
    private final ProductService productService;
    private final AmazonS3 s3Client;
    @Value("${aws.bucket}")
    private String bucket;

    public List<ImageDto> listImagesByProductId(Integer productId) throws ResourceNotFoundException {
        productService.existsProductById(productId);

        List<ProductImage> productImages = productImageRepository.listByProductId(productId);
        List<ImageDto> imageDtos = new ArrayList<>();

        if (!productImages.isEmpty()) {
            for (ProductImage productImage : productImages) {
                imageDtos.add(new ImageDto(productImage.getImageUrl(), productImage.getDescription()));
            }
        }

        return imageDtos;
    }

    public ProductImage uploadFile(Integer productId, MultipartFile file, String description) throws BadRequestException, ResourceNotFoundException {
        Optional<Product> product = productService.getById(productId);
        if (product.isEmpty()) throw new ResourceNotFoundException("No existe un producto con este Id");

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String mimeType = file.getContentType();

        if (
           extension.equalsIgnoreCase("jpg") ||
           extension.equalsIgnoreCase("jpeg") ||
           extension.equalsIgnoreCase("png") &&
           mimeType.equals("image/jpg") ||
           mimeType.equals("image/jpeg") ||
           mimeType.equals("image/png")
        ) {
            File fileObject = convertMultipartFileToFile(file);
            String fileName = product.get().getName().concat(LocalDateTime.now().toString()).concat("." + extension).replace(" ", "_").toLowerCase();
            s3Client.putObject(new PutObjectRequest(bucket, fileName, fileObject));

            S3Object object = s3Client.getObject(bucket, fileName);
            String url = s3Client.getUrl(bucket, object.getKey()).toString();

            return productImageRepository.save(new ProductImage(product.get(), fileName, url, description));
        } else {
            throw new BadRequestException("Error, solo se admiten archivos jpeg, jpg o png");
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
