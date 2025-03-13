package com.myproject.kbayryakov.web.validation;

import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.repositories.UserRepository;
import com.myproject.kbayryakov.web.dto.EditUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component
public class UserEditValidator implements org.springframework.validation.Validator{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserEditValidator(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return EditUserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EditUserDto editUserDto = (EditUserDto) target;

        Optional<User> optionalUser = this.userRepository.findByEmail(editUserDto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found"); // todo change exception
        }
        User user = optionalUser.get();

        if (!this.bCryptPasswordEncoder.matches(editUserDto.getPassword(), user.getPassword())) {
            errors.rejectValue(
                    "password",
                    "Wrong password",
                    "Wrong password"
            );
        }

        if (!editUserDto.getNewPassword().equals(editUserDto.getConfirmNewPassword())) {
            errors.rejectValue(
                    "newPassword",
                    "Passwords do not match",
                    "Passwords do not match"
            );
        }

        if (!user.getUsername().equals(editUserDto.getUsername())
                && this.userRepository.findByUsername(editUserDto.getUsername()).isPresent()) {
            errors.rejectValue(
                    "username",
                    "Username already exists",
                    "Username already exists"
            );
        }
    }
}
