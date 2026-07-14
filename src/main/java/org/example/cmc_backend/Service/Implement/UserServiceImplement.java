package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.RoleEntity;
import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.LoginDTO;
import org.example.cmc_backend.Models.DTO.RoleDTO;
import org.example.cmc_backend.Models.DTO.UserDTO;
import org.example.cmc_backend.Models.Request.*;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.RoleRepository;
import org.example.cmc_backend.Repository.UserRepository;
import org.example.cmc_backend.Service.MailService;
import org.example.cmc_backend.Service.UserService;
import org.example.cmc_backend.Utils.ConvertByteToBase64;
import org.example.cmc_backend.Utils.JwtTokenUtils;
import org.example.cmc_backend.Utils.RandomIdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    ModelMapper modelMapper;


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
            userEntity.setIdUser(RandomIdUtils.generateRandomId("U", 10));
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
            messageResponse.setStatus(HttpStatus.OK);
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

    @Override
    public MessageResponse updateInformation(UpdateUserRequest updateUserRequest) {
        MessageResponse messageResponse = new MessageResponse();
        UserEntity userEntity = null;
        try{
            userEntity = userRepository.findById(updateUserRequest.getIdUser()).get();
            modelMapper.map(updateUserRequest, userEntity);
            userRepository.save(userEntity);
            messageResponse.setMessage("User updated successfully");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("User not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        return messageResponse;
    }

    @Override
    public MessageResponse updateEmailUser(UpdateEmailRequest updateEmailRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            UserEntity userEntity = userRepository.findById(updateEmailRequest.getIdUser()).get();
            userEntity.setEmail(updateEmailRequest.getEmail());
            userRepository.save(userEntity);
            messageResponse.setMessage("User updated successfully");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("User not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        return messageResponse;
    }

    @Override
    public MessageResponse updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            UserEntity userEntity = userRepository.findById(updatePasswordRequest.getIdUser()).get();
            if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), userEntity.getPassword())){
                messageResponse.setMessage("Password incorrect");
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return messageResponse;
            }
            userEntity.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
            userRepository.save(userEntity);
            messageResponse.setMessage("Password updated successfully");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("User not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        return messageResponse;
    }

    @Override
    public MessageResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            UserEntity userEntity = userRepository.findByEmail(forgotPasswordRequest.getEmail());
            userEntity.setPassword(passwordEncoder.encode(forgotPasswordRequest.getNewPassword()));
            userRepository.save(userEntity);
            messageResponse.setMessage("Password forgot successfully");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("User not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        return messageResponse;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            return userEntity;
        }catch (NoSuchElementException ex){
            return null;
        }
    }

    @Override
    public Object getUserById(String idUser) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try{
            UserEntity userEntity = userRepository.findById(idUser).get();
            UserDTO userDTO = new UserDTO();
            modelMapper.map(userEntity, userDTO);
            List<String> roles = new ArrayList<>();
            for (RoleEntity roleEntity : userEntity.getRoleEntities()){
                roles.add(roleEntity.getRole());
            }
            userDTO.setRoles(roles);
            userDTO.setAvatar(ConvertByteToBase64.toBase64(userEntity.getAvatar()));
            dataResponse.setStatus(HttpStatus.OK);
            dataResponse.setMessage("Successfully");
            dataResponse.setData(userDTO);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("User not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public Page<UserDTO> getUsers(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        List<UserDTO> userDTOs = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            UserDTO userDTO = new UserDTO();
            modelMapper.map(userEntity, userDTO);
            userDTO.setAvatar(ConvertByteToBase64.toBase64(userEntity.getAvatar()));
            List<String> role = new ArrayList<>();
            for (RoleEntity roleEntity : userEntity.getRoleEntities()){
                role.add(roleEntity.getRole());
            }
            userDTO.setRoles(role);
            userDTOs.add(userDTO);
        }
        return new PageImpl<>(userDTOs, userEntities.getPageable(), userEntities.getTotalElements());
    }
}
