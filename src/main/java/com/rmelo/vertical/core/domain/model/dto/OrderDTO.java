package com.rmelo.vertical.core.domain.model.dto;

import java.util.List;

public record OrderDTO(int orderId, String total, String date, List<ProductDTO> products) {}

