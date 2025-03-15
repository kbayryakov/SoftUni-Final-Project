package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.errors.UserAlreadyExistException;
import com.myproject.kbayryakov.models.Role;
import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.services.UserService;
import com.myproject.kbayryakov.web.dto.EditUserDto;
import com.myproject.kbayryakov.web.dto.LoginUserDto;
import com.myproject.kbayryakov.web.dto.RegisterUserDto;
import com.myproject.kbayryakov.web.annotations.PageTitle;
import com.myproject.kbayryakov.web.validation.UserEditValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserEditValidator userEditValidator;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, UserEditValidator userEditValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userEditValidator = userEditValidator;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public ModelAndView getRegister(ModelAndView modelAndView) {
        modelAndView = super.view("users/register");
        modelAndView.addObject("registerUserData", new RegisterUserDto());
        return modelAndView;
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public ModelAndView doRegister(
            @Valid RegisterUserDto registerUserData,
            BindingResult bindingResult,
            ModelAndView modelAndView
    ) {
        if (bindingResult.hasErrors()) {
            modelAndView = super.view("users/register");
            modelAndView.addObject("registerUserData", registerUserData);
            modelAndView.addObject("org.springframework.validation.BindingResult.registerUserData", bindingResult);
            return modelAndView;
        }

        this.userService.register(registerUserData);

        return redirect("/users/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Profile")
    public ModelAndView login () {
        ModelAndView modelAndView = super.view("users/login");
        modelAndView.addObject("loginUserData", new LoginUserDto());
        return modelAndView;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle
    public ModelAndView viewProfile (ModelAndView modelAndView, Principal principal) {
        User user = this.userService.findUserByUsername(principal.getName());
        modelAndView.addObject("model", user);
        return super.view("users/profile", modelAndView);
    }

    @GetMapping("/edit-profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Profile")
    public ModelAndView getEditProfile(ModelAndView modelAndView, Principal principal,
                                       @ModelAttribute(name = "model") EditUserDto model) {
        User user = this.userService.findUserByUsername(principal.getName());
        model = this.modelMapper.map(user, EditUserDto.class);
        modelAndView.addObject("model", model);
        return super.view("users/edit-profile", modelAndView);
    }

    @PostMapping("/edit-profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Profile")
    public ModelAndView doEditProfile(ModelAndView modelAndView,
                                      @ModelAttribute(name = "model") @Valid EditUserDto model,
                                      BindingResult bindingResult) {
        this.userEditValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);

            return super.view("users/edit-profile", modelAndView);
        }

        this.userService.editUser(model);

        return super.redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Users")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<User> users = this.userService.findAllUsers();

        modelAndView.addObject("users", users);

        return super.view("users/all-users", modelAndView);
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUser(@PathVariable UUID id) {
        this.userService.setUserRole(id, "user");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setModerator(@PathVariable UUID id) {
        this.userService.setUserRole(id, "moderator");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdmin(@PathVariable UUID id) {
        this.userService.setUserRole(id, "admin");

        return super.redirect("/users/all");
    }
}
