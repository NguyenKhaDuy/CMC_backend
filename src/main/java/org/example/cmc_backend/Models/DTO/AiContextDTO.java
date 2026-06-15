package org.example.cmc_backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiContextDTO {
    private String intent;
    private List<String> entities;
    private int totalRecords;
    private boolean hasDatabaseData;
    private List<String> summaryLines;
}
