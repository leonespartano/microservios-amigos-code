package com.amigoscode.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public NewTopic amigosCodeTopic(){
        return TopicBuilder.name("code-services")
                .build();
    }
}
