package com.duykypaul.wmanage_api.controllers;

import com.duykypaul.wmanage_api.beans.MaterialBean;
import com.duykypaul.wmanage_api.services.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/materials")
public class MaterialController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialController.class);

    @Autowired
    MaterialService materialService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            return materialService.findAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllByIdIn(@RequestBody MaterialBean materialBean) {
        return materialService.deleteAllByIdIn(materialBean.getIds());
    }
}
