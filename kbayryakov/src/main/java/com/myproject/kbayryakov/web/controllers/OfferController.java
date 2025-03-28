package com.myproject.kbayryakov.web.controllers;

import com.myproject.kbayryakov.models.Offer;
import com.myproject.kbayryakov.models.Vehicle;
import com.myproject.kbayryakov.services.OfferService;
import com.myproject.kbayryakov.services.VehicleService;
import com.myproject.kbayryakov.web.annotations.PageTitle;
import com.myproject.kbayryakov.web.dto.AddOfferDto;
import com.myproject.kbayryakov.web.dto.EditOfferDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/offers")
public class OfferController extends BaseController{
    private final VehicleService vehicleService;
    private final OfferService offerService;

    @Autowired
    public OfferController(VehicleService vehicleService, OfferService offerService) {
        this.vehicleService = vehicleService;
        this.offerService = offerService;
    }

    @GetMapping("/add-offer")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Offer")
    public ModelAndView getOffer(ModelAndView modelAndView, Principal principal) {
        List<Vehicle> vehicles = this.vehicleService.findAllByUsername(principal.getName());
        modelAndView.addObject("vehicles", vehicles);
        modelAndView.addObject("addOfferDto", new AddOfferDto());
        return super.view("offers/add-offer", modelAndView);
    }

    @PostMapping("/add-offer")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Offer")
    public ModelAndView doAddOffer(Principal principal,
                                   @Valid AddOfferDto addOfferDto,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = super.view("offers/add-offer");
            List<Vehicle> vehicles = this.vehicleService.findAllByUsername(principal.getName());
            modelAndView.addObject("vehicles", vehicles);
            modelAndView.addObject("addOfferDto", addOfferDto);
            modelAndView.addObject("org.springframework.validation.BindingResult.addOfferDto", bindingResult);
            return modelAndView;
        }

        String username = principal.getName();

        this.offerService.create(addOfferDto, username);
        return super.redirect("/offers/my-offer");
    }

    @GetMapping("/my-offer")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("My Offers")
    public ModelAndView getMyOffer(ModelAndView modelAndView, Principal principal) {
        List<Offer> offers = offerService.findAllOffersByUsername(principal.getName());
        modelAndView.addObject("offers", offers);

        return view("offers/my-offer", modelAndView);
    }

    @GetMapping("/all-offer")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("All Offers")
    public ModelAndView getAllOffers(ModelAndView modelAndView) {
        List<Offer> offers = offerService.findAll();
        modelAndView.addObject("offers", offers);

        return super.view("offers/all-offer", modelAndView);
    }

    @GetMapping("/delete-offer/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Delete Offer")
    public ModelAndView getDeleteOffer(@PathVariable UUID id,
                                       ModelAndView modelAndView) {
        Offer offer = this.offerService.findById(id);

        modelAndView.addObject("offer", offer);

        return super.view("offers/delete-offer", modelAndView);
    }

    @PostMapping("/delete-offer/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Delete Offer")
    public ModelAndView doDeleteOffer(@PathVariable UUID id) {
        this.offerService.delete(id);

        return super.redirect("/offers/my-offer");
    }

    @GetMapping("/edit-offer/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Offer")
    public ModelAndView getEditOffer(@PathVariable UUID id,
                                     ModelAndView modelAndView) {
        Offer offer = this.offerService.findById(id);
        modelAndView.addObject("offer", offer);
        modelAndView.addObject("id", id);

        return super.view("offers/edit-offer", modelAndView);
    }

    @PostMapping("/edit-offer/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Offer")
    public ModelAndView doEditOffer(@PathVariable UUID id,
                                    @Valid EditOfferDto offer,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = super.view("offers/edit-offer");
            modelAndView.addObject("offer", offer);
            modelAndView.addObject("org.springframework.validation.BindingResult.offer", bindingResult);
            modelAndView.addObject("id", id);
            return modelAndView;
        }

        this.offerService.edit(id, offer);
        return super.redirect("/offers/my-offer");
    }

    @GetMapping("/offer-details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Offer Details")
    public ModelAndView getOfferDetails(@PathVariable UUID id, ModelAndView modelAndView) {
        Offer offer = this.offerService.findById(id);
        modelAndView.addObject("offer", offer);

        return super.view("offers/offer-details", modelAndView);
    }
}
