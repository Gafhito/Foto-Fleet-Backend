package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.services.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService productImageService;

    @PostMapping()
    public ResponseEntity<?> uploadImage(@RequestParam Integer productId, @RequestParam MultipartFile file) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(productImageService.uploadFile(productId, file, null), HttpStatus.CREATED);
    }
}
