package com.github.explore_with_me.main.dto;

import com.github.explore_with_me.main.model.Category;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Категория
 */
@Data
@NoArgsConstructor
@ToString
public class CategoryDto {
    private Long id;
    private String name;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public CategoryDto(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
}
