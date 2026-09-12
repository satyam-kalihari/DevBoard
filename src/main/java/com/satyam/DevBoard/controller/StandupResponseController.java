package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.response.StandupResponseResponse;
import com.satyam.DevBoard.model.StandupResponse;
import com.satyam.DevBoard.service.StandupResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/standup-responses")
@RequiredArgsConstructor
public class StandupResponseController {

    private final StandupResponseService standupResponseService;

    @GetMapping("/{id}")
    public ResponseEntity<StandupResponseResponse> getByIdWithDetails(@PathVariable UUID id){
        StandupResponse standupResponse = standupResponseService.getById(id);
        return ResponseEntity.ok((StandupResponseResponse.fromEntity(standupResponse)));
    }

    @GetMapping(params = "userId")
    public ResponseEntity<List<StandupResponseResponse>> getByUserIdWithDetails(@RequestParam UUID userId){
        List<StandupResponseResponse> standupResponseResponses = standupResponseService.getByUserIdWithDetails(userId)
                .stream()
                .map(StandupResponseResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(standupResponseResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStandupResponse(@PathVariable UUID id){
        standupResponseService.deleteStandupResponse(id);
        return ResponseEntity.noContent().build();
    }
}
