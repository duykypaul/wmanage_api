package com.duykypaul.wmanage_api.config;


import com.duykypaul.wmanage_api.common.Constant;
import com.duykypaul.wmanage_api.model.ERole;
import com.duykypaul.wmanage_api.model.Role;
import com.duykypaul.wmanage_api.model.User;
import com.duykypaul.wmanage_api.repository.RoleRepository;
import com.duykypaul.wmanage_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // Add Roles
        if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        if (!roleRepository.findByName(ERole.ROLE_MODERATOR).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        }

        if (!roleRepository.findByName(ERole.ROLE_USER).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_USER));
        }

        // Admin account
        if (!userRepository.findByEmail(Constant.Auth.ADMIN_EMAIL).isPresent()) {
            User admin = new User();
            admin.setEmail(Constant.Auth.ADMIN_EMAIL);
            admin.setPassword(passwordEncoder.encode(Constant.Auth.ADMIN_PASSWORD));
            admin.setUsername(Constant.Auth.ADMIN_NAME);
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).get());
            roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR).get());
            roles.add(roleRepository.findByName(ERole.ROLE_USER).get());
            admin.setRoles(roles);
            admin.setEnabled(true);
            userRepository.save(admin);
        }

        // Add list category
       /* Constant.Category.LST_CATEGORY.forEach(item -> {
            if (!categoryRepository.existsByName(item)) {
                categoryRepository.save(new Category(item));
            }
        });*/

        // Add Post
        /*if (!(postRepository.count() > 0)) {
            for (int i = 0; i < 10; i++) {
                Post post = new Post();
                post.setCategories(new HashSet<>(Collections.singletonList(categoryRepository.findById((long) (Math.random() * 10)).get())));
                post.setContent("test" + i + Math.random() * 10);
                post.setUrlImage(Constant.Post.LST_URL_IMAGE.get(i));
                post.setUser(userRepository.findByEmail(Constant.Auth.ADMIN_EMAIL).get());
                postRepository.saveAndFlush(post);
            }
        }*/

    }
}
