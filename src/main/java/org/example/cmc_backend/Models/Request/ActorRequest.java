package org.example.cmc_backend.Models.Request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ActorRequest {
    private Long idActor;
    private MultipartFile image;
    private String name;
    private String description;
}
