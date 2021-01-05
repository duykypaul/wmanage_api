package com.duykypaul.wmanage_api.services;


import com.duykypaul.wmanage_api.beans.OrderBean;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<?> findAll();
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy);
    ResponseEntity<?> deleteAllByIdIn(Long[] ids);
    ResponseEntity<?> saveOrder(OrderBean orderBean);
}
