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

    @Query("SELECT COALESCE(MAX(SUBSTRING(m.materialNo, 4, 8)), 0) FROM Material m WHERE m.branch.id = ?1 AND m.isDeleted = false")
    String generateMaterialNo(Long branchId);

    @Query("SELECT COALESCE(MAX(SUBSTRING(m.materialNo, 4, 8)), 0) FROM Material m WHERE m.branch.branchName = ?1 AND m.isDeleted = false")
    String generateMaterialNoByBranchName(String branchName);

    @Query("FROM Material m " +
        "WHERE m.seiKbn = 'B' AND ((COALESCE(m.toriaiHeadNoUsed, '') = '' AND m.status = 'ACTIVE') OR " +
        "(m.toriaiHeadNoUsed = :toriaiHeadNo AND m.status = 'PLAN')) " +
        "AND m.branch.branchName = :branchName " +
        "AND m.materialType.materialTypeName = :materialTypeName " +
        "AND m.materialType.dimension = :dimension AND m.isDeleted = false " +
        "ORDER BY m.createdAt")
    List<Material> getAllBySeiKBN_B(String toriaiHeadNo, String branchName, String materialTypeName, String dimension);

    @Query("FROM Material m " +
        "WHERE ((COALESCE(m.toriaiHeadNoUsed, '') = '' AND m.status = 'ACTIVE') " +
        "OR (m.toriaiHeadNoUsed = :toriaiHeadNo AND m.status = 'PLAN')) " +
        "AND ((m.seiKbn = 'Y' AND m.status = 'PLAN') OR (m.seiKbn = 'R' AND (m.status = 'ACTIVE' OR m.status = 'PLAN'))) " +
        "AND m.materialNo LIKE 'R%' " +
        "AND (:toriaiHeadNo = '' OR m.toriaiHeadNo <> :toriaiHeadNo)" +
        "AND m.branch.branchName = :branchName " +
        "AND m.materialType.materialTypeName = :materialTypeName " +
        "AND m.materialType.dimension = :dimension " +
        "AND m.length >= :minOrder " +
        "AND m.length > 500 " +
        "AND m.isDeleted = false " +
        "ORDER BY m.seiKbn, m.createdAt")
    List<Material> getAllBySeiKBN_YR(String toriaiHeadNo, String branchName, String materialTypeName, String dimension, Integer minOrder);
}
