package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.CategoryDto;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto){
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) throws ResourceNotFoundException {
        Optional<Category> categoryDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping
    public ResponseEntity<Collection<CategoryDto>> listCategories(){
        Collection<CategoryDto> categories = categoryService.listCategories();
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) throws ResourceNotFoundException{
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body("Categoría eliminada exitosamente.");
    }
}
