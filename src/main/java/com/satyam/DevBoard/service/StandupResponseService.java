package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateStandupResponseRequest;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.StandupResponse;
import com.satyam.DevBoard.model.StandupRun;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.StandupResponseRepository;
import com.satyam.DevBoard.repository.StandupRunRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StandupResponseService {

    private final StandupRunRepository standupRunRepository;
    private final UserRepository userRepository;
    private final StandupResponseRepository standupResponseRepository;

    @Transactional
    public StandupResponse createStandupResponse(CreateStandupResponseRequest request){

        if (standupResponseRepository.existsByStandupRunIdAndUserId(request.getStandupRunId(), request.getUserId())){
            throw new DuplicateResourceException("You have already submitted a response for this standup.");
        }
        StandupRun standupRun = standupRunRepository.findById(request.getStandupRunId())
                .orElseThrow(() -> new ResourceNotFoundException("Standup Run not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        StandupResponse standupResponse = new StandupResponse();
        standupResponse.setStandupRun(standupRun);
        standupResponse.setUser(user);
        standupResponse.setAnswerYesterday(request.getAnswerYesterday());
        standupResponse.setAnswerToday(request.getAnswerToday());
        standupResponse.setAnswerBlockers(request.getAnswerBlockers());
        boolean hasBlocker = request.getAnswerBlockers() != null && !request.getAnswerBlockers().trim().isEmpty();
        standupResponse.setHasBlockers(hasBlocker);

        return standupResponseRepository.save(standupResponse);
    }

//    GET STANDUP RESPONSES BY STANDUP RUN
    @Transactional(readOnly = true)
    public List<StandupResponse> getByStandupRunIdWithDetails(UUID standupRunId){
        return standupResponseRepository.findByStandupRunIdWithDetails(standupRunId);
    }

//    GET STANDUP RESPONSES BY USER ID
    @Transactional(readOnly = true)
    public List<StandupResponse> getByUserIdWithDetails(UUID userId){
        return standupResponseRepository.findByUserIdWithDetails(userId);
    }

//    GET BY ID
    @Transactional(readOnly = true)
    public StandupResponse getById(UUID id){
        return standupResponseRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Standup Response does not exists"));
    }

//    DELETE STANDUP RESPONSE
    @Transactional
    public void deleteStandupResponse(UUID id){

        StandupResponse standupResponse = standupResponseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Standup Response does not exists"));

        if(standupResponse.getStandupRun().getIsFinalized()){
            throw  new IllegalStateException(
                    "Cannot delete a response from a finalized standup run"
            );
        }

        standupResponseRepository.delete(standupResponse);
    }
}
