package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.StandupResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class StandupResponseResponse {

    private UUID id;
    private UUID standupRunId;
    private UUID userId;

//    private String standupName;
    private String userName;
    private String answerYesterday;
    private String answerToday;
    private String answerBlockers;

    private boolean hasBlockers = false;
    private LocalDateTime submittedAt;

    public static StandupResponseResponse fromEntity(StandupResponse su){
        return StandupResponseResponse.builder()
                .id(su.getId())
                .standupRunId(su.getStandupRun().getId())
                .userId(su.getUser().getId())
//                .standupName(su.getStandupRun().getStandup().getProject().getName())
                .userName(su.getUser().getName())
                .answerYesterday(su.getAnswerYesterday())
                .answerToday(su.getAnswerToday())
                .answerBlockers(su.getAnswerBlockers())
                .hasBlockers(su.isHasBlockers())
                .submittedAt(su.getSubmittedAt())
                .build();
    }
}
