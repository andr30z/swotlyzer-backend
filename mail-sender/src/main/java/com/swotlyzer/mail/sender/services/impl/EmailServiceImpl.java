package com.swotlyzer.mail.sender.services.impl;

import com.swotlyzer.mail.sender.dtos.EmailDTO;
import com.swotlyzer.mail.sender.enums.MailStatus;
import com.swotlyzer.mail.sender.models.Email;
import com.swotlyzer.mail.sender.repositories.EmailRepository;
import com.swotlyzer.mail.sender.services.EmailService;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;

    private final JavaMailSender javaMailSender;

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
        } finally {
            return this.emailRepository.save(email);
        }
    }
}
