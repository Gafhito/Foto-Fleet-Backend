package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.ImageDto;
import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.services.ProductImageService;
import com.digitalhouse.fotofleet.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductImageService productImageService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam Integer page) {
        Page<Product> products = productService.listAllProducts(page);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) throws ResourceNotFoundException {
        ProductDto p = productService.getProductById(id);
        List<ImageDto> imageDto = productImageService.listImagesByProductId(id);

        return ResponseEntity.ok(new ProductDto(p.name(), p.description(), p.categoryId(), p.rentalPrice(), p.stock(), p.statusId(), imageDto));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) throws ResourceNotFoundException{
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
