package com.duykypaul.wmanage_api.controllers;

import com.duykypaul.wmanage_api.services.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/branchs")
public class BranchController {
    private static final Logger logger = LoggerFactory.getLogger(BranchController.class);

    @Autowired
    BranchService branchService;

    @GetMapping()
    public ResponseEntity<?> findAll() {
        try {
            return branchService.findAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }

}
