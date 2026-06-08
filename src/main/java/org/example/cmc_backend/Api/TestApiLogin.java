package org.example.cmc_backend.Api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.cmc_backend.Models.DTO.LoginDTO;
import org.example.cmc_backend.Models.Request.LoginRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApiLogin {
    @Autowired
    UserService userService;

    @PostMapping("/api/login/")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Object result = userService.Login(loginRequest);
        if (result instanceof MessageResponse) {
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        Cookie cookie = new Cookie("token", ((LoginDTO) result).getToken());
        cookie.setHttpOnly(false); // Nếu frontend cần đọc token để set Authorization header
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
