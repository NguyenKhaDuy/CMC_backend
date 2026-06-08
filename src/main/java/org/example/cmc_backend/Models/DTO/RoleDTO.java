package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {
    private Long idRole;
    private String role;
}
