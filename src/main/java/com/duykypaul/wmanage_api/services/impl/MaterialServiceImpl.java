package com.duykypaul.wmanage_api.services.impl;

import com.duykypaul.wmanage_api.beans.MaterialBean;
import com.duykypaul.wmanage_api.beans.MaterialTypeBean;
import com.duykypaul.wmanage_api.common.CommonConst;
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
                String key = CommonConst.MATERIAL.SEI_KBN.B.name() + branch.getBranchCode().toUpperCase();
                BranchAndMaterialNo.put(key, BranchAndMaterialNo.getOrDefault(key, maxMaterialNoCurrent));
                for (int i = 0; i < item.getQuantity(); i++) {
                    BranchAndMaterialNo.put(key, 1 + BranchAndMaterialNo.get(key));
                    Material material = Material.builder()
                        .branch(branch)
                        .materialNo(key + Utils.LeadZeroNumber(BranchAndMaterialNo.get(key), 8))
                        .seiKbn(CommonConst.MATERIAL.SEI_KBN.B.name())
                        .length(item.getLength())
                        .status(CommonConst.MATERIAL.STATUS.ACTIVE.name())
                        .materialType(materialType)
                        .build();

                    materials.add(material);
                }
            });
            materialRepository.saveAll(materials);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), null, "Save data success!"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
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

    @Override
    public List<MaterialBean> getAllBySeiKBN_B(String toriaiHeadNo, String branchName, MaterialTypeBean materialTypeBean) {
        List<MaterialBean> materialBeans = new ArrayList<>();
        try {
            List<Material> materials = materialRepository.getAllBySeiKBN_B(toriaiHeadNo, branchName, materialTypeBean.getMaterialTypeName(), materialTypeBean.getDimension());
            materialBeans = modelMapper.map(materials, CommonConst.MATERIAL.TYPE_LIST_BEAN);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return materialBeans;
    }

    @Override
    public List<MaterialBean> getAllBySeiKBN_YR(String toriaiHeadNo, String branchName, MaterialTypeBean materialType,
                                                String typeToriai, Integer minOrder) {
        List<MaterialBean> materialBeans = new ArrayList<>();
        try {
            List<Material> materials = materialRepository.getAllBySeiKBN_YR(toriaiHeadNo, branchName, materialType.getMaterialTypeName(), materialType.getDimension(), minOrder);
            materialBeans = modelMapper.map(materials, CommonConst.MATERIAL.TYPE_LIST_BEAN);
            if(typeToriai.equals(CommonConst.TORIAI.TYPE_TORIAI.FAST.name())) {
                materialBeans.sort(Comparator.comparing(MaterialBean::getLength).reversed());
            } else {
                materialBeans.sort(Comparator.comparing(MaterialBean::getLength));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return materialBeans;
    }
}
