package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {
    List<Consignment> findAllByIsDeletedIsFalse();
}
