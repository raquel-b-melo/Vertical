package com.rmelo.vertical.infrastructure.rest.api;

import com.rmelo.vertical.application.service.DataProcessingService;
import com.rmelo.vertical.core.domain.model.Order;
import com.rmelo.vertical.core.domain.model.User;
import com.rmelo.vertical.core.domain.model.dto.OrderDTO;
import com.rmelo.vertical.core.domain.model.dto.UserResponseDTO;
import com.rmelo.vertical.shared.utils.ResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    private final DataProcessingService dataProcessingService;

    public SearchController(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }


    //Exemplo de URL: /search/order/123
    @GetMapping("/order/{orderId}")
    public ResponseEntity<UserResponseDTO> findOrderByOrderId(@PathVariable int orderId) {
        return dataProcessingService.findOrderByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<UserResponseDTO>> getAllOrders() {
        List<UserResponseDTO> users = dataProcessingService.getAllOrders();

        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }


    //Exemplo de URL: /search/orders/by-date?startDate=2023-01-01&endDate=2023-01-31
    @GetMapping("/orders/by-date")
    public ResponseEntity<List<UserResponseDTO>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<UserResponseDTO> users = dataProcessingService.findOrdersByDateRange(startDate, endDate);

        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }


}