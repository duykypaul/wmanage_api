package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MaterialTypeRepository extends JpaRepository<MaterialType, Long> {
    Optional<MaterialType> findByMaterialTypeAndDimension(String materialType, String dimension);

    Optional<MaterialType> findByMaterialTypeNameAndDimension(String materialTypeName, String dimension);

    Boolean existsByMaterialTypeAndDimension(String materialType, String dimension);

    @Query("FROM MaterialType u WHERE u.isDeleted = false GROUP BY u.materialType, u.materialTypeName")
    List<MaterialType> findDistinctMaterialTypeAndAndMaterialTypeName();

    @Query(value = "SELECT DISTINCT u.dimension FROM material_type u WHERE u.is_deleted = false", nativeQuery = true)
    List<String> findDistinctDimension();
}
