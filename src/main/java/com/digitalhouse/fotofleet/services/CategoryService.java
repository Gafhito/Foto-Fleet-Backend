package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }
}
