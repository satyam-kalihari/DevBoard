package com.satyam.DevBoard.controller;

//OrgMemberService
//OrgMemberRepo
//OrgMember DTOs (OrgMemberResponse, UpdateOrgMember)

import com.satyam.DevBoard.dto.request.CreateOrgMemberRequest;
import com.satyam.DevBoard.dto.request.UpdateOrgMemberRequest;
import com.satyam.DevBoard.dto.response.OrgMemberResponse;
import com.satyam.DevBoard.model.OrgMember;
import com.satyam.DevBoard.service.OrgMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/org_members")
@RequiredArgsConstructor
public class OrgMemberController {
    private final OrgMemberService orgMemberService;

    @PostMapping
    public ResponseEntity<OrgMemberResponse> createOrgMember(
            @Valid @RequestBody CreateOrgMemberRequest request
            ){

        OrgMember member = orgMemberService.createOrgMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrgMemberResponse.fromEntity(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrgMemberResponse> getOrgMemberById(
            @PathVariable UUID id
            ){
        OrgMember member = orgMemberService.getOrgMemberById(id);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(OrgMemberResponse.fromEntity(member));
    }

    @GetMapping
    public ResponseEntity<List<OrgMemberResponse>> getAllOrgMember(){
        List<OrgMemberResponse> organizations = orgMemberService.getAllOrgMember()
                .stream()
                .map(OrgMemberResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(organizations);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrgMemberResponse> updateOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrgMemberRequest request
    ){
        OrgMember member = orgMemberService.updateOrgMember(id, request);
        return ResponseEntity.ok(OrgMemberResponse.fromEntity(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrgMember(@PathVariable UUID id){
        orgMemberService.deleteOrgMember(id);
        return ResponseEntity.noContent().build();
    }

}
