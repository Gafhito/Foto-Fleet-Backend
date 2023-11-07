package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.CategoryDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
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

    /*public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new ResourceNotFoundException("No existe categoría con ID: " + id);
        }
        return mapper.convertValue(category, CategoryDto.class);
    }*/
    public Optional<Category> getCategoryById(Integer id) throws ResourceNotFoundException {
        if(categoryRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("No existe una categoría registrada con ID: " + id);
        }
        return categoryRepository.findById(id);
    }
    /*public CategoryDto getCategoryById(Integer id) {
        Optional<Category> c = categoryRepository.findById(id);
        return new CategoryDto(c.get().getName(),c.get().getDescription());
    }*/


    public Category createCategory(CategoryDto categoryDto){
        Category c = new Category(categoryDto.name(),categoryDto.description());
        return categoryRepository.save(c);
    }

    public void deleteCategory(Integer id) throws ResourceNotFoundException {
        if(getCategoryById(id).isEmpty()){
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

    public Category updateCategory(Integer id, CategoryDto categoryDto) throws BadRequestException{
        Optional<Category> c = categoryRepository.findById(id);
        if(c.isEmpty()){
            throw new BadRequestException("No es posible modificar la categoría con ID: " + id + "porque no está registrada" );
        }
        Category category = c.get();
        category.setName(categoryDto.name());
        category.setDescription(categoryDto.description());

        return categoryRepository.save(category);
    }


}
