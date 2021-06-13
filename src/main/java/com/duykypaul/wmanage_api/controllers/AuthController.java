package com.duykypaul.wmanage_api.controllers;


import com.duykypaul.wmanage_api.beans.UserBean;
import com.duykypaul.wmanage_api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    final
    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> signIn(@Valid @RequestBody UserBean UserBean) {
        return userService.signIn(UserBean);
    }
}
