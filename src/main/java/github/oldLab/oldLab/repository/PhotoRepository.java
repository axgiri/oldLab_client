package github.oldLab.oldLab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import github.oldLab.oldLab.entity.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findByPersonId(Long personId);
    
    Optional<Photo> findByShopId(Long shopId);
}
