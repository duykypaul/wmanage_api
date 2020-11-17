package com.duykypaul.wmanage_api.controllers;

import com.duykypaul.wmanage_api.services.BranchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/branchs")
public class BranchController {

    @Autowired
    BranchService branchService;

    @GetMapping()
    public ResponseEntity<?> findAll() {
        try {
            return branchService.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }

}
