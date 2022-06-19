package com.microservices.swotlyzer.mail.sender.controllers;

import com.microservices.swotlyzer.common.config.dtos.EmailDTO;
import com.microservices.swotlyzer.mail.sender.models.Email;
import com.microservices.swotlyzer.mail.sender.services.EmailService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/email")
@RestController
public class EmailController {


    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public Email send(@RequestBody @Validated EmailDTO emailDTO) {
        return this.emailService.sendMail(emailDTO);
    }
}
