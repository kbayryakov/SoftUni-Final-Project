package com.myproject.kbayryakov.web;

import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.services.UserService;
import com.myproject.kbayryakov.web.controllers.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getRequestToRegister() throws Exception {
        MockHttpServletRequestBuilder request = get("/users/register");

        mockMvc.perform(request.with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attributeExists("registerUserData"));
    }

    @Test
    void getRequestToLogin() throws Exception {
        MockHttpServletRequestBuilder request = get("/users/login");

        mockMvc.perform(request.with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"))
                .andExpect(model().attributeExists("loginUserData"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void allUsers_shouldReturnUsersList_whenUserIsAdmin() throws Exception {
        User user = new User();
        user.setPassword("Ivan");
        List<User> mockUsers = List.of(user);
        when(userService.findAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("users/all-users"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void allUsers_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void setUser_shouldRedirectToAllUsers_whenUserIsAdmin() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).setUserRole(userId, "user");

        mockMvc.perform(post("/users/set-user/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void setUser_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/users/set-user/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void setModerator_shouldRedirectToAllUsers_whenUserIsAdmin() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).setUserRole(userId, "user");

        mockMvc.perform(post("/users/set-moderator/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void setModerator_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/users/set-moderator/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void setAdmin_shouldRedirectToAllUsers_whenUserIsAdmin() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).setUserRole(userId, "user");

        mockMvc.perform(post("/users/set-admin/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void setAdmin_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/users/set-admin/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
    }


}
