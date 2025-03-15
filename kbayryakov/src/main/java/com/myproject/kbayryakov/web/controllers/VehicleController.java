package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.models.Vehicle;
import com.myproject.kbayryakov.services.VehicleService;
import com.myproject.kbayryakov.web.annotations.PageTitle;
import com.myproject.kbayryakov.web.dto.AddVehicleDto;
import com.myproject.kbayryakov.web.dto.EditVehicleDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/vehicles")
public class VehicleController extends BaseController{
    private final VehicleService vehicleService;
    private final ModelMapper modelMapper;

    @Autowired
    public VehicleController(VehicleService vehicleService, ModelMapper modelMapper) {
        this.vehicleService = vehicleService;
        this.modelMapper = modelMapper;
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

    @GetMapping("/all-vehicles")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("All Vehicles")
    public ModelAndView getAllVehicles(ModelAndView modelAndView) {
        List<Vehicle> allVehicles = this.vehicleService.findAll();
        modelAndView.addObject("vehicles", allVehicles);

        return super.view("vehicles/all-vehicles", modelAndView);
    }

    @GetMapping("/details-vehicle/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Vehicle Details")
    public ModelAndView getVehicleDetails(@PathVariable UUID id, ModelAndView modelAndView) {
        Vehicle vehicle = this.vehicleService.findById(id);
        modelAndView.addObject("vehicle", vehicle);

        return super.view("vehicles/details-vehicle", modelAndView);
    }

    @GetMapping("delete-vehicle/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Delete Vehicle")
    public ModelAndView getDeleteVehicle(@PathVariable UUID id, ModelAndView modelAndView){
        Vehicle vehicle = this.vehicleService.findById(id);
        modelAndView.addObject("vehicle", vehicle);

        return super.view("vehicles/delete-vehicle", modelAndView);
    }

    @PostMapping("delete-vehicle/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Delete Vehicle")
    public ModelAndView doDeleteVehicle(@PathVariable UUID id, ModelAndView modelAndView){
        this.vehicleService.delete(id);

        return super.redirect("/vehicles/my-vehicles");
    }

    @GetMapping("edit-vehicle/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Vehicle")
    public ModelAndView getEditVehicle(@PathVariable UUID id, ModelAndView modelAndView){
        Vehicle vehicle = this.vehicleService.findById(id);
        EditVehicleDto editData = this.modelMapper.map(vehicle, EditVehicleDto.class);
        modelAndView.addObject("vehicle", editData);

        return super.view("vehicles/edit-vehicle", modelAndView);
    }

    @PostMapping("edit-vehicle/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Vehicle")
    public ModelAndView doEditVehicle(@PathVariable UUID id, @Valid EditVehicleDto editData) {
        this.vehicleService.editVehicle(id, editData);

        return super.redirect("/vehicles/details-vehicle/" + id);
    }
}
