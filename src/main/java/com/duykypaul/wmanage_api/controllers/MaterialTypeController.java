package com.duykypaul.wmanage_api.controllers;

import com.duykypaul.wmanage_api.services.MaterialTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/material-type")
public class MaterialTypeController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialTypeController.class);

    @Autowired
    MaterialTypeService materialTypeService;

    @GetMapping("/types")
    public ResponseEntity<?> findDistinctByMaterialTypeAndAndMaterialTypeName() {
        try {
            return materialTypeService.findDistinctMaterialTypeAndAndMaterialTypeName();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }

    @GetMapping("/dimensions")
    public ResponseEntity<?> findDistinctDimension() {
        try {
            return materialTypeService.findDistinctDimension();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }

}
