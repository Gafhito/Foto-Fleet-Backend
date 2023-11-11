package com.digitalhouse.fotofleet.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.digitalhouse.fotofleet.dtos.CategoryDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final AmazonS3 s3Client;
    @Value("${aws.bucket}")
    private String bucket;

    public Category getCategoryById(Integer id) throws ResourceNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) throw new ResourceNotFoundException("No existe categoría con ID: " + id);

        return category.get();
    }

    public Category createCategory(CategoryDto categoryDto){
        return categoryRepository.save(new Category(categoryDto.name(),categoryDto.description()));
    }

    public void deleteCategory(Integer id) throws ResourceNotFoundException {
        Category category = getCategoryById(id);
        categoryRepository.deleteById(category.getCategoryId());
    }

    public List<Category> listCategories(){
        return categoryRepository.findAll();
    }

    public Category updateCategory(Integer id, CategoryDto categoryDto) throws ResourceNotFoundException {
        Category category = getCategoryById(id);
        category.setName(categoryDto.name());
        category.setDescription(categoryDto.description());

        return categoryRepository.save(category);
    }

    public Category uploadImage(Integer categoryId, MultipartFile file) throws BadRequestException, ResourceNotFoundException {
        Category category = getCategoryById(categoryId);

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
            String fileName = category.getName().concat("_" + LocalDateTime.now().toString()).concat("." + extension).replace(" ", "_").toLowerCase();
            s3Client.putObject(new PutObjectRequest(bucket, fileName, fileObject));

            S3Object object = s3Client.getObject(bucket, fileName);
            String url = s3Client.getUrl(bucket, object.getKey()).toString();
            category.setImageUrl(url);
            fileObject.delete();

            return categoryRepository.save(category);
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
