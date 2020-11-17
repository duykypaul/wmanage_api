package com.duykypaul.wmanage_api.services.impl;


import com.duykypaul.wmanage_api.repository.BranchRepository;
import com.duykypaul.wmanage_api.services.BranchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BranchServiceImpl implements BranchService {

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(branchRepository.findAll());
    }
}
