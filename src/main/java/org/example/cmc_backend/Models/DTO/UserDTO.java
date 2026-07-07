package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String idUser;
    private String email;
    private String fullName;
    private String avatar;
    private String phone;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private List<String> roles;

}
