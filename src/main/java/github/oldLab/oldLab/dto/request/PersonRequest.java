package github.oldLab.oldLab.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import github.oldLab.oldLab.Enum.RoleEnum;
import github.oldLab.oldLab.entity.Person;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonRequest {

    @Schema(description = "Имя пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    @NotNull(message = "first name cannot be null") // [Tais0ft & axgiri]: Валидация — имя обязательно
    @Size(min = 2, max = 32, message = "first name must be between 2 and 32 characters") // [Tais0ft & axgiri]: Валидация — длина имени
    private String firstName;
    
    @Schema(description = "Фамилия пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    @NotNull(message = "last name cannot be null") // [Tais0ft & axgiri]: Валидация — фамилия обязательна
    @Size(min = 2, max = 32, message = "last name must be between 2 и 32 characters") // [Tais0ft & axgiri]: Валидация — длина фамилии
    private String lastName;

    @Schema(description = "Номер телефона пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    @NotNull(message = "phone number cannot be null") // [Tais0ft & axgiri]: Валидация — телефон обязателен
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "phone number must be valid") // [Tais0ft & axgiri]: Валидация — формат телефона
    private String phoneNumber;

    @Schema(description = "Пароль пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    @NotNull(message = "password cannot be null") // [Tais0ft & axgiri]: Валидация — пароль обязателен
    @Size(min = 6, max = 64, message = "password must be between 6 and 64 characters") // [Tais0ft & axgiri]: Валидация — длина пароля
    private String password;

    @Schema(description = "Роль пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    private RoleEnum role;

    @Schema(description = "Активен ли пользователь") // [Tais0ft & axgiri]: Описание для Swagger
    private boolean isActive;
}

