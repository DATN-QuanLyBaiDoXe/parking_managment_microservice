package com.tth.vehicle.repository;

import com.tth.vehicle.model.Vehicle;
import com.tth.vehicle.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    
    Page<Vehicle> findAllByStatus(Pageable pageable, Integer status);

    @Query("SELECT v FROM Vehicle v WHERE (v.place like %:plate% OR v.color like %:color% OR v.brand like %:brand%) AND v.status = :status")
    Page<Vehicle> findByKeyword(@Param("plate") String plate, @Param("color") String color,
                             @Param("brand") String brand, @Param("status") Integer status, Pageable pageable);

    Vehicle findByPlaceAndStatus(String place, Integer status);

    Vehicle findByUuidAndStatus(String uuid, Integer status);

    List<Vehicle> findByUuidInAndStatus(List<String> uuids, Integer status);

    @Query("SELECT v FROM Vehicle v WHERE v.owner.uuid = :ownerId AND v.status = :status")
    List<Vehicle> findByOwnerAndStatus(@Param("ownerId") String ownerId, @Param("status") Integer status);
}
