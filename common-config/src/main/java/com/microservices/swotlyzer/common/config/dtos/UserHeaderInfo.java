package com.microservices.swotlyzer.common.config.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserHeaderInfo {
    Long userId;
    String username;
}
