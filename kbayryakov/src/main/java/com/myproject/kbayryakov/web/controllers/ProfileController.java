package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.services.UserService;
import com.myproject.kbayryakov.web.annotations.PageTitle;
import com.myproject.kbayryakov.web.dto.EditUserDto;
import com.myproject.kbayryakov.web.validation.UserEditValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
public class ProfileController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserEditValidator userEditValidator;

    public ProfileController(UserService userService, ModelMapper modelMapper, UserEditValidator userEditValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userEditValidator = userEditValidator;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Profile")
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
}
