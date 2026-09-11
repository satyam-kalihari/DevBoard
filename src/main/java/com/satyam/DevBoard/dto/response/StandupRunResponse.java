package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.StandupRun;
//import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class StandupRunResponse {

    private UUID id;
    private UUID standupId;

    private String aiSummary;
    private Boolean isFinalized;

    private LocalDate runDate;
    private LocalDateTime finalizedAt;
//    private List<StandupResponse> responses;

    public static StandupRunResponse fromEntity(StandupRun standupRun){
        return StandupRunResponse.builder()
                .id(standupRun.getId())
                .standupId(standupRun.getStandup().getId())
                .aiSummary(standupRun.getAiSummary())
                .isFinalized(standupRun.getIsFinalized())
                .runDate(standupRun.getRunDate())
                .finalizedAt(standupRun.getFinalizedAt())
//                .responses(standupRun.getResponses() == null ? List.of() :
//                        standupRun.getResponses()
//                                .stream()
//                                .map(StandupResponseResponse::fromEntity)
//                                .toList())
                .build();
    }
}
