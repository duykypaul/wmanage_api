package com.duykypaul.wmanage_api.services;


import com.duykypaul.wmanage_api.beans.UserBean;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

public interface UserService {
    ResponseEntity<?> signIn(@Valid UserBean loginReq);

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> findAll();

    ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy);
}
