package com.swotlyzer.mail.sender.services;

import com.swotlyzer.mail.sender.dtos.EmailDTO;
import com.swotlyzer.mail.sender.models.Email;

public interface EmailService {
    Email sendMail(EmailDTO emailDTO);
}
