package com.satyam.DevBoard.messaging;

import com.satyam.DevBoard.config.KafkaTopicConfig;
import com.satyam.DevBoard.event.TaskAssignedEvent;
import com.satyam.DevBoard.event.TaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTaskAssigned(TaskEvent event){
        kafkaTemplate.send(KafkaTopicConfig.TASK_EVENTS, event.taskId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null){
                        log.error("Failed to publish {}", event.eventId(), ex);
                        //if Kafka is down at the moment after the commit, the event is lost.
                        // The proper fix is the outbox pattern, where the event is written to a database table in the
                        // same transaction and a separate process relays it.
                    }else {
                        log.info("Published {} to partition {} offset {}",
                                event.eventId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
