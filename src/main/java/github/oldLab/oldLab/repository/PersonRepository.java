package github.oldLab.oldLab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.oldLab.oldLab.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    Optional<Person> findByPhoneNumber(String phoneNumber);

    List<Person> findByCompanyId(Long companyId);
}
