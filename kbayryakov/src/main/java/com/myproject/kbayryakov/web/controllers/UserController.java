package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.errors.UserAlreadyExistException;
import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.services.UserService;
import com.myproject.kbayryakov.web.dto.LoginUserDto;
import com.myproject.kbayryakov.web.dto.RegisterUserDto;
import com.myproject.kbayryakov.web.annotations.PageTitle;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

        if (!registerUserData.getPassword().equals(registerUserData.getConfirmPassword())) {
            return redirect("/users/register");
        }

        try {
            this.userService.register(registerUserData);
        } catch (UserAlreadyExistException e) {
            modelAndView = view("users/register");
            modelAndView.addObject("registerUserData", registerUserData);
            modelAndView.addObject("errorMessage", e.getMessage());
            return modelAndView;
            //TODO change when create exception handling
        }

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
                                       @ModelAttribute(name = "model") User model) {
        model = this.userService.findUserByUsername(principal.getName());
        modelAndView.addObject("model", model);
        return super.view("users/edit-profile", modelAndView);
    }

    @PostMapping("/edit-profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Profile")
    public ModelAndView doEditProfile(ModelAndView modelAndView,
                                      @ModelAttribute(name = "model") User model,
                                      BindingResult bindingResult) {
        return null;//TODO
    }
}
