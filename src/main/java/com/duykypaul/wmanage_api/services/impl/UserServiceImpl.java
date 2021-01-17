package com.duykypaul.wmanage_api.services.impl;


import com.duykypaul.wmanage_api.beans.UserBean;
import com.duykypaul.wmanage_api.model.User;
import com.duykypaul.wmanage_api.payload.respone.JwtBean;
import com.duykypaul.wmanage_api.payload.respone.MessageBean;
import com.duykypaul.wmanage_api.payload.respone.ResponseBean;
import com.duykypaul.wmanage_api.repository.RoleRepository;
import com.duykypaul.wmanage_api.repository.UserRepository;
import com.duykypaul.wmanage_api.security.jwt.JwtUtils;
import com.duykypaul.wmanage_api.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> signIn(@Valid UserBean loginReq) {
        try {
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword());
            Authentication authentication = authenticationManager.authenticate(authReq);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtUtils.generateJwtToken(authentication);

            Optional<User> user = userRepository.findByUsername(authentication.getName());
            UserBean userBean = new UserBean();
            if (user.isPresent()) {
                userBean = modelMapper.map(user.get(), UserBean.class);
            }

            return ResponseEntity.ok(new JwtBean(HttpStatus.OK.value(), jwtToken, userBean));
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(new MessageBean(HttpStatus.UNAUTHORIZED.value(), "Email or password invalid!"));
        }
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: User Id is not found"));
            UserBean userBean = modelMapper.map(user, UserBean.class);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), userBean, "Success"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.UNAUTHORIZED.value(), null, "UnAuthorized"));
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Error: User Id is not found"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

    @Override
    public ResponseEntity<?> findAll() {
        List<User> users = userRepository.findAll();
        return convertListUserToUserBean(users);
    }

    @Override
    public ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        List<User> users = userRepository.findAll(paging).getContent();
        return convertListUserToUserBean(users);
    }

    private ResponseEntity<?> convertListUserToUserBean(List<User> users) {
        List<UserBean> userBeans = new ArrayList<>();
        users.forEach((user) -> {
            UserBean userBean = modelMapper.map(user, UserBean.class);
            userBeans.add(userBean);
        });
        return ResponseEntity.ok(userBeans);
    }
}
