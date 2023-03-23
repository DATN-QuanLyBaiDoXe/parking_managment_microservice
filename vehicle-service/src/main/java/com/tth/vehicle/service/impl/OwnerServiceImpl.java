package com.tth.vehicle.service.impl;

import com.tth.common.utils.StringUtil;
import com.tth.vehicle.model.Owner;
import com.tth.vehicle.model.dto.OwnerDTO;
import com.tth.vehicle.model.dto.OwnerResponse;
import com.tth.vehicle.repository.OwnerRepository;
import com.tth.vehicle.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public Page<OwnerResponse> getAll(Pageable pageable, String search) {
        Page<Owner> ownerPage;
        if(StringUtil.isNullOrEmpty(search)){
            ownerPage = ownerRepository.findAllByStatus(pageable, 1);
        } else {
            ownerPage = ownerRepository.findByFullNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCaseOrAddressContainingIgnoreCase(pageable, search, search, search);
        }
        return transform(ownerPage);
    }

    @Override
    public Owner findByPhoneNumber(String phoneNumber) {
        return ownerRepository.findByPhoneNumberAndStatus(phoneNumber, 1);
    }

    @Override
    public OwnerDTO save(OwnerDTO ownerDTO, String uuid) {
        Owner owner = transformFromOwnerDTO(ownerDTO);
        owner.setCreatedBy(uuid);
        owner.setCreatedDate(new Date());
        owner.setStatus(1);
        owner.setUuid(UUID.randomUUID().toString());
        ownerRepository.save(owner);
        return ownerDTO;
    }

    @Override
    public OwnerDTO update(Owner owner, OwnerDTO ownerDTO, String uuid) {
        owner.setFullName(ownerDTO.getFullName());
        owner.setAddress(ownerDTO.getAddress());
        owner.setPhoneNumber(ownerDTO.getPhoneNumber());
        owner.setModifiedBy(uuid);
        owner.setModifiedDate(new Date());
        ownerRepository.save(owner);
        return ownerDTO;
    }

    @Override
    public Owner findByUuid(String uuid) {
        return ownerRepository.findByUuidAndStatus(uuid, 1);
    }

    @Override
    public void deleteMultiOwner(List<Owner> ownerList) {
        for (Owner owner : ownerList){
            owner.setStatus(0);
        }
        ownerRepository.saveAll(ownerList);
    }

    @Override
    public List<Owner> findByUuidIn(List<String> uuids) {
        return ownerRepository.findByUuidInAndStatus(uuids, 1);
    }

    private Owner transformFromOwnerDTO(OwnerDTO ownerDTO) {
        Owner owner = new Owner();
        owner.setAddress(ownerDTO.getAddress());
        owner.setFullName(ownerDTO.getFullName());
        owner.setPhoneNumber(ownerDTO.getPhoneNumber());
        return owner;
    }

    private Page<OwnerResponse> transform(Page<Owner> ownerPage) {
        Page<OwnerResponse> ownerResponsePage = null;
        if(ownerPage.getTotalElements() > 0){
            ownerResponsePage = ownerPage.map(owner -> {
                OwnerResponse ownerResponse = new OwnerResponse();
                ownerResponse.setUuid(owner.getUuid());
                ownerResponse.setAddress(owner.getAddress());
                ownerResponse.setFullName(owner.getFullName());
                ownerResponse.setPhoneNumber(owner.getPhoneNumber());
                return ownerResponse;
            });
        }
        return ownerResponsePage;
    }
}
