package com.duykypaul.wmanage_api.controllers;

import com.duykypaul.wmanage_api.beans.MaterialBean;
import com.duykypaul.wmanage_api.payload.request.MaterialReq;
import com.duykypaul.wmanage_api.services.MaterialService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    MaterialService materialService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            return materialService.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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

    @PostMapping
    public ResponseEntity<?> saveAll(@RequestBody List<MaterialReq> materialBeans) {
        return materialService.saveALL(materialBeans);
    }
}
