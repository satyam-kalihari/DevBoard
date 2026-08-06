package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateUserRequest;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
