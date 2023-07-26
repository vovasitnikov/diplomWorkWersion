package com.github.explore_with_me.main.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ToString
public class NewUserRequest {
    @Size(min = 6, max = 254)
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    @Size(min = 2, max = 254)
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;
}
