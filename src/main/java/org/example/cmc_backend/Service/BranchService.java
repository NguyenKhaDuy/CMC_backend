package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.BranchDTO;
import org.example.cmc_backend.Models.Request.BranchRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BranchService {
    Page<BranchDTO> getAllBranches(Integer pageNo); //admin
    DataResponse getAllBranches(); //all
    Object getBranchById(Long idBranch); //all
    MessageResponse addBranch(BranchRequest branchRequest); //admin
    MessageResponse updateBranch(BranchRequest branchRequest); //admin
    MessageResponse deleteBranch(Long idBranch); //admin
}
