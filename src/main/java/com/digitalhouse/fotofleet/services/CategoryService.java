package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.CategoryDto;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

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
}
