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
public class AiContextResult {
    private String intent;
    private List<String> entities;
    private int totalRecords;
    private String databaseContext;
    private List<String> summaryLines;

    public boolean hasDatabaseData() {
        return totalRecords > 0 && databaseContext != null && !databaseContext.isBlank();
    }

    public AiContextDTO toDto() {
        return new AiContextDTO(intent, entities, totalRecords, hasDatabaseData(), summaryLines);
    }
}
