package com.tth.vehicle.service.impl;

import com.tth.common.utils.StringUtil;
import com.tth.vehicle.model.Owner;
import com.tth.vehicle.model.Vehicle;
import com.tth.vehicle.model.dto.VehicleDTO;
import com.tth.vehicle.repository.VehicleRepository;
import com.tth.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Page<VehicleDTO> getAll(Pageable pageable, String search) {
        Page<Vehicle> vehiclePage;
        if (StringUtil.isNullOrEmpty(search)) {
            vehiclePage = vehicleRepository.findAllByStatus(pageable, 1);
        } else {
            vehiclePage = vehicleRepository.findByKeyword(search, search, search, 1, pageable);
        }
        return transformToVehicleDTO(vehiclePage);
    }

    @Override
    public Vehicle findByPlace(String place) {
        return vehicleRepository.findByPlaceAndStatus(place, 1);
    }

    @Override
    public VehicleDTO save(VehicleDTO vehicleDTO, Owner owner, String uuid) {
        Vehicle vehicle = transformToVehicle(vehicleDTO);
        vehicle.setCreatedBy(uuid);
        vehicle.setCreatedDate(new Date());
        vehicle.setUuid(UUID.randomUUID().toString());
        vehicle.setStatus(1);
        vehicle.setOwner(owner);
        vehicleRepository.save(vehicle);
        return vehicleDTO;
    }

    @Override
    public Vehicle findById(String uuid) {
        return vehicleRepository.findByUuidAndStatus(uuid, 1);
    }

    @Override
    public VehicleDTO update(Vehicle vehicle, VehicleDTO vehicleDTO, String uuid, Owner owner) {
        vehicle.setUuid(vehicleDTO.getUuid());
        vehicle.setVehicleType(vehicleDTO.getVehicleType());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setPlace(vehicleDTO.getPlace());
        vehicle.setModifiedBy(uuid);
        vehicle.setModifiedDate(new Date());
        vehicle.setOwner(owner);
        vehicleRepository.save(vehicle);
        return vehicleDTO;
    }

    @Override
    public void deleteMultiVehicle(List<Vehicle> vehicleList) {
        for (Vehicle vehicle : vehicleList) {
            vehicle.setStatus(0);
        }
        vehicleRepository.saveAll(vehicleList);
    }

    @Override
    public List<Vehicle> findByUuidIn(List<String> uuids) {
        return vehicleRepository.findByUuidInAndStatus(uuids, 1);
    }

    @Override
    public List<VehicleDTO> findByOwner(String ownerId) {
        List<Vehicle> vehicleList = vehicleRepository.findByOwnerAndStatus(ownerId, 1);
        List<VehicleDTO> vehicleDTOList = vehicleList.stream().map(vehicle -> {
            VehicleDTO vehicleDTO = new VehicleDTO();
            vehicleDTO.setVehicleType(vehicle.getVehicleType());
            vehicleDTO.setBrand(vehicle.getBrand());
            vehicleDTO.setColor(vehicle.getColor());
            vehicleDTO.setPlace(vehicle.getPlace());
            return vehicleDTO;
        }).collect(Collectors.toList());
        return vehicleDTOList;
    }

    private Vehicle transformToVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setUuid(vehicleDTO.getUuid());
        vehicle.setVehicleType(vehicleDTO.getVehicleType());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setPlace(vehicleDTO.getPlace());
        return vehicle;
    }

    private Page<VehicleDTO> transformToVehicleDTO(Page<Vehicle> vehiclePage) {
        Page<VehicleDTO> vehicleDTOPage = null;
        if (vehiclePage.getTotalElements() > 0) {
            vehicleDTOPage = vehiclePage.map(vehicle -> {
                VehicleDTO vehicleDTO = new VehicleDTO();
                vehicleDTO.setUuid(vehicle.getUuid());
                vehicleDTO.setVehicleType(vehicle.getVehicleType());
                vehicleDTO.setBrand(vehicle.getBrand());
                vehicleDTO.setColor(vehicle.getColor());
                vehicleDTO.setPlace(vehicle.getPlace());
                vehicleDTO.setOwnerName(vehicle.getOwner().getFullName());
                vehicleDTO.setOwnerId(vehicle.getOwner().getUuid());
                return vehicleDTO;
            });
        }
        return vehicleDTOPage;
    }
}
