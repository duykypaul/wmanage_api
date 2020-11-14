package com.duykypaul.wmanage_api.services.impl;


import com.duykypaul.wmanage_api.repository.BranchRepository;
import com.duykypaul.wmanage_api.services.BranchService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BranchServiceImpl implements BranchService {
    private static final Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(branchRepository.findAll());
    }
}
