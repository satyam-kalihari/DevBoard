package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateStandupResponseRequest;
import com.satyam.DevBoard.dto.request.CreateStandupRunRequest;
import com.satyam.DevBoard.dto.request.UpdateStandupRunRequest;
import com.satyam.DevBoard.dto.response.StandupResponseResponse;
import com.satyam.DevBoard.dto.response.StandupRunResponse;
import com.satyam.DevBoard.model.StandupResponse;
import com.satyam.DevBoard.model.StandupRun;
import com.satyam.DevBoard.service.StandupResponseService;
import com.satyam.DevBoard.service.StandupRunService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/standupruns")
@RequiredArgsConstructor
public class StandupRunController {

    private final StandupRunService standupRunService;
    private final StandupResponseService standupResponseService;

    @PostMapping
    public ResponseEntity<StandupRunResponse> createStandupRun(
            @Valid @RequestBody CreateStandupRunRequest request
    ){

        StandupRun standupRun = standupRunService.createStandupRun(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(StandupRunResponse.fromEntity(standupRun));
    }

    @PostMapping("/{standupRunId}/responses")
    public ResponseEntity<StandupResponseResponse> createStandupResponse(@Valid @RequestBody CreateStandupResponseRequest request){

        StandupResponse standupResponse = standupResponseService.createStandupResponse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(StandupResponseResponse.fromEntity(standupResponse));
    }


    @GetMapping("/{id}")
    public ResponseEntity<StandupRunResponse> getByIdWithDetails(@PathVariable UUID id){

        StandupRun standupRun = standupRunService.getByIdWithDetails(id);
        return ResponseEntity.ok(StandupRunResponse.fromEntity(standupRun));
    }

    @GetMapping("/{standupRunId}/responses")
    public ResponseEntity<List<StandupResponseResponse>> getByProjectIdWithDetails(@PathVariable UUID standupRunId){
        List<StandupResponseResponse> standupResponseResponses = standupResponseService.getByStandupRunIdWithDetails(standupRunId)
                .stream()
                .map(StandupResponseResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(standupResponseResponses);
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<StandupRunResponse> updateStandupRun(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStandupRunRequest request
            ){

        StandupRun standupRun = standupRunService.updateStandupRun(id, request);
        return ResponseEntity.ok(StandupRunResponse.fromEntity(standupRun));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStartupRun(@PathVariable UUID id){

        standupRunService.deleteStandupRun(id);
        return ResponseEntity.noContent().build();
    }
}
