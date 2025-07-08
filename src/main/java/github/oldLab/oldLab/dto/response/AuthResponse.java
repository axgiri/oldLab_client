package github.oldLab.oldLab.dto.response;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class AuthResponse {

    @Schema(description = "JWT токен пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    private String token;
    @Schema(description = "Данные пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    private PersonResponse person;

    public AuthResponse(String token, PersonResponse person) {
        this.token = token;
        this.person = person;
    }
}
