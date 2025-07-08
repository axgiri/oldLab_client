package github.oldLab.oldLab.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import github.oldLab.oldLab.entity.Person;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Accessors(chain = true)
public class PersonResponse {
    
    @Schema(description = "ID пользователя") // [Tais0ft & axgiri]: Описание для Swagger
    @JsonProperty("id") // [axgiri]: JSON-имя поля
    private Long id;

    @Schema(description = "Версия записи")
    @JsonProperty("version")
    private Long version;

    @Schema(description = "Имя пользователя")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "Номер телефона пользователя")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Schema(description = "ID компании")
    @JsonProperty("company_id")
    private Long companyId;

    @Schema(description = "Дата создания")
    @JsonProperty("created_at")
    private LocalDate createdAt;

    @Schema(description = "Дата обновления")
    @JsonProperty("updated_at")
    private LocalDate updatedAt;
    
    public static PersonResponse fromEntityToDto(Person person) {
        return new PersonResponse()
            .setId(person.getId())
            .setVersion(person.getVersion())
            .setFirstName(person.getFirstName())
            .setLastName(person.getLastName())
            .setPhoneNumber(person.getPhoneNumber())
            .setCompanyId(person.getCompanyId())
            .setCreatedAt(person.getCreatedAt())
            .setUpdatedAt(person.getUpdatedAt());
    }
}
