package com.duykypaul.wmanage_api.services;


import com.duykypaul.wmanage_api.payload.request.MaterialReq;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MaterialService {
    ResponseEntity<?> findAll();
    ResponseEntity<?> saveALL(List<MaterialReq> userBean);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy);
    ResponseEntity<?> deleteAllByIdIn(Long[] ids);
    String generateMaterialNo();

}
