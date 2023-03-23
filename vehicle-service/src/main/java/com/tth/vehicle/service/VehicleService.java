package com.tth.vehicle.service;

import com.tth.vehicle.model.Owner;
import com.tth.vehicle.model.Vehicle;
import com.tth.vehicle.model.dto.VehicleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService {

    Page<VehicleDTO> getAll(Pageable pageable, String search);

    Vehicle findByPlace(String place);

    VehicleDTO save(VehicleDTO vehicleDTO, Owner owner, String uuid);

    Vehicle findById(String uuid);

    VehicleDTO update(Vehicle vehicle, VehicleDTO vehicleDTO, String uuid, Owner owner);

    void deleteMultiVehicle(List<Vehicle> vehicleList);

    List<Vehicle> findByUuidIn(List<String> uuids);

    List<VehicleDTO> findByOwner(String ownerId);
}
