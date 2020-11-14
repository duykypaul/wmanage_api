package com.duykypaul.wmanage_api.services;


import com.duykypaul.wmanage_api.beans.UserBean;
import org.springframework.http.ResponseEntity;

public interface MaterialService {
    ResponseEntity<?> findAll();
    ResponseEntity<?> saveALL(UserBean userBean);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy);
    String generateMaterialNo();
}
