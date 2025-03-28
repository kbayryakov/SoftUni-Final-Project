package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.errors.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionAdvice extends BaseController {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ModelAndView handleUserAlreadyExists(UserAlreadyExistException e,
                                                RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        return redirect("/users/register");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("error");

        modelAndView.addObject("errorMessage", exception.getMessage());
        modelAndView.addObject("statusCode", HttpStatus.BAD_REQUEST.value());

        return modelAndView;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("error");

        modelAndView.addObject("errorMessage", exception.getMessage());
        modelAndView.addObject("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return modelAndView;
    }
}
