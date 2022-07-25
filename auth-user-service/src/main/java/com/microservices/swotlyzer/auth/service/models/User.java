package com.microservices.swotlyzer.auth.service.models;


import com.microservices.swotlyzer.common.config.models.BaseUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseUser {
}
