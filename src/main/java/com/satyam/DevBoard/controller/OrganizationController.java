package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateOrganizationRequest;
import com.satyam.DevBoard.dto.request.UpdateOrganizationRequest;
import com.satyam.DevBoard.dto.response.OrganizationResponse;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(
            @Valid @RequestBody CreateOrganizationRequest request
            ){
        Organization org = organizationService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrganizationResponse.fromEntity(org));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getOrganization(@PathVariable UUID id){
        Organization org = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(OrganizationResponse.fromEntity(org));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations(){
        List<OrganizationResponse> organizations = organizationService.getAllOrganizations()
                .stream()
                .map(OrganizationResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(organizations);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrganizationResponse> updateOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrganizationRequest request
    ){
        Organization organization = organizationService.updateOrganization(id, request);
        return ResponseEntity.ok(OrganizationResponse.fromEntity(organization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable UUID id){
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}
