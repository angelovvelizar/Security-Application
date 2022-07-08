package com.example.securityapplication.service;

import com.example.securityapplication.model.entity.UserEntity;
import com.example.securityapplication.model.entity.UserRoleEntity;
import com.example.securityapplication.model.enums.UserRoleEnum;
import com.example.securityapplication.repository.UserRepository;
import com.example.securityapplication.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void init(){
        if(userRepository.count() == 0 && userRoleRepository.count() == 0){
            UserRoleEntity adminRole = new UserRoleEntity().setUserRole(UserRoleEnum.ADMIN);
            UserRoleEntity moderatorRole = new UserRoleEntity().setUserRole(UserRoleEnum.MODERATOR);

            userRoleRepository.save(adminRole);
            userRoleRepository.save(moderatorRole);

            initAdmin(List.of(adminRole,moderatorRole));
            initModerator(List.of(moderatorRole));
            initUser(List.of());
        }
    }

    private void initUser(List<UserRoleEntity> roles) {
        UserEntity user = new UserEntity()
                .setEmail("user@example.com")
                .setFirstName("User")
                .setLastName("Userov")
                .setPassword(passwordEncoder.encode("12345"))
                .setUserRoles(roles);

        userRepository.save(user);
    }

    private void initModerator(List<UserRoleEntity> moderatorRoles) {
        UserEntity moderator = new UserEntity()
                .setEmail("moderator@example.com")
                .setFirstName("Moderator")
                .setLastName("Moderatorov")
                .setPassword(passwordEncoder.encode("12345"))
                .setUserRoles(moderatorRoles);

        userRepository.save(moderator);
    }

    private void initAdmin(List<UserRoleEntity> adminRoles) {
        UserEntity admin = new UserEntity()
                .setEmail("admin@example.com")
                .setFirstName("Admin")
                .setLastName("Adminov")
                .setPassword(passwordEncoder.encode("12345"))
                .setUserRoles(adminRoles);

        userRepository.save(admin);
    }


}
