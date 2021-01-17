package com.duykypaul.wmanage_api.controllers;

import com.duykypaul.wmanage_api.model.User;
import com.duykypaul.wmanage_api.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class BaseController {
    @Autowired
    UserService userService;

    public final User getUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                String userCd = auth.getName();
                return userService.findByUsername(userCd);
            }
            return new User();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new User();
        }
    }
}
