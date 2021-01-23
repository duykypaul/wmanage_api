package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {
    List<Consignment> findAllByIsDeletedIsFalse();

    @Modifying
    @Query("UPDATE Consignment c SET c.isDeleted = true WHERE c.order.id IN ?1")
    void deleteByIdIn(Long[] ids);

    Optional<Consignment> findByConsignmentNoAndLength(String consignmentNo, Integer length);
}
