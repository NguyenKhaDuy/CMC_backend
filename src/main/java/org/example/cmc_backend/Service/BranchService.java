package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.BranchRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface BranchService {
    DataPageResponse getAllBranches(Integer pageNo);
    DataResponse getAllBranches();
    Object getBranchById(Long idBranch);
    MessageResponse addBranch(BranchRequest branchRequest);
    MessageResponse updateBranch(BranchRequest branchRequest);
    MessageResponse deleteBranch(Long idBranch);
}
