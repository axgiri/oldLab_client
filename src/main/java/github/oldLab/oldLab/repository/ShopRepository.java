package github.oldLab.oldLab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import github.oldLab.oldLab.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    
}
