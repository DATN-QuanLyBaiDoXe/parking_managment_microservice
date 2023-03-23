package com.tth.id.service;

import com.tth.id.model.User;
import com.tth.id.model.dto.UserDTO;
import com.tth.id.model.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;


public interface UserService {

    Page<UserResponse> getAll(Pageable pageable, String search);

    UserResponse findById(String id);

    UserResponse save(UserDTO user, String uuid) throws ParseException;

    UserResponse update(String id, User user, UserDTO userDTO, String uuid) throws ParseException;

    void deleteMultiUser(List<User> userList);
    
    User findByUuid(String id);

    User findByUsername(String username);

    User findByPhoneNumber(String phoneNumber);

    User findByEmail(String email);

    UserResponse changePassword(User user, String newPassword);

    List<User> findByUuidIn(List<String> uuids);
}
