package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.web.annotations.PageTitle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    @PageTitle("Index")
    public ModelAndView getIndex () {
        return super.view("home/index");
    }

    @GetMapping("/home")
    @PageTitle("Home")
    public ModelAndView getHome () {
        return super.view("home/home");
    }
}
