package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateOrganizationRequest;
import com.satyam.DevBoard.dto.request.UpdateOrganizationRequest;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public Organization createOrganization(CreateOrganizationRequest request){
        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setSlug(generateUniqueSlug(request.getSlug()));
        organization.setCode(generateCode(request.getName()));
        organization.setAvatarUrl(request.getAvatarUrl());
        organization.setLocation(request.getLocation());
        organization.setTimeZone((request.getTimeZone()));

        return organizationRepository.save(organization);
    }

    private String generateCode(String name){
        String rawCode = name.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        String baseCode = rawCode.length() > 8 ? rawCode.substring(0, 8) : rawCode;

        String candidate = baseCode;
        int attempt = 1;

        while (organizationRepository.existsByCode(candidate)) {
            attempt++;
            candidate = baseCode + attempt;
        }

        return candidate;
    }

    private String generateUniqueSlug(String name) {
        String baseSlug = name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");

        String candidate = baseSlug;
        int attempt = 1;

        while (organizationRepository.existsBySlug(candidate)) {
            attempt++;
            candidate = baseSlug + "-" + attempt;
        }

        return candidate;
    }

    public Organization getOrganizationById(UUID id){
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        return org;
    }

    public List<Organization> getAllOrganizations(){
        List<Organization> organizations = organizationRepository.findAll();
        return organizations;
    }

    public Organization updateOrganization(UUID id, UpdateOrganizationRequest request){
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        if (request.getName() != null){
            org.setName(request.getName());
        }

        if (request.getSlug() != null){
            if(!request.getSlug().equals(org.getSlug()) &&
            organizationRepository.existsBySlug(request.getSlug())){
                throw new DuplicateResourceException("An organization with the slug already exists");
            }
            org.setSlug(request.getSlug());
        }

        if (request.getLocation() != null) {
            org.setLocation(request.getLocation());
        }

        if (request.getTimeZone() != null) {
            org.setTimeZone(request.getTimeZone());
        }

        if (request.getAvatarUrl() != null) {
            org.setAvatarUrl(request.getAvatarUrl());
        }
        return organizationRepository.save(org);
    }

    public void deleteOrganization(UUID id){
        Organization org = getOrganizationById(id);
        organizationRepository.delete(org);
    }
}
