package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchRequest {
    private Long idBranch;
    private String address;
    private String nameBranch;
    private String phone;
}
