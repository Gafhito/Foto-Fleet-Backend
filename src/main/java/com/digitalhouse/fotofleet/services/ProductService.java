package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.ImageDto;
import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.dtos.RentalDateDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.models.RentalDetail;
import com.digitalhouse.fotofleet.models.Status;
import com.digitalhouse.fotofleet.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class  ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final StatusService statusService;
    private final ProductImageService productImageService;
    private final RentalDetailService rentalDetailService;

    public Page<ProductDto> listAllProducts(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product p : products) {
            List<ImageDto> images = productImageService.listImagesByProductId(p.getProductId());
            productDtos.add(new ProductDto(p.getProductId(), p.getName(), p.getDescription(), p.getCategory().getCategoryId(), p.getRentalPrice(), p.getStock(), p.getStatus().getName(), images, p.getCharacteristics()));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productDtos.size());

        return new PageImpl<>(productDtos.subList(start, end), pageable, productDtos.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(ProductDto productDto) throws BadRequestException, ResourceNotFoundException {
        Optional<Product> product = getProductByName(productDto.name().trim());
        if (product.isPresent()) throw new BadRequestException("Error, ya existe un producto registrado con este nombre");

        Category category = categoryService.getCategoryById(productDto.categoryId());
        Optional<Status> status = statusService.getStatusByName("Active");

        return productRepository.save(new Product(productDto.name().trim(), productDto.description().trim(), category, productDto.rentalPrice(), productDto.stock(), status.get(), productDto.characteristics()));
    }

    public Optional<Product> getById(Integer id) {
        return productRepository.findById(id);
    }

    public ProductDto getDtoByProductId(Integer id) throws ResourceNotFoundException {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) throw new ResourceNotFoundException("No existe un producto con este ID");

        List<ImageDto> imageDtos = productImageService.listImagesByProductId(id);
        List<RentalDetail> rentalDetails = rentalDetailService.listPendingAndActiveByProductId(product.get().getProductId());
        List<RentalDateDto> rentalDateDtos = new ArrayList<>();

        for (RentalDetail rd : rentalDetails) {
            rentalDateDtos.add(new RentalDateDto(rd.getRental().getRentalId(), rd.getRental().getStartDate(), rd.getRental().getEndDate()));
        }

        return new ProductDto(product.get().getProductId(),product.get().getName(), product.get().getDescription(), product.get().getCategory().getCategoryId(), product.get().getRentalPrice(), product.get().getStock(), product.get().getStatus().getName(), imageDtos, product.get().getCharacteristics(), rentalDateDtos);
    }

    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    public void deleteProduct(Integer id) throws ResourceNotFoundException{
        Optional<Product> product = getById(id);
        if(product.isEmpty()) throw new ResourceNotFoundException("No existe el producto con ID: " + id);

        productImageService.deleteImagesToS3ByProduct(product.get());
        productRepository.deleteById(id);
    }
  
    public void existsProductById(Integer id) throws ResourceNotFoundException {
        if (!productRepository.existsById(id)) throw new ResourceNotFoundException("No existe un producto con este ID");
    }

    public Product updateProduct(Integer id, ProductDto productDto) throws BadRequestException, ResourceNotFoundException {
        Category category = categoryService.getCategoryById(productDto.categoryId());
        Optional<Product> p = productRepository.findById(id);
        Optional<Status> status = statusService.getStatusByName(productDto.status());

        if (p.isEmpty()) throw new BadRequestException("No es posible actualizar el producto con ID: " + id + ", porque no está registrado");
        if (status.isEmpty()) throw new ResourceNotFoundException("No existe un status con el nombre de: " + productDto.status());
      
        Product product = p.get();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setCategory(category);
        product.setRentalPrice(productDto.rentalPrice());
        product.setStock(productDto.stock());
        product.setStatus(status.get());
        return productRepository.save(product);
    }

    public Product updateProductWhithCharacteristics(Product product) throws ResourceNotFoundException, BadRequestException {
        getDtoByProductId(product.getProductId());
        if (product.getCharacteristics().isEmpty()) throw new BadRequestException("Error, el listado de características no puede estar vacío");

        return productRepository.save(product);
    }

    public Page<ProductDto> search(Integer page, String product, String categoryName) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(page, 10);
        List<Product> products = productRepository.findByFilter(product, categoryName);
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product p : products) {
            ProductDto productDto = getDtoByProductId(p.getProductId());
            productDtos.add(productDto);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productDtos.size());


        return new PageImpl<>(productDtos.subList(start, end), pageable, productDtos.size());
    }

    public void changeCategory(Integer categoryId) throws ResourceNotFoundException {
        categoryService.getCategoryById(categoryId);
        List<Product> products = productRepository.listByCategoryId(categoryId);

        if (!products.isEmpty()) {
            Category others = categoryService.getOthers();

            for (Product p : products) {
                p.setCategory(others);
                productRepository.save(p);
            }
        }
    }
}
