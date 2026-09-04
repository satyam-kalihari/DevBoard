package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateUserRequest;
import com.satyam.DevBoard.dto.request.UpdateUserRequest;
import com.satyam.DevBoard.dto.response.UserResponse;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(CreateUserRequest request){

        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("An account with this email already exists.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone((request.getPhone()));
        user.setAvatarUrl(request.getAvatarUrl());

        return userRepository.save(user);
    }

    public User getUserById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
    }

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users;
    }

    public User updateUser(UUID id, UpdateUserRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        if (request.getName() != null){
            user.setName(request.getName());
        }

        if(request.getPhone() != null){
            user.setPhone(request.getPhone());
        }

        if (request.getAvatarUrl() != null){
            user.setAvatarUrl(request.getAvatarUrl());
        }
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        userRepository.delete(user);
    }
}
