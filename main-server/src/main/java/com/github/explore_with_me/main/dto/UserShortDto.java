package com.github.explore_with_me.main.dto;

import com.github.explore_with_me.main.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UserShortDto {
    private Long id;
    private String name;

    public UserShortDto(final User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
