package com.duykypaul.wmanage_api.services.impl;


import com.duykypaul.wmanage_api.beans.MaterialTypeBean;
import com.duykypaul.wmanage_api.model.MaterialType;
import com.duykypaul.wmanage_api.repository.MaterialTypeRepository;
import com.duykypaul.wmanage_api.services.MaterialTypeService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MaterialTypeServiceImpl implements MaterialTypeService {
    @Autowired
    MaterialTypeRepository materialTypeRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public ResponseEntity<?> findDistinctMaterialTypeAndAndMaterialTypeName() {
        List<MaterialType> materialTypeList = materialTypeRepository.findDistinctMaterialTypeAndAndMaterialTypeName();
        List<MaterialTypeBean> materialTypeBeans = modelMapper.map(materialTypeList, new TypeToken<List<MaterialTypeBean>>() {}.getType());
        return ResponseEntity.ok(materialTypeBeans);
    }

    @Override
    public ResponseEntity<List<String>> findDistinctDimension() {
        return ResponseEntity.ok(materialTypeRepository.findDistinctDimension());
    }
}
