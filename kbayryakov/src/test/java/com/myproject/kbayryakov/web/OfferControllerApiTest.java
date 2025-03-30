package com.myproject.kbayryakov.web;

import com.myproject.kbayryakov.models.Offer;
import com.myproject.kbayryakov.models.Vehicle;
import com.myproject.kbayryakov.services.OfferService;
import com.myproject.kbayryakov.services.VehicleService;
import com.myproject.kbayryakov.web.controllers.OfferController;
import com.myproject.kbayryakov.web.dto.AddOfferDto;
import com.myproject.kbayryakov.web.dto.EditOfferDto;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OfferController.class)
public class OfferControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OfferService offerService;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private ModelMapper modelMapper;

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getOffer_shouldReturnAddOfferPage_whenUserIsAuthenticated() throws Exception {
        Vehicle vehicle = new Vehicle();
        List<Vehicle> vehicles = List.of(vehicle);
        when(vehicleService.findAllByUsername("Ivan")).thenReturn(vehicles);

        mockMvc.perform(get("/offers/add-offer"))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/add-offer"))
                .andExpect(model().attributeExists("vehicles"))
                .andExpect(model().attributeExists("addOfferDto"));
    }

    @Test
    void getOffer_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/offers/add-offer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void doAddOffer_shouldReturnFormWithErrors_whenDataIsInvalid() throws Exception {
        AddOfferDto addOfferDto = new AddOfferDto();

        when(vehicleService.findAllByUsername("Ivan")).thenReturn(List.of(new Vehicle()));

        mockMvc.perform(post("/offers/add-offer")
                        .with(csrf())
                        .flashAttr("addOfferDto", addOfferDto))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/add-offer"))
                .andExpect(model().attributeExists("org.springframework.validation.BindingResult.addOfferDto"));
    }

    @Test
    void doAddOffer_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        AddOfferDto addOfferDto = new AddOfferDto();

        mockMvc.perform(post("/offers/add-offer")
                        .with(csrf())
                        .flashAttr("addOfferDto", addOfferDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getMyOffer_shouldReturnOffers_whenUserIsAuthenticated() throws Exception {
        Offer offer = new Offer();
        Vehicle vehicle = new Vehicle();
        offer.setVehicle(vehicle);
        List<Offer> offers = List.of(offer);
        when(offerService.findAllOffersByUsername("Ivan")).thenReturn(offers);

        mockMvc.perform(get("/offers/my-offer"))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/my-offer"))
                .andExpect(model().attribute("offers", offers));
    }

    @Test
    void getMyOffer_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/offers/my-offer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getAllOffers_shouldReturnOffers_whenUserIsAuthenticated() throws Exception {
        Offer offer = new Offer();
        Vehicle vehicle = new Vehicle();
        offer.setVehicle(vehicle);
        List<Offer> offers = List.of(offer);

        when(offerService.findAll()).thenReturn(offers);

        mockMvc.perform(get("/offers/all-offer"))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/all-offer"))
                .andExpect(model().attribute("offers", offers));
    }

    @Test
    void getAllOffers_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/offers/all-offer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getDeleteOffer_shouldReturnOffer_whenUserIsAuthenticated() throws Exception {
        UUID offerId = UUID.randomUUID();
        Offer offer = new Offer();
        Vehicle vehicle = new Vehicle();
        offer.setVehicle(vehicle);

        when(offerService.findById(offerId)).thenReturn(offer);

        mockMvc.perform(get("/offers/delete-offer/{id}", offerId))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/delete-offer"))
                .andExpect(model().attribute("offer", offer));
    }

    @Test
    void getDeleteOffer_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        UUID offerId = UUID.randomUUID();

        mockMvc.perform(get("/offers/delete-offer/{id}", offerId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void doDeleteOffer_shouldRedirectToMyOffers_whenUserIsAuthenticated() throws Exception {
        UUID offerId = UUID.randomUUID();

        doNothing().when(offerService).delete(offerId);

        mockMvc.perform(post("/offers/delete-offer/{id}", offerId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

    }

    @Test
    void doDeleteOffer_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        UUID offerId = UUID.randomUUID();

        mockMvc.perform(post("/offers/delete-offer/{id}", offerId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getEditOffer_shouldReturnEditOfferView_whenUserIsAuthenticated() throws Exception {
        UUID offerId = UUID.randomUUID();
        Offer offer = new Offer();
        offer.setId(offerId);
        Vehicle vehicle = new Vehicle();
        offer.setVehicle(vehicle);

        when(offerService.findById(offerId)).thenReturn(offer);

        mockMvc.perform(MockMvcRequestBuilders.get("/offers/edit-offer/{id}", offerId))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/edit-offer"))
                .andExpect(model().attributeExists("offer"))
                .andExpect(model().attribute("offer", offer))
                .andExpect(model().attribute("id", offerId));
    }

    @Test
    void getEditOffer_shouldReturnLoginPage_whenUserIsNotAuthenticated() throws Exception {
        UUID offerId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/offers/edit-offer/{id}", offerId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void doEditOffer_shouldReturnEditOfferView_whenBindingErrorsExist() throws Exception {
        UUID offerId = UUID.randomUUID();
        EditOfferDto offerDto = new EditOfferDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/offers/edit-offer/{id}", offerId)
                        .flashAttr("offer", offerDto)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/edit-offer"))
                .andExpect(model().attributeExists("offer"))
                .andExpect(model().attributeExists("org.springframework.validation.BindingResult.offer"))
                .andExpect(model().attribute("id", offerId));
    }

    @Test
    void doEditOffer_shouldReturnLoginPage_whenUserIsNotAuthenticated() throws Exception {
        UUID offerId = UUID.randomUUID();
        EditOfferDto offerDto = new EditOfferDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/offers/edit-offer/{id}", offerId)
                        .flashAttr("offer", offerDto)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    void getOfferDetails_shouldRedirectToLoginPage_whenUserIsNotAuthenticated() throws Exception {
        UUID offerId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/offers/offer-details/{id}", offerId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }
}
