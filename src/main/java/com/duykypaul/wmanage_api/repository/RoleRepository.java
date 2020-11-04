package com.duykypaul.wmanage_api.repository;


import com.duykypaul.wmanage_api.model.ERole;
import com.duykypaul.wmanage_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
