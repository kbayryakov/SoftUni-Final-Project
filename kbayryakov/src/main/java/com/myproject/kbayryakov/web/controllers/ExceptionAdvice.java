package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.errors.UserAlreadyExistException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionAdvice extends BaseController {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException.class)
    public ModelAndView handleUserAlreadyExists(UserAlreadyExistException e,
                                                RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        return redirect("/users/register");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception, HttpServletResponse response) {
        int statusCode = response.getStatus();

        super.view("error").addObject("errorMessage", exception.getMessage());
        super.view("error").addObject("statusCode", statusCode);

        return super.view("error");
    }
}
