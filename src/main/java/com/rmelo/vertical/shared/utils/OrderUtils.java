package com.rmelo.vertical.shared.utils;

import com.rmelo.vertical.core.domain.model.Order;
import java.math.BigDecimal;
import com.rmelo.vertical.core.domain.model.Product;

public class OrderUtils {
    public static BigDecimal calculateTotal(Order order) {
        return order.getProducts().stream()
                .map(Product::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}