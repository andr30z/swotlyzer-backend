package com.swotlyzer.mail.sender.repositories;

import com.swotlyzer.mail.sender.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
