package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateStandupRequest;
import com.satyam.DevBoard.dto.request.UpdateStandupRequest;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.Standup;
import com.satyam.DevBoard.model.StandupRun;
import com.satyam.DevBoard.repository.ProjectRepository;
import com.satyam.DevBoard.repository.StandupRepository;
import com.satyam.DevBoard.repository.StandupRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StandupService {
    private final ProjectRepository projectRepository;
    private final StandupRunRepository standupRunRepository;
    private final StandupRepository standupRepository;

    @Transactional
    public Standup createStandup(CreateStandupRequest request){

        if (standupRepository.existsByProjectId(request.getProjectId())){
            throw  new DuplicateResourceException("Standup with for this project already exists");
        }
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exists"));

        Standup standup = new Standup();
        standup.setProject(project);
        standup.setScheduleDays(request.getScheduleDays());
        standup.setScheduledTime(request.getScheduledTime());

        if (request.getRunsId() != null && !request.getRunsId().isEmpty()) {
            List<StandupRun> standupRuns = standupRunRepository.findAllById(request.getRunsId());
            if (standupRuns.size() != request.getRunsId().size()) {
                throw new ResourceNotFoundException("One or more standup runs were not found");
            }

            for (StandupRun run : standupRuns){
                run.setStandup(standup);
            }

            standup.setRuns(standupRuns);
        }

        return standupRepository.save(standup);
    }

//    GET STANDUP BY ID
    @Transactional(readOnly = true)
    public Standup getStandUpByIdWithDetails(UUID id){
        return standupRepository.findStandupByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Standup does not exists"));
    }

//    GET STANDUP BY PROJECT ID
    @Transactional(readOnly = true)
    public Standup getStandupByProjectIdWithDetails(UUID projectId){
        return standupRepository.findStandupByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Standup does not exists"));
    }

//    UPDATE STANDUP
    @Transactional
    public Standup updateStandup(UUID id, UpdateStandupRequest request){

        Standup standup = getStandUpByIdWithDetails(id);

        if (request.getScheduleDays() != null){
            standup.setScheduleDays(request.getScheduleDays());
        }
        if (request.getIsActive() != null){
            standup.setIsActive(request.getIsActive());
        }
        if (request.getScheduledTime() != null) {
            standup.setScheduledTime(request.getScheduledTime());
        }

        return standupRepository.save(standup);
    }

    @Transactional
    public void deleteStandup(UUID id) {
        Standup standup = standupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Standup configuration not found."));
        standupRepository.delete(standup);
    }
}
