package org.example.cmc_backend.Models.Response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DataPageResponse <T>{
    private T data;
    private Long total_page;
    private Integer current_page;
    private String message;
    private HttpStatus status;
}
