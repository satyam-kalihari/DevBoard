package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateOrganizationRequest;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
