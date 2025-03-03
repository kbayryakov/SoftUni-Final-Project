package com.myproject.kbayryakov.services;

import com.myproject.kbayryakov.errors.UserAlreadyExistException;
import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.repositories.UserRepository;
import com.myproject.kbayryakov.web.dto.RegisterUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    public void register (RegisterUserDto data) {
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
    }

    public User findUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));//TODO create exception
    }

    public void editUser() {

    }
}
