package com.myproject.kbayryakov.web;

import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.services.UserService;
import com.myproject.kbayryakov.web.controllers.ProfileController;
import com.myproject.kbayryakov.web.dto.EditUserDto;
import com.myproject.kbayryakov.web.validation.UserEditValidator;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
public class ProfileControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ModelMapper modelMapper;

    @MockitoBean
    private UserEditValidator userEditValidator;

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    public void testViewProfile_authenticatedUser() throws Exception {
        User user = new User();
        user.setUsername("Ivan");

        when(userService.findUserByUsername("Ivan")).thenReturn(user);

        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/profile"))
                .andExpect(model().attributeExists("model"))
                .andExpect(model().attribute("model", user));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    public void testGetEditProfile_authenticatedUser() throws Exception {
        User user = new User();
        user.setUsername("Ivan");

        EditUserDto editUserDto = new EditUserDto();
        editUserDto.setUsername("Ivan");

        when(userService.findUserByUsername("Ivan")).thenReturn(user);
        when(modelMapper.map(user, EditUserDto.class)).thenReturn(editUserDto);

        mockMvc.perform(get("/users/edit-profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/edit-profile"))
                .andExpect(model().attributeExists("model"))
                .andExpect(model().attribute("model", editUserDto));
    }

    @Test
    public void testGetEditProfile_unauthenticatedUser() throws Exception {
        mockMvc.perform(get("/users/edit-profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    public void testDoEditProfile_validationErrors() throws Exception {
        EditUserDto editUserDto = new EditUserDto();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        doNothing().when(userEditValidator).validate(eq(editUserDto), eq(bindingResult));

        mockMvc.perform(post("/users/edit-profile")
                        .flashAttr("model", editUserDto)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("users/edit-profile"))
                .andExpect(model().attributeExists("model"))
                .andExpect(model().attribute("model", editUserDto));
    }
}
