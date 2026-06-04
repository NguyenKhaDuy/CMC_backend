package org.example.cmc_backend.Models.Response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class MessageResponse {
    private String message;
    private HttpStatus status;
}
