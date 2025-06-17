package com.rmelo.vertical.infrastructure.parser;

import com.rmelo.vertical.core.domain.model.Order;
import com.rmelo.vertical.core.domain.model.Product;
import com.rmelo.vertical.core.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class TxtParser {

    public List<User> processFile(BufferedReader reader) {
        Map<Integer, User> userMap = new HashMap<>();
        Map<Integer, Order> orderMap = new HashMap<>();

        try {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                int userId = Integer.parseInt(line.substring(0, 10).trim());
                String userName = line.substring(10, 55).trim();
                int orderId = Integer.parseInt(line.substring(55, 65).trim());
                int productId = Integer.parseInt(line.substring(65, 75).trim());
                BigDecimal value = new BigDecimal(line.substring(75, 87).trim());
                LocalDate date = LocalDate.parse(line.substring(87, 95).trim(), java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

                User user = userMap.computeIfAbsent(userId, id -> new User(id, userName, new HashSet<>()));
                Order order = orderMap.computeIfAbsent(orderId, id -> new Order(id, date, user, new HashSet<>()));

                Product product = new Product(productId, value, order);
                order.getProducts().add(product);

                user.getOrders().add(order);
            }

        } catch (IOException e) {
            log.error("Erro ao processar o arquivo: ", e);
        }

        return new ArrayList<>(userMap.values());
    }
}