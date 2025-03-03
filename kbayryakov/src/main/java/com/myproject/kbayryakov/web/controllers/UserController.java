package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.errors.UserAlreadyExistException;
import com.myproject.kbayryakov.services.UserService;
import com.myproject.kbayryakov.services.dto.LoginUserDto;
import com.myproject.kbayryakov.services.dto.RegisterUserDto;
import com.myproject.kbayryakov.web.annotations.PageTitle;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    @PageTitle("Register")
    public ModelAndView getRegister() {
        ModelAndView modelAndView = super.view("users/register");
        modelAndView.addObject("registerUserData", new RegisterUserDto());
        return modelAndView;
    }

    @PostMapping("/register")
    @PageTitle("Register")
    public ModelAndView doRegister(
            @Valid RegisterUserDto registerUserData,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = view("users/register");
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
            ModelAndView modelAndView = view("users/register");
            modelAndView.addObject("registerUserData", registerUserData);
            modelAndView.addObject("errorMessage", e.getMessage());
            return modelAndView;
        }

        return redirect("/users/login");
    }

    @GetMapping("/login")
    @PageTitle("Profile")
    public ModelAndView login () {
        ModelAndView modelAndView = super.view("users/login");
        modelAndView.addObject("loginUserData", new LoginUserDto());
        return modelAndView;
    }
}
