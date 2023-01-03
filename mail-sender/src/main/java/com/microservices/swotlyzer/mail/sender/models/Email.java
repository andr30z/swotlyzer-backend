package com.microservices.swotlyzer.mail.sender.models;

import com.microservices.swotlyzer.mail.sender.enums.MailStatus;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emails_seq")
    @SequenceGenerator(name = "emails_seq", sequenceName = "emails_seq", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;
    private String ownerRef;
    private String emailFrom;
    private String emailTo;
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime sendDateMail;
    @Column(name = "status")
    private MailStatus mailStatus;

}
