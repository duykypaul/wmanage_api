package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByMaterialNo(String materialNo);

    List<Material> findAllByIsDeletedIsFalse();

    @Modifying
    @Query("UPDATE Material SET isDeleted = true WHERE id IN ?1")
    void deleteAllByIdIn(List<Long> ids);

    @Query("SELECT COALESCE(MAX(SUBSTRING(m.materialNo, 4, 8)), 0) FROM Material m where m.branch.id = ?1 and m.isDeleted = false")
    String generateMaterialNo(Long branchId);
}
