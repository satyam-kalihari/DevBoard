package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateNotificationRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Notification;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.model.Task;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.NotificationRepository;
import com.satyam.DevBoard.repository.OrganizationRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final NotificationRepository notificationRepository;

    public Notification createNotification(CreateNotificationRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Organization organization = organizationRepository.findById(request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setOrganization(organization);
        notification.setTitle(request.getTitle());
        notification.setType(request.getType());
        notification.setPayload(request.getPayload());

        return notificationRepository.save(notification);
    }

    public void notifyTaskAssigned(Task task, User assignee){

        CreateNotificationRequest notificationRequest = new CreateNotificationRequest();
        notificationRequest.setUserId(assignee.getId());
        notificationRequest.setOrgId(task.getProject().getOrganization().getId());
        notificationRequest.setTitle(task.getTitle());
        notificationRequest.setType(Notification.Type.TASK_ASSIGNED);
        notificationRequest.setPayload(Map.of(
                "taskId", task.getId().toString(),
                "projectId", task.getProject().getId().toString()
        ));

        createNotification(notificationRequest);
    }
}
