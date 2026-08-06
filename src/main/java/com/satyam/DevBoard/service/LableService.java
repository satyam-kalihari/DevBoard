package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateLabelRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Label;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.repository.LabelRepository;
import com.satyam.DevBoard.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LableService {

    private final OrganizationRepository organizationRepository;
    private final LabelRepository labelRepository;

    public Label createLabel(UUID organizationId, CreateLabelRequest request){
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        Label label = new Label();
        label.setOrganization(organization);
        label.setName(request.getName());
        label.setColorHex(request.getColorHex());

        return labelRepository.save(label);
    }
}
