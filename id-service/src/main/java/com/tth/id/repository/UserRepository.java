package com.tth.id.repository;

import com.tth.id.model.User;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByPhoneNumberAndStatus(String phoneNumber, Integer status);

    User findByEmailAndStatus(String email, Integer status);

    User findByUsernameAndStatus(String username, Integer status);

    Page<User> findAllByStatus(Pageable pageable, Integer status);

    @Query("SELECT u FROM User u WHERE (u.username like %:username% OR u.fullName like %:fullName% OR u.email like %:email%) AND u.status = :status")
    Page<User> findByKeyword(@Param("username") String username, @Param("fullName") String fullName,
                             @Param("email") String email, @Param("status") Integer status, Pageable pageable);

    List<User> findByUuidInAndStatus(List<String> uuids, Integer status);

    Optional<User> findByUuidAndStatus(String uuid, Integer status);
}
