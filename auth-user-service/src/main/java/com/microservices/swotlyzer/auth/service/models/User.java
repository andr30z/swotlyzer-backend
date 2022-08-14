package com.microservices.swotlyzer.auth.service.models;


import com.microservices.swotlyzer.common.config.models.BaseUser;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseUser {
}
