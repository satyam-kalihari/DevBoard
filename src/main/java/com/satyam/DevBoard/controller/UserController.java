package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateUserRequest;
import com.satyam.DevBoard.dto.request.UpdateUserRequest;
import com.satyam.DevBoard.dto.response.UserResponse;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request
            ){
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromEntity(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id){
        User user = userService.getUserById(id);

        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.fromEntity(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    ){
        User user = userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserResponse.fromEntity(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
