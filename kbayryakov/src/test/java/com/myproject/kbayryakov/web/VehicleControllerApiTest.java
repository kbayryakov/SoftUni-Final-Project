package com.myproject.kbayryakov.web;

import com.myproject.kbayryakov.models.Vehicle;
import com.myproject.kbayryakov.services.VehicleService;
import com.myproject.kbayryakov.web.controllers.VehicleController;
import com.myproject.kbayryakov.web.dto.EditVehicleDto;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
public class VehicleControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private ModelMapper modelMapper;

    @Test
    @WithMockUser(roles = "USER")
    void getRequestToAddVehicle_happyPath() throws Exception {
        MockHttpServletRequestBuilder request = get("/vehicles/add-vehicle");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("vehicles/add-vehicle"))
                .andExpect(model().attributeExists("vehicleDetails"));
    }

    @Test
    void getAddVehicle_shouldRedirect_whenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/vehicles/add-vehicle"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USER")
    void doAddVehicle_shouldReturnFormView_whenValidationFails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/add-vehicle")
                        .with(csrf())
                        .param("vehicleDetails", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicles/add-vehicle"))
                .andExpect(model().attributeExists("vehicleDetails"))
                .andExpect(model().attributeExists("org.springframework.validation.BindingResult.vehicleDetails"));
    }

    @Test
    void doAddVehicle_shouldReturnForbidden_whenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/add-vehicle")
                        .with(csrf())
                        .param("vehicleDetails", ""))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getMyVehicles_shouldReturnMyVehicles_whenUserIsAuthenticated() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("BMW");
        List<Vehicle> mockVehicles = List.of(vehicle);
        when(vehicleService.findAllByUsername("Ivan")).thenReturn(mockVehicles);

        mockMvc.perform(get("/vehicles/my-vehicles"))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicles/my-vehicles"))
                .andExpect(model().attributeExists("vehicles"));
    }

    @Test
    void getMyVehicles_shouldRedirect_whenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/vehicles/my-vehicles"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getAllVehicles_shouldReturnAllVehicles_whenUserIsAuthenticated() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("BMW");
        List<Vehicle> mockVehicles = List.of(vehicle);
        when(vehicleService.findAll()).thenReturn(mockVehicles);

        mockMvc.perform(get("/vehicles/all-vehicles"))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicles/all-vehicles"))
                .andExpect(model().attributeExists("vehicles"));
    }

    @Test
    void getAllVehicles_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/vehicles/all-vehicles"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getVehicleDetails_shouldReturnVehicleDetails_whenUserIsAuthenticated() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("BMW");
        when(vehicleService.findById(vehicleId)).thenReturn(vehicle);

        mockMvc.perform(get("/vehicles/details-vehicle/{id}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicles/details-vehicle"))
                .andExpect(model().attributeExists("vehicle"));
    }

    @Test
    void getVehicleDetails_shouldRedirect_whenUserIsNotAuthenticated() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        mockMvc.perform(get("/vehicles/details-vehicle/{id}", vehicleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getDeleteVehicle_shouldReturnDeleteVehicleView_whenUserIsAuthenticated() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle();
        when(vehicleService.findById(vehicleId)).thenReturn(vehicle);

        mockMvc.perform(get("/vehicles/delete-vehicle/{id}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicles/delete-vehicle"))
                .andExpect(model().attributeExists("vehicle"));
    }

    @Test
    void getDeleteVehicle_shouldRedirect_whenUserIsNotAuthenticated() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        mockMvc.perform(get("/vehicles/delete-vehicle/{id}", vehicleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void doDeleteVehicle_shouldRedirectToMyVehicles_whenUserIsAuthenticated() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        doNothing().when(vehicleService).delete(vehicleId);

        mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/delete-vehicle/{id}", vehicleId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void doDeleteVehicle_shouldRedirect_whenUserIsNotAuthenticated() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/delete-vehicle/{id}", vehicleId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "USER")
    void getEditVehicle_shouldReturnEditVehicleView_whenUserIsAuthenticated() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle();
        EditVehicleDto editData = new EditVehicleDto();
        when(vehicleService.findById(vehicleId)).thenReturn(vehicle);
        when(modelMapper.map(vehicle, EditVehicleDto.class)).thenReturn(editData);

        mockMvc.perform(get("/vehicles/edit-vehicle/{id}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicles/edit-vehicle"))
                .andExpect(model().attributeExists("vehicle"));
    }

    @Test
    void getEditVehicle_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        mockMvc.perform(get("/vehicles/edit-vehicle/{id}", vehicleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void doEditVehicle_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        EditVehicleDto editData = new EditVehicleDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/edit-vehicle/{id}", vehicleId)
                        .with(csrf())
                        .flashAttr("editData", editData))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

}
