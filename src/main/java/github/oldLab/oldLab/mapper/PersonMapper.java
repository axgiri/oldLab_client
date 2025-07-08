// [Tais0ft]: Этот класс создан для маппинга PersonRequest в Person (ручной маппинг)
package github.oldLab.oldLab.mapper;

import github.oldLab.oldLab.dto.request.PersonRequest;
import github.oldLab.oldLab.entity.Person;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    public Person toEntity(PersonRequest request) {
        return new Person()
            .setFirstName(request.getFirstName())
            .setLastName(request.getLastName())
            .setPhoneNumber(request.getPhoneNumber())
            .setPassword(request.getPassword())
            .setRoleEnum(request.getRole())
            .setCreatedAt(LocalDate.now())
            .setUpdatedAt(LocalDate.now());
    }
} 