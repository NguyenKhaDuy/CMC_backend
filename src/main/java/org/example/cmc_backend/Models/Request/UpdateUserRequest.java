package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateUserRequest {
    private String idUser;
    private String fullName;
    private String phone;
    private LocalDate dob;
}
