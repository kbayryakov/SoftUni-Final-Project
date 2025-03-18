package com.myproject.kbayryakov.services;

import com.myproject.kbayryakov.models.Offer;
import com.myproject.kbayryakov.models.Vehicle;
import com.myproject.kbayryakov.repositories.OfferRepository;
import com.myproject.kbayryakov.web.dto.AddOfferDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferService {
    private final ModelMapper modelMapper;
    private final VehicleService vehicleService;
    private final UserService userService;
    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(ModelMapper modelMapper, VehicleService vehicleService, UserService userService, OfferRepository offerRepository) {
        this.modelMapper = modelMapper;
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.offerRepository = offerRepository;
    }

    public void create(AddOfferDto addOfferDto, String username) {
        Offer offer = this.modelMapper.map(addOfferDto, Offer.class);
        Vehicle vehicle = this.vehicleService.findById(addOfferDto.getVehicle().getId());

        if (offer.getVehicle().getOffer() != null) {
            throw new RuntimeException("Offer with this vehicle already exists");
        }
        vehicle.setUser(this.userService.findUserByUsername(username));

        offer.setUser(this.userService.findUserByUsername(username));
        offer.setVehicle(vehicle);

        this.offerRepository.save(offer);
    }

    public List<Offer> findAllOffersByUsername(String username) {
        return this.offerRepository.findAllByUser_Username(username);
    }

    public List<Offer> findAll() {
        return this.offerRepository.findAll();
    }

    public Offer findById(UUID id){
        Optional<Offer> optionalOffer = this.offerRepository.findById(id);

        if (optionalOffer.isEmpty()) {
            throw new RuntimeException("Offer not found");
        }

        return optionalOffer.get();
    }

    public void delete(UUID id){
        Offer offer = this.offerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Offer with the given id was not found!"));

        offer.getVehicle().setOffer(null);
        offer.setVehicle(null);
        offer.setUser(null);

        this.offerRepository.delete(offer);
    }
}
