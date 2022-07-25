package com.microservices.swotlyzer.mail.sender.repositories;

import com.microservices.swotlyzer.mail.sender.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
