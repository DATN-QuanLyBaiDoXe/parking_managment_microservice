package com.tth.vehicle.service;

import com.tth.vehicle.model.Owner;
import com.tth.vehicle.model.dto.OwnerDTO;
import com.tth.vehicle.model.dto.OwnerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OwnerService {

    Page<OwnerResponse> getAll(Pageable pageable, String search);

    Owner findByPhoneNumber(String phoneNumber);

    OwnerDTO save(OwnerDTO ownerDTO, String uuid);

    OwnerDTO update(Owner owner, OwnerDTO ownerDTO, String uuid);

    Owner findByUuid(String uuid);

    void deleteMultiOwner(List<Owner> ownerList);

    List<Owner> findByUuidIn(List<String> uuids);
}
