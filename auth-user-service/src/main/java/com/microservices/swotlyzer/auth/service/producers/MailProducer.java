package com.microservices.swotlyzer.auth.service.producers;

import com.microservices.swotlyzer.common.config.dtos.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailProducer {
    private final NewTopic newTopic;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public MailProducer(NewTopic newTopic,
                        KafkaTemplate<String, UserCreatedEvent> kafkaTemplate) {
        this.newTopic = newTopic;
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(UserCreatedEvent userCreatedEvent) {
        log.info("Sending mail event");
        Message<UserCreatedEvent> message = MessageBuilder.withPayload(userCreatedEvent)
                .setHeader(KafkaHeaders.TOPIC, newTopic.name()).build();

        kafkaTemplate.send(message);
    }

}
