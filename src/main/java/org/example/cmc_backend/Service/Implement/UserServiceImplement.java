package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.RoleEntity;
import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.LoginDTO;
import org.example.cmc_backend.Models.Request.LoginRequest;
import org.example.cmc_backend.Models.Request.RegisterRequest;
import org.example.cmc_backend.Models.Request.UpdateAvatarRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.RoleRepository;
import org.example.cmc_backend.Repository.UserRepository;
import org.example.cmc_backend.Service.UserService;
import org.example.cmc_backend.Utils.ConvertByteToBase64;
import org.example.cmc_backend.Utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImplement implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

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

            RoleEntity roleEntity = roleRepository.findByRole("CUSTOMER");
            userEntity.getRoleEntities().add(roleEntity);

            UserEntity user = userRepository.save(userEntity);

            roleEntity.getUserEntities().add(user);
            roleRepository.save(roleEntity);

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
    public Object Login(LoginRequest loginRequest) {
        MessageResponse messageResponse = new MessageResponse();
        LoginDTO loginDTO = new LoginDTO();
        try{
            UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail());
            if (userEntity != null){
                if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())){
                    messageResponse.setMessage("Password incorrect");
                    messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                    return messageResponse;
                }
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword(), userEntity.getAuthorities());
            authenticationManager.authenticate(authenticationToken);
            String token = jwtTokenUtils.generateToken(userEntity);
            loginDTO.setMessage("Login success");
            loginDTO.setToken(token);
            loginDTO.setId_user(userEntity.getIdUser());
            loginDTO.setFull_name(userEntity.getFullName());
            loginDTO.setAvatarBase64(ConvertByteToBase64.toBase64(userEntity.getAvatar()));
            for (RoleEntity roleEntity : userEntity.getRoleEntities()){
                loginDTO.getRoles().add(roleEntity.getRole());
            }
            loginDTO.setHttpStatus(HttpStatus.OK);
            return loginDTO;
        }catch (NullPointerException ex){
            messageResponse.setMessage("Can not found email");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return messageResponse;
        }
    }
}
