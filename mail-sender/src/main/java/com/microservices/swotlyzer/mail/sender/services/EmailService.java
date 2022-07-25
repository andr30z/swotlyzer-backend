package com.microservices.swotlyzer.mail.sender.services;

import com.microservices.swotlyzer.common.config.dtos.EmailDTO;
import com.microservices.swotlyzer.mail.sender.models.Email;

public interface EmailService {
    Email sendMail(EmailDTO emailDTO);
}
