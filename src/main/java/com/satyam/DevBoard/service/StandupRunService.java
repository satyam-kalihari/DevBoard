package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateStandupRunRequest;
import com.satyam.DevBoard.dto.request.UpdateStandupRunRequest;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.*;
import com.satyam.DevBoard.repository.ProjectMemberRepository;
import com.satyam.DevBoard.repository.StandupRepository;
import com.satyam.DevBoard.repository.StandupResponseRepository;
import com.satyam.DevBoard.repository.StandupRunRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StandupRunService {

    private final StandupRepository standupRepository;
    private final StandupRunRepository standupRunRepository;
    private final StandupResponseRepository standupResponseRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final NotificationService notificationService;

    @Transactional
    public StandupRun createStandupRun(CreateStandupRunRequest request){

        if (standupRunRepository.existsByStandupIdAndRunDate(request.getStandupId(), request.getRunDate())){
            throw new DuplicateResourceException("A standup run already exists for this date.");
        }

        Standup standup = standupRepository.findById(request.getStandupId())
                .orElseThrow(() -> new ResourceNotFoundException("Standup does not exists"));

        StandupRun standupRun = new StandupRun();
        standupRun.setStandup(standup);
        standupRun.setRunDate(request.getRunDate());

        return standupRunRepository.save(standupRun);
    }

    @Transactional
    public void openDailyRun(UUID standupId, LocalDate runDate) {
        if (standupRunRepository.existsByStandupIdAndRunDate(standupId, runDate)) {
            return;   // already opened today, which is the normal case on every later tick
        }

        Standup standup = standupRepository.findByIdWithProject(standupId)
                .orElseThrow(() -> new ResourceNotFoundException("Standup not found"));

        StandupRun run = new StandupRun();
        run.setStandup(standup);
        run.setRunDate(runDate);
        // saveAndFlush sends the INSERT now, so a unique-constraint race fails here, before any reminders
        standupRunRepository.saveAndFlush(run);

        Project project = standup.getProject();
        for (ProjectMember member : projectMemberRepository.findAllByProjectIdWithUser(project.getId())) {
            notificationService.notifyStandupReminder(
                    member.getUser(), project.getOrganization(),
                    project.getName(), project.getId(), run.getId());
        }
        log.info("Opened standup run {} for project {} on {}", run.getId(), project.getName(), runDate);
    }

    /*
     * This method will be called later (e.g., by a Scheduled Cron Job at the end of the day)
     * after hitting the Anthropic API for the AI Summary.
     */

    @Transactional
    public StandupRun finalizedStandupRun(UUID standupRunId, String aiSummary){

        StandupRun standupRun = standupRunRepository.findById(standupRunId)
                .orElseThrow(() -> new ResourceNotFoundException("Standup run not found"));

        if (standupRun.getIsFinalized()){
            throw new IllegalStateException("This standup run has already been finalized.");
        }

        standupRun.setAiSummary(aiSummary);
        standupRun.setIsFinalized(true);
        standupRun.setFinalizedAt(LocalDateTime.now());

        return standupRunRepository.save(standupRun);
    }

    @Transactional(readOnly = true)
    public StandupRun getByIdWithDetails(UUID id){
        return standupRunRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Standup run does not exists"));
    }

    @Transactional(readOnly = true)
    public List<StandupRun> getAllByStandupIdWithDetails(UUID standupId){
        return standupRunRepository.findAllByStandupIdWithDetails(standupId);
    }

    @Transactional
    public StandupRun updateStandupRun(UUID id, UpdateStandupRunRequest request){

        StandupRun standupRun = getByIdWithDetails(id);

        if (request.getAiSummary() != null) standupRun.setAiSummary(request.getAiSummary());

        return standupRunRepository.save(standupRun);
    }

    @Transactional
    public void deleteStandupRun(UUID id){
        StandupRun standupRun = getByIdWithDetails(id);
        standupRunRepository.delete(standupRun);
    }
}
