package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateLabelRequest;
import com.satyam.DevBoard.dto.request.UpdateLabelRequest;
import com.satyam.DevBoard.dto.response.LabelResponse;
import com.satyam.DevBoard.model.Label;
import com.satyam.DevBoard.service.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    @PostMapping("/organization/{orgId}")
    public ResponseEntity<LabelResponse> createLabel(
            @PathVariable UUID orgId,
            @Valid @RequestBody CreateLabelRequest request
            ){
        Label label = labelService.createLabel(orgId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(LabelResponse.fromEntity(label));
    }

    @GetMapping(params = "orgId")
    public ResponseEntity<List<LabelResponse>> getAllLabelByOrgId(
            @RequestParam UUID orgId
    ){
        List<LabelResponse> labels = labelService.getAllLabelByOrgId(orgId)
                .stream()
                .map(LabelResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(labels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelResponse> getById(
            @PathVariable UUID id
    ){
        Label label = labelService.getLabelByIdWithDetail(id);

        return ResponseEntity.status(HttpStatus.OK).body(LabelResponse.fromEntity(label));
    }

    @GetMapping
    public ResponseEntity<List<LabelResponse>> getAllLabel(){
        List<LabelResponse> labels = labelService.getAllByIdWithDetail()
                .stream()
                .map(LabelResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(labels);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LabelResponse> updateLabel(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLabelRequest request
            ){
        Label label = labelService.updateLabel(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(LabelResponse.fromEntity(label));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable UUID id){
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
}
