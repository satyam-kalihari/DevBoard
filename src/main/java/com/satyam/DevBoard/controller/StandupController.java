package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateStandupRequest;
import com.satyam.DevBoard.dto.request.UpdateStandupRequest;
import com.satyam.DevBoard.dto.response.StandupResponse;
import com.satyam.DevBoard.model.Standup;
import com.satyam.DevBoard.service.StandupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/standups")
@RequiredArgsConstructor
public class StandupController {

    private final StandupService standupService;

    @PostMapping
    public ResponseEntity<StandupResponse> createStandup(
            @Valid @RequestBody CreateStandupRequest request
            ){

        Standup standup = standupService.createStandup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(StandupResponse.fromEntity(standup));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandupResponse> getByIdWithDetails(@PathVariable UUID id){

        Standup standup = standupService.getStandUpByIdWithDetails(id);
        return ResponseEntity.ok(StandupResponse.fromEntity(standup));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StandupResponse> updateStandup(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStandupRequest request
            ){

        Standup standup = standupService.updateStandup(id, request);
        return ResponseEntity.ok(StandupResponse.fromEntity(standup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStandup(@PathVariable UUID id){

        standupService.deleteStandup(id);
        return ResponseEntity.noContent().build();
    }
}
