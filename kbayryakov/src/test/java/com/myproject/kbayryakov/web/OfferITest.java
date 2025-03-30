package com.myproject.kbayryakov.web;

import com.myproject.kbayryakov.models.Offer;
import com.myproject.kbayryakov.models.User;
import com.myproject.kbayryakov.models.Vehicle;
import com.myproject.kbayryakov.repositories.UserRepository;
import com.myproject.kbayryakov.repositories.VehicleRepository;
import com.myproject.kbayryakov.services.OfferService;
import com.myproject.kbayryakov.services.UserService;
import com.myproject.kbayryakov.services.VehicleService;
import com.myproject.kbayryakov.web.dto.AddOfferDto;
import com.myproject.kbayryakov.web.dto.AddVehicleDto;
import com.myproject.kbayryakov.web.dto.RegisterUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OfferITest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private OfferService offerService;

    @Test
    void createOffer_happyPath() throws IOException {
        RegisterUserDto userData = new RegisterUserDto();
        userData.setUsername("ivan123");
        userData.setPassword("ivan123");
        userData.setConfirmPassword("ivan123");
        userData.setEmail("ivan@ivan");

        this.userService.register(userData);
        User user = this.userRepository.findByEmail("ivan@ivan").get();

        AddVehicleDto vehicleData = new AddVehicleDto();
        vehicleData.setBrand("BMW");
        vehicleData.setColor("Blue");
        vehicleData.setEuroStandard("EURO5");
        vehicleData.setMileage(2222);
        vehicleData.setModel("1 series");
        vehicleData.setFuelType("Hybrid");
        vehicleData.setState("New");
        vehicleData.setTransmission("Automatic");
        vehicleData.setYear(3333);
        vehicleData.setType("Car");
        this.vehicleService.create(vehicleData, user.getUsername());
        Vehicle vehicle = this.vehicleRepository.findAllByUser_Username("ivan123").getFirst();

        AddOfferDto addOfferDto = new AddOfferDto();
        addOfferDto.setCreatedOn(LocalDate.now());
        addOfferDto.setDescription("description");
        addOfferDto.setPrice(new BigDecimal(4));
        addOfferDto.setVehicle(vehicle);
        ClassPathResource imgFile = new ClassPathResource("static/img.jpeg");
        addOfferDto.setImageUrl(new MockMultipartFile(
                "file", "test-image.jpg",
                "image/jpeg", imgFile.getContentAsByteArray()));
        addOfferDto.setValidUntil(LocalDate.now());

        this.offerService.create(addOfferDto, user.getUsername());

        assertTrue(getUserAddedOffer());
    }

    private boolean getUserAddedOffer(){
        List<Offer> offers = this.offerService.findAllOffersByUsername("ivan123");
        return offers.getFirst().getDescription().equals("description");
    }
}
