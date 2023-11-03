package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.CategoryDto;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.repositories.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    ObjectMapper mapper;

    public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new ResourceNotFoundException("No existe categoría con ID: " + id);
        }
        return mapper.convertValue(category, CategoryDto.class);
    }

    public CategoryDto createCategory(CategoryDto categoryDto){
        Category category = mapper.convertValue(categoryDto, Category.class);
        return mapper.convertValue(categoryRepository.save(category), CategoryDto.class);
    }

    public void deleteCategory(Integer id) throws ResourceNotFoundException {
        if(getCategoryById(id) == null){
            throw new ResourceNotFoundException("No existe categoría con ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    public Collection<CategoryDto> listCategories(){
        List<Category> categories = categoryRepository.findAll();
        Collection<CategoryDto> listCategoriesDto = new HashSet<>();

        for(Category c : categories){
            listCategoriesDto.add(mapper.convertValue(c, CategoryDto.class));
        }
        return listCategoriesDto;
    }


}
