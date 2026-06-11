package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.Request.RoleRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleApi {
    @Autowired
    RoleService roleService;

    @GetMapping(value = "/api/admin/role")
    public ResponseEntity<Object> getAllRoles() {
        DataResponse dataResponse = roleService.getAllRole();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/role/id-role={idRole}")
    public ResponseEntity<Object> getRoleById(@PathVariable Long idRole) {
        Object result = roleService.getRoleById(idRole);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/role")
    public ResponseEntity<Object> addRole(@RequestBody RoleRequest roleRequest) {
        MessageResponse result = roleService.addRole(roleRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/api/admin/role")
    public ResponseEntity<Object> updateRole(@RequestBody RoleRequest roleRequest) {
        MessageResponse result = roleService.updateRole(roleRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/role/id-role={idRole}")
    public ResponseEntity<Object> deleteRole(@PathVariable Long idRole) {
        MessageResponse result = roleService.deleteRole(idRole);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
