package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.LoginRequest;
import org.example.cmc_backend.Models.Request.RegisterRequest;
import org.example.cmc_backend.Models.Request.UpdateAvatarRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public MessageResponse Register(RegisterRequest registerRequest);
    public MessageResponse UpdateAvatar(UpdateAvatarRequest updateAvatarRequest);
    public MessageResponse Login(LoginRequest loginRequest);
}
