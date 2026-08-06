package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is required.")
    @Size(max = 50,message = "Name cannot be more than 50 character")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be a valid email address")
    @Size(max = 254,message = "Email cannot be more than 254 character")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be valid, e.g. +919876543210")
    private String phone;

    private String avatarUrl;

}
