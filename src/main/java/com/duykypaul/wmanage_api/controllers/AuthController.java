package com.duykypaul.wmanage_api.controllers;


import com.duykypaul.wmanage_api.beans.UserBean;
import com.duykypaul.wmanage_api.model.User;
import com.duykypaul.wmanage_api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController  extends BaseController {
    final
    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> signIn(@Valid @RequestBody UserBean UserBean) {
        return userService.signIn(UserBean);
    }
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo() {
        User user = getUser();
        if(user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
