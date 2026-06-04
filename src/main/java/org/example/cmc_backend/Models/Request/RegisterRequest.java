package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private String phone;
    private LocalDate dob;
    private String fullName;
}
