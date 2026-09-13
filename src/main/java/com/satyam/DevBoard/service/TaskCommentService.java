package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateTaskCommentRequest;
import com.satyam.DevBoard.dto.request.UpdateTaskCommentRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Task;
import com.satyam.DevBoard.model.TaskComment;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.TaskCommentRepository;
import com.satyam.DevBoard.repository.TaskRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskCommentService {

    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskComment createTaskComment(CreateTaskCommentRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TaskComment comment = new TaskComment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setBody(request.getBody());

        return taskCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public TaskComment getByIdWithDetails(UUID id) {
        return taskCommentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }

    @Transactional(readOnly = true)
    public List<TaskComment> getAllByTaskId(UUID taskId) {
        return taskCommentRepository.findAllByTaskIdWithDetails(taskId);
    }

    @Transactional
    public TaskComment updateTaskComment(UUID id, UpdateTaskCommentRequest request) {
        TaskComment comment = taskCommentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        comment.setBody(request.getBody());
        return taskCommentRepository.save(comment);
    }

    @Transactional
    public void deleteTaskComment(UUID id) {
        TaskComment comment = taskCommentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        taskCommentRepository.delete(comment);
    }
}