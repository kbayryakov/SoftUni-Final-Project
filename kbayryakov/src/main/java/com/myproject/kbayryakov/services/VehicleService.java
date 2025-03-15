package com.myproject.kbayryakov.services;

import com.myproject.kbayryakov.models.Vehicle;
import com.myproject.kbayryakov.repositories.VehicleRepository;
import com.myproject.kbayryakov.web.dto.AddVehicleDto;
import com.myproject.kbayryakov.web.dto.EditVehicleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<Vehicle> findAll() {
        return this.vehicleRepository.findAll();
    }

    public Vehicle findById(UUID id) {
        Optional<Vehicle> optionalVehicle = this.vehicleRepository.findById(id);

        if (optionalVehicle.isEmpty()) {
            throw new RuntimeException("Vehicle not found!");
        }

        return optionalVehicle.get();
    }

    public void delete(UUID id) {
        Optional<Vehicle> optionalVehicle = this.vehicleRepository.findById(id);

        if (optionalVehicle.isEmpty()) {
            throw new RuntimeException("Vehicle not found!");
        }

        this.vehicleRepository.delete(optionalVehicle.get());
    }

    public void editVehicle(UUID id, EditVehicleDto editData) {
        Optional<Vehicle> optionalVehicle = this.vehicleRepository.findById(id);

        if (optionalVehicle.isEmpty()) {
            throw new RuntimeException("Vehicle not found!");
        }

        Vehicle vehicle = optionalVehicle.get();

        vehicle.setType(editData.getType());
        vehicle.setModel(editData.getModel());
        vehicle.setBrand(editData.getBrand());
        vehicle.setYear(editData.getYear());
        vehicle.setMileage(editData.getMileage());
        vehicle.setEuroStandard(editData.getEuroStandard());
        vehicle.setTransmission(editData.getTransmission());
        vehicle.setFuelType(editData.getFuelType());
        vehicle.setColor(editData.getColor());

        this.vehicleRepository.save(vehicle);
    }
}
