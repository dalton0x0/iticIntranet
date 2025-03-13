package com.itic.intranet.dtos;

import com.itic.intranet.models.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotNull(message = "Firstname can not be null !")
    @NotBlank(message = "Firstname is empty !")
    private String firstname;
    @NotNull(message = "Lastname can not be null !")
    @NotBlank(message = "Lastname is empty !")
    private String lastname;
    @NotNull(message = "Email can not be null !")
    @NotBlank(message = "Email is empty !")
    @Email(message = "Email must be valid !")
    private String email;
    @NotNull(message = "Username can not be null !")
    @NotBlank(message = "Username is empty !")
    @Size(min = 3, message = "Username must have minimum 3 characters !")
    private String username;
    @NotNull(message = "Password can not be null !")
    @NotBlank(message = "Password is empty !")
    @Size(min = 8, message = "Password must have minimum 8 characters !")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain 8 characters, one uppercase letter, one lowercase letter, one number and one special character !")
    private String password;
    @NotNull(message = "Role can not be null !")
    private Role role;
}
