package com.duykypaul.wmanage_api.services.impl;


import com.duykypaul.wmanage_api.beans.MaterialBean;
import com.duykypaul.wmanage_api.common.EMaterialStatus;
import com.duykypaul.wmanage_api.common.Utils;
import com.duykypaul.wmanage_api.model.Branch;
import com.duykypaul.wmanage_api.model.Material;
import com.duykypaul.wmanage_api.model.MaterialType;
import com.duykypaul.wmanage_api.payload.request.MaterialReq;
import com.duykypaul.wmanage_api.payload.respone.ResponseBean;
import com.duykypaul.wmanage_api.repository.BranchRepository;
import com.duykypaul.wmanage_api.repository.MaterialRepository;
import com.duykypaul.wmanage_api.repository.MaterialTypeRepository;
import com.duykypaul.wmanage_api.services.MaterialService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j2
@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    MaterialTypeRepository materialTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> findAll() {
        List<MaterialBean> materialBeans = new ArrayList<>();
        try {
            List<Material> materials = materialRepository.findAllByIsDeletedIsFalse();
            materials.forEach(material -> {
                MaterialBean materialBean = modelMapper.map(material, MaterialBean.class);
                materialBeans.add(materialBean);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(materialBeans);
    }

    @Override
    public ResponseEntity<?> saveALL(List<MaterialReq> materialReqs) {
        try {
            List<Material> materials = new ArrayList<>();
            Map<String, Integer> BranchAndMaterialNo = new HashMap<>();
            materialReqs.forEach(item -> {
                Branch branch = branchRepository.findByBranchCode(item.getBranchCode())
                    .orElseThrow(() -> new RuntimeException("Branch code notfound"));
                MaterialType materialType = materialTypeRepository.findByMaterialTypeAndDimension(item.getMaterialType(), item.getDimension())
                    .orElseThrow(() -> new RuntimeException("MaterialType code notfound"));
                int maxMaterialNoCurrent = Integer.parseInt(materialRepository.generateMaterialNo(branch.getId()));
                String key = branch.getBranchCode().toUpperCase();
                BranchAndMaterialNo.put(key, BranchAndMaterialNo.getOrDefault(key, maxMaterialNoCurrent));
                for (int i = 0; i < item.getQuantity(); i++) {
                    BranchAndMaterialNo.put(key, 1 + BranchAndMaterialNo.get(key));
                    Material material = new Material();
                    material.setMaterialNo(key + Utils.LeadZeroNumber(BranchAndMaterialNo.get(key), 5));
                    material.setLength(item.getLength());
                    material.setStatus(EMaterialStatus.ACTIVE.name());
                    material.setBranch(branch);
                    material.setMaterialType(materialType);

                    materials.add(material);
                }
            });
            materialRepository.saveAll(materials);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), null, "success"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, "error"));
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteAllByIdIn(Long[] ids) {
        try {
            materialRepository.deleteAllByIdIn(Arrays.asList(ids));
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), null, "success"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, "error"));
    }

    @Override
    public String generateMaterialNo() {
        return null;
    }
}
