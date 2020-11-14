package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByMaterialNo(String materialNo);

//    @Query("from Material where isDeleted = false")
    List<Material> findAllByIsDeletedIsFalse();
}
