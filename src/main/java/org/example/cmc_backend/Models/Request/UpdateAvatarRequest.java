package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateAvatarRequest {
    private String id_user;
    private MultipartFile avatar;
}
