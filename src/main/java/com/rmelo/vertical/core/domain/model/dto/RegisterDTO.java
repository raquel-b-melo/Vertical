package com.rmelo.vertical.core.domain.model.dto;

import com.rmelo.vertical.core.domain.model.enums.Roles;

public record RegisterDTO(String login, String password, Roles role) {
}
