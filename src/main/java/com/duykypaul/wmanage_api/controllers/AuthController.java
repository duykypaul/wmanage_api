package com.duykypaul.wmanage_api.controllers;


import com.duykypaul.wmanage_api.payload.request.LoginReq;
import com.duykypaul.wmanage_api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginReq loginReq) {
        return userService.signIn(loginReq);
    }
}
