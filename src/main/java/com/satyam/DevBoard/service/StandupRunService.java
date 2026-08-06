package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateStandupRunRequest;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Standup;
import com.satyam.DevBoard.model.StandupRun;
import com.satyam.DevBoard.repository.StandupRepository;
import com.satyam.DevBoard.repository.StandupRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StandupRunService {

    private final StandupRepository standupRepository;
    private final StandupRunRepository standupRunRepository;

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

    /*
     * This method will be called later (e.g., by a Scheduled Cron Job at the end of the day)
     * after hitting the Anthropic API for the AI Summary.
     */

    public StandupRun finalizedStandupRun(UUID standupRunId, String aiSummary){

        StandupRun standupRun = standupRunRepository.findById(standupRunId)
                .orElseThrow(() -> new ResourceNotFoundException("Standup run not found"));

        if (standupRun.isFinalized()){
            throw new IllegalStateException("This standup run has already been finalized.");
        }

        standupRun.setAiSummary(aiSummary);
        standupRun.setFinalized(true);
        standupRun.setFinalizedAt(LocalDateTime.now());

        return standupRunRepository.save(standupRun);
    }
}
