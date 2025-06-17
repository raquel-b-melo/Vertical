package com.rmelo.vertical.core.domain.model.dto;

import java.util.List;

public record UserResponseDTO(int userId, String name, List<OrderDTO> orders) {}
