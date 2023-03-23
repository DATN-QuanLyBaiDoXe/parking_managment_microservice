package com.tth.id.service.impl;

import com.tth.common.utils.StringUtil;
import com.tth.id.model.User;
import com.tth.id.model.dto.UserDTO;
import com.tth.id.model.dto.UserResponse;
import com.tth.id.repository.UserRepository;
import com.tth.id.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<UserResponse> getAll(Pageable pageable, String search) {
        Page<User> userList;
        if(StringUtil.isNullOrEmpty(search)){
            userList = userRepository.findAllByStatus(pageable, 1);
        } else {
            userList = userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndStatus(search, search, search, pageable, 1);
        }
        return transform(userList);
    }

    @Override
    public UserResponse findById(String id) {
        return null;
    }

    private Page<UserResponse> transform(Page<User> userList) {
        Page<UserResponse> userResponsePage = null;
        if(userList.getTotalElements() > 0) {
            userResponsePage = userList.map(user -> {
                UserResponse userResponse = new UserResponse();
                userResponse.setUuid(user.getUuid());
                userResponse.setAddress(user.getAddress());
                userResponse.setAvatar(user.getAvatar());
                userResponse.setBirthday(user.getBirthday());
                userResponse.setEmail(user.getEmail());
                userResponse.setUsername(user.getUsername());
                userResponse.setGender(user.getGender());
                userResponse.setFullName(user.getFullName());
                userResponse.setPhoneNumber(user.getPhoneNumber());
                userResponse.setRole(user.getRole());
                return userResponse;
            });
        }
        return userResponsePage;
    }

    @Override
    public UserResponse save(UserDTO userDTO, String uuid) throws ParseException {
        User user = transform(userDTO);
        user.setUuid(UUID.randomUUID().toString());
        user.setCreatedDate(new Date());
        user.setCreatedBy(uuid);
        user.setStatus(1);
        User newUser = userRepository.save(user);
        return transformUserResponse(newUser);
    }

    private UserResponse transformUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setFullName(user.getFullName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setEmail(user.getEmail());
        userResponse.setGender(user.getGender());
        userResponse.setBirthday(user.getBirthday());
        userResponse.setAddress(user.getAddress());
        userResponse.setAvatar(user.getAvatar());
        userResponse.setRole(user.getRole());
        return userResponse;
    }

    private User transform(UserDTO userDTO) throws ParseException {
        User user = new User();
        if(!StringUtil.isNullOrEmpty(userDTO.getUsername())){
            user.setUsername(userDTO.getUsername());
        }
        if(!StringUtil.isNullOrEmpty(userDTO.getPassword())){
            user.setPassword(encoder.encode(userDTO.getPassword()));
        }
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        user.setBirthday(format.parse(userDTO.getBirthday()));
        user.setAddress(userDTO.getAddress());
        user.setAvatar(userDTO.getAvatar());
        user.setRole(userDTO.getRole());
        return user;
    }

    @Override
    public UserResponse update(String id, User user, UserDTO userDTO, String uuid) throws ParseException {
        user.setModifiedBy(uuid);
        user.setModifiedDate(new Date());
        if(!StringUtil.isNullOrEmpty(userDTO.getPassword())){
            user.setPassword(encoder.encode(userDTO.getPassword()));
        }
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(userDTO.getRole());
        user.setAvatar(userDTO.getAvatar());
        user.setAddress(userDTO.getAddress());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        user.setBirthday(format.parse(userDTO.getBirthday()));
        user.setGender(userDTO.getGender());
        User newUser = userRepository.save(user);
        return transformUserResponse(newUser);
    }

    @Override
    public void deleteMultiUser(List<User> userList) {
        for (User user : userList) {
            user.setStatus(0);
        }
        userRepository.saveAll(userList);
    }

    @Override
    public User findByUuid(String id) {
        Optional<User> user = userRepository.findByUuidAndStatus(id, 1);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsernameAndStatus(username, 1);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumberAndStatus(phoneNumber, 1);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, 1);
    }

    @Override
    public UserResponse changePassword(User user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        User newUser = userRepository.save(user);
        return transformUserResponse(newUser);
    }

    @Override
    public List<User> findByUuidIn(List<String> uuids) {
        return userRepository.findByUuidInAndStatus(uuids, 1);
    }
}
