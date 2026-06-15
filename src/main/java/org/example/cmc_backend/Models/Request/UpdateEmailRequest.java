package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmailRequest {
    private String idUser;
    private String email;
    private String newEmail;
}
