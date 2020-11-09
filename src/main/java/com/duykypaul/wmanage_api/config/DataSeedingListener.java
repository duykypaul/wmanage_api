package com.duykypaul.wmanage_api.config;


import com.duykypaul.wmanage_api.common.Constant;
import com.duykypaul.wmanage_api.model.*;
import com.duykypaul.wmanage_api.repository.BranchRepository;
import com.duykypaul.wmanage_api.repository.MaterialTypeRepository;
import com.duykypaul.wmanage_api.repository.RoleRepository;
import com.duykypaul.wmanage_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // Add Roles
        if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        if (!roleRepository.findByName(ERole.ROLE_MODERATOR).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        }

        if (!roleRepository.findByName(ERole.ROLE_USER).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_USER));
        }

        // Add list MaterialType
        List<MaterialType> materialTypeList = new ArrayList<>();
        Constant.MATERIAL_TYPE.LST_MATERIAL_TYPE.forEach(item -> {
            materialTypeList.add(new MaterialType(item.getKey(), item.getValue()));
        });
        if (materialTypeRepository.count() == 0) {
            materialTypeRepository.saveAll(materialTypeList);
        }

        // Add list Branch
        List<Branch> branchList = new ArrayList<>();
        Constant.BRANCH.LST_BRANCH.forEach(item -> {
            branchList.add(new Branch(item.getValue0(), item.getValue1(), item.getValue2()));
        });
        if (branchRepository.count() == 0) {
            branchRepository.saveAll(branchList);
        }

        // Admin account
        if (!userRepository.findByEmail(Constant.Auth.ADMIN_EMAIL).isPresent()) {
            User admin = new User();
            admin.setEmail(Constant.Auth.ADMIN_EMAIL);
            admin.setPassword(passwordEncoder.encode(Constant.Auth.ADMIN_PASSWORD));
            admin.setUsername(Constant.Auth.ADMIN_NAME);
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).get());
            roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR).get());
            roles.add(roleRepository.findByName(ERole.ROLE_USER).get());
            admin.setRoles(roles);
            admin.setEnabled(true);
            admin.setBranch(branchRepository.findByBranchName("HaNoi").get());
            userRepository.save(admin);
        }
    }
}
