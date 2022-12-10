package com.microservices.swotlyzer.auth.service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.microservices.swotlyzer.common.config.models.BaseUser;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseUser {
}
