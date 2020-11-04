package com.duykypaul.wmanage_api.controllers;

import com.duykypaul.wmanage_api.common.Constant;
import com.duykypaul.wmanage_api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/pagination")
    public ResponseEntity<?> findAllPaging(@RequestParam(defaultValue = "0") Integer pageNo,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(defaultValue = "id") String sortBy) {
        return userService.findAll(pageNo, pageSize, sortBy);
    }

    @GetMapping("/get-image/{image}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String image) {
        if (!StringUtils.isEmpty(image)) {
            try {
                Path filename = Paths.get(Constant.UPLOAD_ROOT, Constant.UPLOAD_USER, image);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
            } catch (IOException io) {
                logger.error(io.getMessage(), io);
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
