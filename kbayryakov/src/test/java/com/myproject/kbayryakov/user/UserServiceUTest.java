package com.myproject.kbayryakov.user;

import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.repositories.UserRepository;
import com.myproject.kbayryakov.services.UserService;
import com.myproject.kbayryakov.web.dto.EditUserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testEditUser_success() {
        EditUserDto editUserDto = new EditUserDto();
        editUserDto.setEmail("ivan@example.com");
        editUserDto.setUsername("Ivan");
        editUserDto.setNewPassword("newPassword123");

        User existingUser = new User();
        existingUser.setEmail("ivan@example.com");
        existingUser.setUsername("Ivan");
        existingUser.setPassword("oldPassword");

        when(userRepository.findByEmail(editUserDto.getEmail())).thenReturn(Optional.of(existingUser));

        when(bCryptPasswordEncoder.encode(editUserDto.getNewPassword())).thenReturn("encodedPassword");

        userService.editUser(editUserDto);

        assertEquals("encodedPassword", existingUser.getPassword());

        assertEquals("Ivan", existingUser.getUsername());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testEditUser_userNotFound() {
        EditUserDto editUserDto = new EditUserDto();
        editUserDto.setEmail("ivan@example.com");
        editUserDto.setUsername("Ivan");
        editUserDto.setNewPassword("newPassword123");

        User existingUser = new User();
        existingUser.setEmail("ivan@example.com");
        existingUser.setUsername("Ivan");
        existingUser.setPassword("oldPassword");

        when(userRepository.findByEmail(editUserDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.editUser(editUserDto);
        });
    }

    @Test
    public void testEditUser_repositorySaveCalled() {
        EditUserDto editUserDto = new EditUserDto();
        editUserDto.setEmail("ivan@example.com");
        editUserDto.setUsername("Ivan");
        editUserDto.setNewPassword("newPassword123");

        User existingUser = new User();
        existingUser.setEmail("ivan@example.com");
        existingUser.setUsername("Ivan");
        existingUser.setPassword("oldPassword");

        when(userRepository.findByEmail(editUserDto.getEmail())).thenReturn(Optional.of(existingUser));

        when(bCryptPasswordEncoder.encode(editUserDto.getNewPassword())).thenReturn("encodedPassword");

        userService.editUser(editUserDto);

        verify(userRepository, times(1)).save(existingUser);
    }
}
