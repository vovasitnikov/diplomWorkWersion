package com.github.explore_with_me.main.controller;

import com.github.explore_with_me.main.dto.CategoryDto;
import com.github.explore_with_me.main.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
Публичный API для работы с категориями
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDto>> getAll(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.getAll(from, size));
    }

    @RequestMapping(value = "/{catId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> getOne(@PathVariable(value = "catId") Long catId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.getCategory(catId));
    }
}
