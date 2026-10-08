package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateNotificationRequest;
import com.satyam.DevBoard.dto.request.UpdateNotificationRequest;
import com.satyam.DevBoard.event.TaskAssignedEvent;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.messaging.ProcessedEventStore;
import com.satyam.DevBoard.model.Notification;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.NotificationRepository;
import com.satyam.DevBoard.repository.OrganizationRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final NotificationRepository notificationRepository;
    private final ProcessedEventStore processedEventStore;

    @Transactional
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

    @Transactional
    public void notifyTaskAssigned(TaskAssignedEvent event){

        if(!processedEventStore.markedProcessed("notifications", event.eventId())){
            log.info("Skipping duplicate event {}", event.eventId());
            return;
        }
        if (event.actorId() != null && event.actorId().equals(event.assigneeId())){
            log.info("Skipping self-assignment notification for {}", event.eventId());
            return;
        }

        CreateNotificationRequest notificationRequest = new CreateNotificationRequest();
        notificationRequest.setUserId(event.assigneeId());
        notificationRequest.setOrgId(event.orgId());
        notificationRequest.setTitle(event.taskTitle());
        notificationRequest.setType(Notification.Type.TASK_ASSIGNED);
        notificationRequest.setPayload(Map.of(
                "taskId", event.taskId(),
                "projectId", event.projectId()
        ));

        createNotification(notificationRequest);
    }

    @Transactional
    public void notifyMemberInvited(User invitedUser, Organization org) {
        CreateNotificationRequest req = new CreateNotificationRequest();
        req.setUserId(invitedUser.getId());
        req.setOrgId(org.getId());
        req.setType(Notification.Type.MEMBER_INVITED);
        req.setTitle("You were invited to join \"" + org.getName() + "\"");
        req.setPayload(Map.of("orgId", org.getId().toString()));
        createNotification(req);
    }

    @Transactional
    public void notifyStandupReminder(User user, Organization org, String projectName) {
        CreateNotificationRequest req = new CreateNotificationRequest();
        req.setUserId(user.getId());
        req.setOrgId(org.getId());
        req.setType(Notification.Type.STANDUP_REMINDER);
        req.setTitle("Daily standup for \"" + projectName + "\" is ready");
        req.setPayload(Map.of("projectName", projectName));
        createNotification(req);
    }

    @Transactional
    public void notifySprintStarted(User user, Organization org, String sprintName) {
        CreateNotificationRequest req = new CreateNotificationRequest();
        req.setUserId(user.getId());
        req.setOrgId(org.getId());
        req.setType(Notification.Type.SPRINT_STARTED);
        req.setTitle("Sprint \"" + sprintName + "\" has started");
        req.setPayload(Map.of("sprintName", sprintName));
        createNotification(req);
    }

    @Transactional
    public void notifySprintEnded(User user, Organization org, String sprintName) {
        CreateNotificationRequest req = new CreateNotificationRequest();
        req.setUserId(user.getId());
        req.setOrgId(org.getId());
        req.setType(Notification.Type.SPRINT_ENDED);
        req.setTitle("Sprint \"" + sprintName + "\" has ended");
        req.setPayload(Map.of("sprintName", sprintName));
        createNotification(req);
    }


    @Transactional(readOnly = true)
    public Notification getByIdWithDetails(UUID id) {
        return notificationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
    }

    @Transactional(readOnly = true)
    public List<Notification> getAllByUserId(UUID userId) {
        return notificationRepository.findAllByUserIdWithDetails(userId);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadByUserId(UUID userId) {
        return notificationRepository.findUnreadByUserId(userId);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(UUID userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

//    WRITE OPERATIONS
    @Transactional
    public Notification markAsRead(UUID id, UpdateNotificationRequest request){
        Notification notification = notificationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        notification.setRead(request.getIsRead());
        return notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }

    @Transactional
    public void deleteNotification(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notificationRepository.delete(notification);
    }
}
