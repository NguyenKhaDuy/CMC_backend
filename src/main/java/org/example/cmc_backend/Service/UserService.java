package org.example.cmc_backend.Service;

import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.UserDTO;
import org.example.cmc_backend.Models.Request.*;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    MessageResponse Register(RegisterRequest registerRequest);
    MessageResponse UpdateAvatar(UpdateAvatarRequest updateAvatarRequest);
    Object Login(LoginRequest loginRequest);
    MessageResponse updateInformation(UpdateUserRequest updateUserRequest);
    MessageResponse updateEmailUser(UpdateEmailRequest updateEmailRequest);
    MessageResponse updatePassword(UpdatePasswordRequest updatePasswordRequest);
    MessageResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    UserEntity getUserByEmail(String email);
    Object getUserById(String idUser);
    Page<UserDTO> getUsers(Integer pageNo);
}
