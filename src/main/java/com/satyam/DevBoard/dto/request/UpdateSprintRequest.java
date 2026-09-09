package com.satyam.DevBoard.dto.request;

import com.satyam.DevBoard.model.Sprint;
import com.satyam.DevBoard.model.Task;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateSprintRequest {
    private String name;
    private String goal;
    private LocalDate endDate;
    private Sprint.Status status;
}
