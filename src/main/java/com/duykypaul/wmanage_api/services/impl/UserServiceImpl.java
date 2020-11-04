package com.duykypaul.wmanage_api.services.impl;


import com.duykypaul.wmanage_api.beans.RoleBean;
import com.duykypaul.wmanage_api.beans.UserBean;
import com.duykypaul.wmanage_api.common.Constant;
import com.duykypaul.wmanage_api.model.ERole;
import com.duykypaul.wmanage_api.model.Role;
import com.duykypaul.wmanage_api.model.User;
import com.duykypaul.wmanage_api.payload.request.LoginReq;
import com.duykypaul.wmanage_api.payload.respone.JwtBean;
import com.duykypaul.wmanage_api.payload.respone.MessageBean;
import com.duykypaul.wmanage_api.payload.respone.ResponseBean;
import com.duykypaul.wmanage_api.repository.RoleRepository;
import com.duykypaul.wmanage_api.repository.UserRepository;
import com.duykypaul.wmanage_api.security.jwt.JwtUtils;
import com.duykypaul.wmanage_api.security.services.EmailSenderService;
import com.duykypaul.wmanage_api.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public ResponseEntity<?> signIn(LoginReq loginReq) {
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
            logger.error(e.getMessage(), e);
            return ResponseEntity.ok(new MessageBean(HttpStatus.UNAUTHORIZED.value(), "Email or password invalid!"));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> signUp(UserBean userBean) {
        ModelMapper modelMapper = new ModelMapper();
        if (userRepository.existsByUsername(userBean.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageBean(0, "Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(userBean.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageBean(0, "Email is already taken!"));
        }

        userBean.setPassword(passwordEncoder.encode(userBean.getPassword()));
        User user = modelMapper.map(userBean, User.class);

        Set<RoleBean> roleBeans = userBean.getRoles();
        Set<Role> roles = new HashSet<>();

        if (null == roleBeans) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            roleBeans.forEach(role -> {
                switch (role.getName().name().toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        user.setProfilePicture(Constant.Auth.AVATAR_DEFAULT);
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), userBean, "Please check gmail to confirm your account!"));
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: User Id is not found"));
            UserBean userBean = modelMapper.map(user, UserBean.class);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), userBean, "Success"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.UNAUTHORIZED.value(), null, "UnAuthorized"));
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
