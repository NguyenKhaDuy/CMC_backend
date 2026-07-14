package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleStatusRequest {
    private Long idSchedule;
    private boolean status;
}
