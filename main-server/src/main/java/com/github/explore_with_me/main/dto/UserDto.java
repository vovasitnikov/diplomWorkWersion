package com.github.explore_with_me.main.dto;

import com.github.explore_with_me.main.model.Category;
import com.github.explore_with_me.main.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UserDto {
    private String email;
    private Long id;
    private String name;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public UserDto(final Long id, final String name, final String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UserDto(final String name, final String email) {
        this.name = name;
        this.email = email;
    }
}
