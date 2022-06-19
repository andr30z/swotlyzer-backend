package com.swotlyzer.mail.sender.models;

import com.swotlyzer.mail.sender.enums.MailStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
