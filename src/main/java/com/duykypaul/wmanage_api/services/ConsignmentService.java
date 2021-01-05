package com.duykypaul.wmanage_api.services;


import com.duykypaul.wmanage_api.beans.ConsignmentBean;
import org.springframework.http.ResponseEntity;

public interface ConsignmentService {
    ResponseEntity<?> findAll();
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy);
    ResponseEntity<?> deleteAllByIdIn(Long[] ids);
    ResponseEntity<?> saveConsignment(ConsignmentBean consignmentBean);
}
