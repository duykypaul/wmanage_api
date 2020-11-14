package com.duykypaul.wmanage_api.services;


import com.duykypaul.wmanage_api.payload.request.LoginReq;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> signIn(LoginReq loginReq);

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> findAll();

    ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy);
}
