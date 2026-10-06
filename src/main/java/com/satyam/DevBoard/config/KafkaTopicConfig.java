package com.satyam.DevBoard.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String TASK_EVENTS = "task_events";

    @Bean
    public NewTopic taskEventsTopic(){
        return TopicBuilder
                .name(TASK_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
