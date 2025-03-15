package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.models.Vehicle;
import com.myproject.kbayryakov.services.VehicleService;
import com.myproject.kbayryakov.web.annotations.PageTitle;
import com.myproject.kbayryakov.web.dto.AddVehicleDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/vehicles")
public class VehicleController extends BaseController{
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/add-vehicle")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Vehicle")
    public ModelAndView getAddVehicle() {
        return super.view("vehicles/add-vehicle");
    }

    @PostMapping("/add-vehicle")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Vehicle")
    public ModelAndView doAddVehicle(@ModelAttribute(name = "vehicleDetails") @Valid AddVehicleDto vehicleDetails,
                                     Principal principal) {
        String username = principal.getName();
        this.vehicleService.create(vehicleDetails, username);

        return super.redirect("/vehicles/my-vehicles");
    }

    @GetMapping("/my-vehicles")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("My Vehicles")
    public ModelAndView getMyVehicles(ModelAndView modelAndView, Principal principal) {
        List<Vehicle> myVehicles = this.vehicleService.findAllByUsername(principal.getName());
        modelAndView.addObject("vehicles", myVehicles);

        return super.view("vehicles/my-vehicles", modelAndView);
    }
}
