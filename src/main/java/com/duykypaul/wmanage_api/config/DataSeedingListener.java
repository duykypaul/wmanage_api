package com.duykypaul.wmanage_api.config;


import com.duykypaul.wmanage_api.common.CommonConst;
import com.duykypaul.wmanage_api.common.Utils;
import com.duykypaul.wmanage_api.model.*;
import com.duykypaul.wmanage_api.repository.*;
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
    private MaterialRepository materialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // Add Roles
        if (!roleRepository.findByName(CommonConst.AUTH.ROLE.ROLE_ADMIN).isPresent()) {
            roleRepository.save(new Role(CommonConst.AUTH.ROLE.ROLE_ADMIN));
        }

        if (!roleRepository.findByName(CommonConst.AUTH.ROLE.ROLE_MODERATOR).isPresent()) {
            roleRepository.save(new Role(CommonConst.AUTH.ROLE.ROLE_MODERATOR));
        }

        if (!roleRepository.findByName(CommonConst.AUTH.ROLE.ROLE_USER).isPresent()) {
            roleRepository.save(new Role(CommonConst.AUTH.ROLE.ROLE_USER));
        }

        // Add list MaterialType
        List<MaterialType> materialTypeList = new ArrayList<>();
        CommonConst.MATERIAL_TYPE.LST_MATERIAL_TYPE.forEach(item -> {
            materialTypeList.add(new MaterialType(item.getValue0(), item.getValue1(), item.getValue2()));
        });
        if (materialTypeRepository.count() == 0) {
            materialTypeRepository.saveAll(materialTypeList);
        }

        // Add list Branch
        List<Branch> branchList = new ArrayList<>();
        CommonConst.BRANCH.LST_BRANCH.forEach(item -> {
            branchList.add(new Branch(item.getValue0(), item.getValue1(), item.getValue2()));
        });
        if (branchRepository.count() == 0) {
            branchRepository.saveAll(branchList);
        }

        // Admin account
        if (!userRepository.findByEmail(CommonConst.AUTH.ADMIN_EMAIL).isPresent()) {
            User admin = new User();
            admin.setEmail(CommonConst.AUTH.ADMIN_EMAIL);
            admin.setPassword(passwordEncoder.encode(CommonConst.AUTH.ADMIN_PASSWORD));
            admin.setUsername(CommonConst.AUTH.ADMIN_NAME);
            admin.setAvatar(CommonConst.AUTH.AVATAR);
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(CommonConst.AUTH.ROLE.ROLE_ADMIN).get());
            roles.add(roleRepository.findByName(CommonConst.AUTH.ROLE.ROLE_MODERATOR).get());
            roles.add(roleRepository.findByName(CommonConst.AUTH.ROLE.ROLE_USER).get());
            admin.setRoles(roles);
            admin.setEnabled(true);
            admin.setBranch(branchRepository.findByBranchName("HaNoi").get());
            userRepository.save(admin);
        }


        // Add list Material
        List<Material> materialList = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Material material = new Material();
            material.setBranch(branchRepository.getOne(3L));
            material.setMaterialNo(CommonConst.MATERIAL.SEI_KBN.B.name() + "HN" + Utils.LeadZeroNumber(i + 1, 8));
            material.setLength(CommonConst.MATERIAL.LENGTH_DEFAULT);
            material.setSeiKbn(CommonConst.MATERIAL.SEI_KBN.B.name());
            material.setStatus(CommonConst.MATERIAL.STATUS.ACTIVE.name());
            material.setMaterialType(materialTypeRepository.getOne(1L));
            material.setDeleted(false);
            materialList.add(material);
        }
        if (materialRepository.count() == 0) {
            materialRepository.saveAll(materialList);
        }
    }
}
