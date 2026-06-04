package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.Request.LoginRequest;
import org.example.cmc_backend.Models.Request.RegisterRequest;
import org.example.cmc_backend.Models.Request.UpdateAvatarRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.UserRepository;
import org.example.cmc_backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImplement implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public MessageResponse Register(RegisterRequest registerRequest) {
        MessageResponse messageResponse = new MessageResponse();
        UserEntity userEntity =  null;
        try {
            userEntity = userRepository.findByEmail(registerRequest.getEmail());
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("User not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setEmail(registerRequest.getEmail());
            userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            userEntity.setDob(registerRequest.getDob());
            userEntity.setPhone(registerRequest.getPhone());
            userEntity.setFullName(registerRequest.getFullName());
            userEntity.setAvatar(null);
            userRepository.save(userEntity);
            messageResponse.setMessage("User registered successfully");
            messageResponse.setStatus(HttpStatus.CREATED);
        }else {
            messageResponse.setMessage("User already exists");
            messageResponse.setStatus(HttpStatus.CONFLICT);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse UpdateAvatar(UpdateAvatarRequest updateAvatarRequest) {
        MessageResponse messageResponse = new MessageResponse();
        UserEntity userEntity =  null;
        try {
            userEntity = userRepository.findById(updateAvatarRequest.getId_user()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("User not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        if (userEntity == null) {
            messageResponse.setMessage("User not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }else {
            try {
                userEntity.setAvatar(updateAvatarRequest.getAvatar().getBytes());
                userRepository.save(userEntity);
                messageResponse.setMessage("User updated successfully");
                messageResponse.setStatus(HttpStatus.OK);
            }catch (Exception ex){
                messageResponse.setMessage("Avatar update failed");
                messageResponse.setStatus(HttpStatus.CONFLICT);
            }
        }
        return messageResponse;
    }

    @Override
    public MessageResponse Login(LoginRequest loginRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail());
            if (userEntity.getPassword().equals(passwordEncoder.encode(loginRequest.getPassword()))) {
                messageResponse.setMessage("User successfully logged in");
                messageResponse.setStatus(HttpStatus.OK);
            }else {
                messageResponse.setMessage("Wrong password");
                messageResponse.setStatus(HttpStatus.CONFLICT);
            }
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("User not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        return messageResponse;
    }
}
