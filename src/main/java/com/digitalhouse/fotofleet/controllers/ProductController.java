package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.services.ProductImageService;
import com.digitalhouse.fotofleet.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
        return new ResponseEntity<>(productService.getDtoByProductId(id), HttpStatus.OK);
    }

    @PostMapping("/images")
    public ResponseEntity<?> uploadImages(@RequestParam Integer productId, @RequestParam MultipartFile primaryImage, @RequestParam List<MultipartFile> secondaryImages) throws BadRequestException {
        Optional<Product> product = productService.getById(productId);
        if (product.isEmpty()) throw new BadRequestException("No existe el producto con el ID especificado");

        productImageService.uploadImagesForProduct(product.get(), primaryImage, secondaryImages);

        return new ResponseEntity<>(HttpStatus.CREATED);

    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) throws ResourceNotFoundException{
        Product product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) throws ResourceNotFoundException{
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDto productDto) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(productService.updateProduct(id, productDto),HttpStatus.OK);
    }
}
