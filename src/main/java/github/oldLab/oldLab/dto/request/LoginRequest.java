package github.oldLab.oldLab.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginRequest {
    
    @Schema(description = "Номер телефона пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    @NotNull(message = "phone number cannot be null") // [Tais0ft & axgiri]: Валидация — телефон обязателен
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "phone number must be valid") // [Tais0ft & axgiri]: Валидация — формат телефона
    private String phoneNumber;

    @Schema(description = "Пароль пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    @NotNull(message = "password cannot be null") // [Tais0ft & axgiri]: Валидация — пароль обязателен
    @Size(min = 6, max = 64, message = "password must be between 6 and 64 characters") // [Tais0ft & axgiri]: Валидация — длина пароля
    private String password;
}
