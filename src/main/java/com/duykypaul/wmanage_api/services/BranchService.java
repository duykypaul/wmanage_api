package com.duykypaul.wmanage_api.services;


import org.springframework.http.ResponseEntity;

public interface BranchService {
    ResponseEntity<?> findAll();
}
