package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    private String idUser;
    private String email;
    private String oldPassword;
    private String newPassword;

}
