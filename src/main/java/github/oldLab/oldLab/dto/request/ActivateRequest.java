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
public class ActivateRequest {

    @Schema(description = "Номер телефона пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    @NotNull(message = "phone number cannot be null") // [Tais0ft & axgiri]: Валидация — телефон обязателен
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "phone number must be valid") // [Tais0ft & axgiri]: Валидация — формат телефона
    private String phoneNumber;

    @Schema(description = "OTP-код для активации/входа") // [Tais0ft & axgiri]: Описание для Swagger
    @NotNull(message = "otp cannot be null") // [Tais0ft & axgiri]: Валидация — OTP обязателен
    @Size(min = 4, max = 8, message = "otp must be between 4 and 8 digits") // [Tais0ft & axgiri]: Валидация — длина OTP
    private String otp;
}
