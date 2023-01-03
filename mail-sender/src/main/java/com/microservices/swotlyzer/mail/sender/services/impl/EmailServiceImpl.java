package com.microservices.swotlyzer.mail.sender.services.impl;

import com.microservices.swotlyzer.common.config.dtos.EmailDTO;
import com.microservices.swotlyzer.mail.sender.enums.MailStatus;
import com.microservices.swotlyzer.mail.sender.models.Email;
import com.microservices.swotlyzer.mail.sender.repositories.EmailRepository;
import com.microservices.swotlyzer.mail.sender.services.EmailService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;

    public EmailServiceImpl(EmailRepository emailRepository, JavaMailSender javaMailSender) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Email sendMail(EmailDTO emailDTO) {
        var email = new Email();

        BeanUtils.copyProperties(emailDTO, email);
        email.setSendDateMail(LocalDateTime.now());
        try {
            var simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(email.getEmailFrom());
            simpleMailMessage.setTo(email.getEmailTo());
            simpleMailMessage.setSubject(email.getSubject());
            simpleMailMessage.setText(email.getContent());
            this.javaMailSender.send(simpleMailMessage);
            email.setMailStatus(MailStatus.SENT);
        } catch (MailException mailException) {
            email.setMailStatus(MailStatus.ERROR);
            log.error("Failed to send email", mailException);
        }
        return this.emailRepository.save(email);
    }
}
