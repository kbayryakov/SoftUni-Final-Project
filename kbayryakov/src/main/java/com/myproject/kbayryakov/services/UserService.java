package com.myproject.kbayryakov.services;

import com.myproject.kbayryakov.errors.UserAlreadyExistException;
import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.notifications.service.NotificationService;
import com.myproject.kbayryakov.repositories.UserRepository;
import com.myproject.kbayryakov.web.dto.EditUserDto;
import com.myproject.kbayryakov.web.dto.RegisterUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final NotificationService notificationService;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.notificationService = notificationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    public void register (RegisterUserDto data) {
        if (!data.getPassword().equals(data.getConfirmPassword())) {
            throw new UserAlreadyExistException("Passwords do not match");
        }

        Optional<User> optionalUser = this.userRepository.findByUsernameOrEmail(data.getUsername(), data.getEmail());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException("User already exists");
        }

        User user = this.modelMapper.map(data, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        this.roleService.seedRolesInDb();
        if (this.userRepository.count() == 0) {
            user.setAuthorities(this.roleService.findAllRoles());
        } else {
            user.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        }

        this.userRepository.save(user);

        try {
            this.notificationService.saveUser(user.getId(), user.getEmail());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            String body = "Hello %s! Have a good time in the app!".formatted(user.getUsername());
            this.notificationService.sendNotification(user.getId(), "New user greetings", body);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public User findUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));
    }

    public void editUser(EditUserDto editUserDto) {
        User user = this.userRepository.findByEmail(editUserDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        user.setPassword(this.bCryptPasswordEncoder.encode(editUserDto.getNewPassword()));
        user.setUsername(editUserDto.getUsername());
        this.userRepository.save(user);
    }

    public List<User> findAllUsers () {
        return this.userRepository.findAll();
    }

    public void setUserRole(UUID id, String role) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id!"));

        user.getAuthorities().clear();

        switch (role) {
            case "user":
                user.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                break;
            case "moderator":
                user.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                user.getAuthorities().add(this.roleService.findByAuthority("ROLE_MODERATOR"));
                break;
            case "admin":
                user.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                user.getAuthorities().add(this.roleService.findByAuthority("ROLE_MODERATOR"));
                user.getAuthorities().add(this.roleService.findByAuthority("ROLE_ADMIN"));
                break;
        }

        this.userRepository.saveAndFlush(user);
    }
}
