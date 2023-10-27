package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam Integer page) {
        Page<Product> products = productService.listAllProducts(page);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
       Optional<Product> product = productService.getProductById(id);
       /*return new ResponseEntity<>(product, HttpStatus.OK);*/
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) {
        Product product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@RequestParam Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
