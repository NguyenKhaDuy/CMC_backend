package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.DTO.BranchDTO;
import org.example.cmc_backend.Models.Request.BranchRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BranchApi {
    @Autowired
    BranchService branchService;

    @GetMapping(value = "/api/admin/branch")
    public ResponseEntity<Object> getAllBrancheAdmin(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<BranchDTO> branchDTOS = branchService.getAllBranches(pageNo);
        DataPageResponse dataPageResponse = new DataPageResponse();
        dataPageResponse.setData(branchDTOS.getContent());
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setStatus(HttpStatus.OK);
        dataPageResponse.setTotal_page(branchDTOS.getTotalPages());
        dataPageResponse.setMessage("Success");
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/branch")
    public ResponseEntity<Object> getAllBranchs() {
        DataResponse result = branchService.getAllBranches();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/branch/id-brach={idBranch}")
    public ResponseEntity<Object> getBranchById(@PathVariable("idBranch") Long idBranch) {
        Object result = branchService.getBranchById(idBranch);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/branch")
    public ResponseEntity<Object> createBranch(@RequestBody BranchRequest branchRequest) {
        MessageResponse result = branchService.addBranch(branchRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/admin/branch")
    public ResponseEntity<Object> updateBranch(@RequestBody BranchRequest branchRequest) {
        MessageResponse result = branchService.updateBranch(branchRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @DeleteMapping(value = "/api/admin/branch/id-branch={idBranch}")
    public ResponseEntity<Object> deleteBranch(@PathVariable Long idBranch) {
        MessageResponse result = branchService.deleteBranch(idBranch);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
