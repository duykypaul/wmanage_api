package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByBranchName(String name);
    Optional<Branch> findByBranchCode(String branchCode);
}
