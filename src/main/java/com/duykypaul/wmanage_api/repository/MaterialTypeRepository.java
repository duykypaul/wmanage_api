package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaterialTypeRepository extends JpaRepository<MaterialType, Long> {
    Optional<MaterialType> findByMaterialTypeAndDimension(String materialType, String dimension);
    Boolean existsByMaterialTypeAndDimension(String materialType, String dimension);
}
