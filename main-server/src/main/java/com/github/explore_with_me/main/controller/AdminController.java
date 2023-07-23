package com.github.explore_with_me.main.controller;

import com.github.explore_with_me.main.dto.CategoryDto;
import com.github.explore_with_me.main.dto.NewCategoryDto;
import com.github.explore_with_me.main.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final CategoryService categoryService;

    public AdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.create(newCategoryDto));
    }

    @RequestMapping(value = "/categories/{catId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable(value = "catId") Long catId,
                                                      @RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.update(catId, newCategoryDto));
    }

    @RequestMapping(value = "/categories/{catId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteCategory(@PathVariable(value = "catId") Long catId) {
        categoryService.delete(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
