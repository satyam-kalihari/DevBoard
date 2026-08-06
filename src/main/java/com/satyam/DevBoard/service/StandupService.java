package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateStandupRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.Standup;
import com.satyam.DevBoard.model.StandupRun;
import com.satyam.DevBoard.repository.ProjectRepository;
import com.satyam.DevBoard.repository.StandupRepository;
import com.satyam.DevBoard.repository.StandupRunRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StandupService {
    private final ProjectRepository projectRepository;
    private final StandupRunRepository standupRunRepository;
    private final StandupRepository standupRepository;

    @Transactional
    public Standup createStandup(CreateStandupRequest request){
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
}
