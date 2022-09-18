package com.microservices.swotlyzer.mail.sender.consumers;

import com.microservices.swotlyzer.common.config.dtos.EmailDTO;
import com.microservices.swotlyzer.common.config.dtos.UserCreatedEvent;
import com.microservices.swotlyzer.mail.sender.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailSenderConsumer {

    private final EmailService emailService;


    public MailSenderConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = {"${spring.kafka.topic.name}"}, groupId = "${spring.kafka.consumer.group-id}")
    public void consume(UserCreatedEvent userCreatedEvent) {
        log.info("Consuming event for user with id => {} and name => {}",
                userCreatedEvent.getUserId(),
                userCreatedEvent.getName());
        String EMAIL_FROM = "no.reply.swotlyzer@gmail.com";
        String WELCOME = "Welcome";
        String CONTENT = String.format("Hello, %s, \n Welcome to our app!", userCreatedEvent.getName());
        EmailDTO emailDTO =
                EmailDTO.builder().ownerRef(userCreatedEvent.getUserId().toString()).subject(WELCOME).content(CONTENT)
                        .emailFrom(EMAIL_FROM).emailTo(userCreatedEvent.getEmail()).build();
        emailService.sendMail(emailDTO);
    }
}
