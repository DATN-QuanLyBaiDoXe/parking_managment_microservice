package com.tth.vehicle.repository;

import com.tth.vehicle.model.Owner;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, String> {

    Page<Owner> findAllByStatus(Pageable pageable, Integer status);

    Page<Owner> findByFullNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCaseOrAddressContainingIgnoreCase(Pageable pageable, String fullName, String phoneNumber, String address);

    Owner findByPhoneNumberAndStatus(String phoneNumber, Integer status);

    Owner findByUuidAndStatus(String uuid, Integer status);

    List<Owner> findByUuidInAndStatus(List<String> uuids, Integer status);
}
