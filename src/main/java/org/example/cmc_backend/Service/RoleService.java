package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.RoleRequest;
import org.example.cmc_backend.Models.Request.SizeRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    DataResponse getAllRole();
    Object getRoleById(Long idRole);
    MessageResponse addRole(RoleRequest roleRequest);
    MessageResponse deleteRole(Long idRole);
    MessageResponse updateRole(RoleRequest roleRequest);
}
