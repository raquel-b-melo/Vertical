package com.rmelo.vertical.shared.utils;

import com.rmelo.vertical.core.domain.model.Order;
import com.rmelo.vertical.core.domain.model.User;
import com.rmelo.vertical.core.domain.model.dto.OrderDTO;
import com.rmelo.vertical.core.domain.model.dto.ProductDTO;
import com.rmelo.vertical.core.domain.model.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseMapper {

    public static UserResponseDTO toUserDTO(User user) {
        List<OrderDTO> orders = user.getOrders().stream()
                .map(ResponseMapper::toOrderDTO)
                .collect(Collectors.toList());

        return new UserResponseDTO(user.getUserId(), user.getName(), orders);
    }

    public static OrderDTO toOrderDTO(Order order) {
        List<ProductDTO> products = order.getProducts().stream()
                .map(p -> new ProductDTO(p.getProductId(), p.getValue().toString()))
                .collect(Collectors.toList());

        return new OrderDTO(order.getOrderId(), OrderUtils.calculateTotal(order).toString(), order.getPurchaseDate().toString(), products);
    }

    public static UserResponseDTO toUserOneOrderDTO(User user, int orderId) {
        List<OrderDTO> orders = user.getOrders().stream()
                .filter(order -> order.getOrderId() == orderId)
                .map(ResponseMapper::toOrderDTO)
                .toList();

        return new UserResponseDTO(user.getUserId(), user.getName(), orders);
    }
}
