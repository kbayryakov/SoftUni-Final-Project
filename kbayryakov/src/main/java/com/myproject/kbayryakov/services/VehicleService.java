package com.myproject.kbayryakov.services;

import com.myproject.kbayryakov.models.Vehicle;
import com.myproject.kbayryakov.repositories.VehicleRepository;
import com.myproject.kbayryakov.web.dto.AddVehicleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final ModelMapper modelMapper;
    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    @Autowired
    public VehicleService(ModelMapper modelMapper, VehicleRepository vehicleRepository, UserService userService) {
        this.modelMapper = modelMapper;
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
    }

    public void create (AddVehicleDto vehicleDetails, String username) {
        Vehicle vehicle = this.modelMapper.map(vehicleDetails, Vehicle.class);
        vehicle.setUser(this.userService.findUserByUsername(username));

        this.vehicleRepository.save(vehicle);
    }

    public List<Vehicle> findAllByUsername(String username) {
        return this.vehicleRepository.findAllByUser_Username(username);
    }
}
