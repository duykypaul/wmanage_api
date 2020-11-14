package com.duykypaul.wmanage_api.services.impl;


import com.duykypaul.wmanage_api.beans.MaterialBean;
import com.duykypaul.wmanage_api.beans.UserBean;
import com.duykypaul.wmanage_api.model.Material;
import com.duykypaul.wmanage_api.payload.respone.ResponseBean;
import com.duykypaul.wmanage_api.repository.MaterialRepository;
import com.duykypaul.wmanage_api.services.MaterialService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {
    private static final Logger logger = LoggerFactory.getLogger(MaterialServiceImpl.class);

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> findAll() {
        List<MaterialBean> materialBeans = new ArrayList<>();
        try {
            List<Material> materials = materialRepository.findAllByIsDeletedIsFalse();
            materials.forEach((material) -> {
                MaterialBean materialBean = modelMapper.map(material, MaterialBean.class);
                materialBeans.add(materialBean);
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(materialBeans);
    }

    @Override
    public ResponseEntity<?> saveALL(UserBean userBean) {
        return null;
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
            System.out.println(Arrays.toString(Arrays.asList(ids).toArray()));
            materialRepository.deleteAllByIdIn(Arrays.asList(ids));
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), null, "success"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, "error"));
    }

    @Override
    public String generateMaterialNo() {
        return null;
    }
}
