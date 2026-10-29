package com.satyam.DevBoard.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TaskListCacheInvalidator {
    private final TaskListCache cache;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProjectTasksChanged(ProjectTasksChanged event) {
        cache.evict(event.projectId());
    }
}
