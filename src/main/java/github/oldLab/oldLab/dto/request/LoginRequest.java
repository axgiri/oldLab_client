package github.oldLab.oldLab.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginRequest {
    
    @NotNull(message = "phone number cannot be null")
    private String phoneNumber;

    @NotNull(message = "password cannot be null")
    private String password;
}
