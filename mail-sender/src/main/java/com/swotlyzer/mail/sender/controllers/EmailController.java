package com.swotlyzer.mail.sender.controllers;

import com.swotlyzer.mail.sender.dtos.EmailDTO;
import com.swotlyzer.mail.sender.models.Email;
import com.swotlyzer.mail.sender.services.EmailService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
