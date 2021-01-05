package com.duykypaul.wmanage_api.controllers;

import com.duykypaul.wmanage_api.beans.OrderBean;
import com.duykypaul.wmanage_api.services.ConsignmentService;
import com.duykypaul.wmanage_api.services.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ConsignmentService consignmentService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            return orderService.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }

    @GetMapping("/consignments")
    public ResponseEntity<?> findAllConsignments() {
        try {
            return consignmentService.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id) {
        return null;
    }


    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody OrderBean orderBean) {
        try {
            return orderService.saveOrder(orderBean);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error");
        }

    }
}
