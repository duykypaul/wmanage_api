package com.duykypaul.wmanage_api.services;


import com.duykypaul.wmanage_api.beans.MaterialBean;
import com.duykypaul.wmanage_api.beans.MaterialTypeBean;
import com.duykypaul.wmanage_api.payload.request.MaterialReq;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MaterialService {
    ResponseEntity<?> findAll();
    ResponseEntity<?> saveALL(List<MaterialReq> materials);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy);
    ResponseEntity<?> deleteAllByIdIn(Long[] ids);
    String generateMaterialNo();

    List<MaterialBean> getAllBySeiKBN_B(String toriaiHeadNo, String branchName, MaterialTypeBean materialType);

    List<MaterialBean> getAllBySeiKBN_YR(String toriaiHeadNo, String branchName, MaterialTypeBean materialType, String typeToriai, Integer minInArray);
}
