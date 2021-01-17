package com.duykypaul.wmanage_api.controllers;

import com.duykypaul.wmanage_api.beans.ToriaiHeadBean;
import com.duykypaul.wmanage_api.payload.respone.ResponseBean;
import com.duykypaul.wmanage_api.services.ToriaiHeadService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/toriais")
public class ToriaiController extends BaseController{

    @Autowired
    ToriaiHeadService toriaiHeadService;


    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            return toriaiHeadService.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }


    @PostMapping("/exeAlgorithm")
    public ResponseEntity<?> exeAlgorithm(@RequestBody ToriaiHeadBean toriaiHeadBean) {
        try {
            return toriaiHeadService.exeAlgorithm(toriaiHeadBean, getUser());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, "algorithm error!"));
        }
    }


    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody ToriaiHeadBean toriaiHeadBean) {
        try {
            return toriaiHeadService.saveToriai(toriaiHeadBean);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllByIdIn(@RequestBody Long[] ids) {
        try {
            return toriaiHeadService.deleteAllByIdIn(ids);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }
}
