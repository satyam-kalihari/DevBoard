package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateLabelRequest;
import com.satyam.DevBoard.dto.request.UpdateLabelRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Label;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.repository.LabelRepository;
import com.satyam.DevBoard.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final OrganizationRepository organizationRepository;
    private final LabelRepository labelRepository;

    @Transactional
    public Label createLabel(UUID organizationId, CreateLabelRequest request){
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

        Label label = new Label();
        label.setOrganization(organization);
        label.setName(request.getName());

        if (request.getColorHex() != null && request.getColorHex().isBlank()){
            label.setColorHex(request.getColorHex());
        }

        return labelRepository.save(label);
    }

//    GET ALL LABEL OF THE ORGANIZATION
    @Transactional(readOnly = true)
    public List<Label> getAllLabelByOrgId(UUID orgId){
        return labelRepository.getAllLabelByOrgId(orgId);
    }

//    GET ALL LABEL
    @Transactional(readOnly = true)
    public List<Label> getAllByIdWithDetail(){
        return labelRepository.getAllLabelWithDetail();
    }

//    GET LABEL BY ID
    @Transactional(readOnly = true)
    public Label getLabelByIdWithDetail(UUID id){
        return labelRepository.getLabelByIdWithDetail(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label does not exists"));
    }

//    UPDATE LABEL
    @Transactional
    public Label updateLabel(UUID id, UpdateLabelRequest request){
        Label label = labelRepository.getLabelByIdWithDetail(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label does not exists"));

        if (request.getName() != null){
            label.setName(request.getName());
        }
        if (request.getColorHex() != null){
            label.setColorHex(request.getColorHex());
        }

        return labelRepository.save(label);
    }

//    DELETE LABEL
    @Transactional
    public void deleteLabel(UUID id){
        Label label = getLabelByIdWithDetail(id);
        labelRepository.delete(label);
    }
}
