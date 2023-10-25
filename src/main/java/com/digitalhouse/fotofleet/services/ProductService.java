package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public Page<Product> listAllProducts(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return productRepository.listAllProducts(pageable);
    }

    public Product createProduct(ProductDto productDto) {
        Optional<Category> category = categoryService.getCategoryById(productDto.categoryId());
        return productRepository.save(new Product(productDto.name(), productDto.description(), productDto.rentalPrice(), productDto.stock(), category.get()));
    }

    public Optional<Product> getProductById(Integer id){
        return productRepository.findById(id);
    }

    public void deleteProduct(Integer id){
        productRepository.deleteById(id);
    }


}
