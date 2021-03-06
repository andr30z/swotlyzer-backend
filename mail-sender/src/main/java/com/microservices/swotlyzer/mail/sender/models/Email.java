package com.microservices.swotlyzer.mail.sender.models;

import com.microservices.swotlyzer.mail.sender.enums.MailStatus;
import lombok.*;

import javax.persistence.*;
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
