package com.duykypaul.wmanage_api.controllers;


import com.duykypaul.wmanage_api.beans.UserBean;
import com.duykypaul.wmanage_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<?> signIn(@Valid @RequestBody UserBean UserBean) {
        return userService.signIn(UserBean);
    }
}
