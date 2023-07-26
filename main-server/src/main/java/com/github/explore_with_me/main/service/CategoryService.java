package com.github.explore_with_me.main.service;

import com.github.explore_with_me.main.dto.CategoryDto;
import com.github.explore_with_me.main.dto.NewCategoryDto;
import com.github.explore_with_me.main.dto.NewUserRequest;
import com.github.explore_with_me.main.exception.NotFoundException;
import com.github.explore_with_me.main.model.Category;
import com.github.explore_with_me.main.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<CategoryDto> getAll(final Integer from, final Integer size) {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> result = new ArrayList<>();
        categories.forEach(c -> result.add(new CategoryDto(c)));
        return result;
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public CategoryDto getCategory(final Long categoryId) throws NotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found"));
        return new CategoryDto(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto create(final NewCategoryDto newCategoryDto) {
        validate(newCategoryDto);
        Category category = Category.builder().name(newCategoryDto.getName()).build();
        categoryRepository.save(category);
        return new CategoryDto(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto update(final Long catId,
                              final NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        category.setName(newCategoryDto.getName());
        categoryRepository.save(category);
        return new CategoryDto(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(final Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        categoryRepository.delete(category);
    }

    private void validate(NewCategoryDto categoryDto) {
        if (categoryDto.getName() == null)
            throw new ValidationException("Category name cannot be empty.");
        if (categoryDto.getName().isBlank())
            throw new ValidationException("Category name cannot be empty.");
    }
}
