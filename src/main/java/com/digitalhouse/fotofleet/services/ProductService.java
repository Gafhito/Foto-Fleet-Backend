package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ObjectMapper mapper;

    public Page<Product> listAllProducts(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return productRepository.listAllProducts(pageable);
    }

    public Product createProduct(ProductDto productDto) throws ResourceNotFoundException {
        Optional<Category> category = categoryService.getCategoryById(productDto.categoryId());
        if (category.isEmpty()) throw new ResourceNotFoundException("No existe categoría con ID: " + productDto.categoryId());

        Product product = new Product(productDto.name(), productDto.description(), category.get(), productDto.rentalPrice(), productDto.stock(), null);

        return productRepository.save(product);
    }

    public ProductDto getProductById(Integer id) throws ResourceNotFoundException {
        Optional<Product> p = productRepository.findById(id);
        if(p.isEmpty()){
            throw new ResourceNotFoundException("No existe un producto con el ID: " + id);
        }
        return new ProductDto(p.get().getName(), p.get().getDescription(),p.get().getCategory().getCategoryId(),p.get().getRentalPrice(),p.get().getStock(),null);
    }

    public void deleteProduct(Integer id) throws ResourceNotFoundException{
        if(getProductById(id) == null){
            throw new ResourceNotFoundException("No existe el producto con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product updateProduct(Integer id,ProductDto productDto) throws BadRequestException{
        Optional<Category> category = categoryService.getCategoryById(productDto.categoryId());
        Optional<Product> p = productRepository.findById(id);
        if(p.isEmpty()){
            throw new BadRequestException("No es posible actualizar el producto con ID: " + productDto + ", porque no está registrado");
        }
        Product product = p.get();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setCategory(category.get());
        product.setRentalPrice(productDto.rentalPrice());
        product.setStock(productDto.stock());
        //product.setStatus();
        return productRepository.save(product);
        /*
        Product product = mapper.convertValue(productDto, Product.class);
        return mapper.convertValue(productRepository.save(product), ProductDto.class);
        */
    }
}