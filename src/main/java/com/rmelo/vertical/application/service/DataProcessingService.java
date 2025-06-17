package com.rmelo.vertical.application.service;

import com.rmelo.vertical.core.domain.model.Order;
import com.rmelo.vertical.core.domain.model.User;
import com.rmelo.vertical.core.domain.model.dto.OrderDTO;
import com.rmelo.vertical.core.domain.model.dto.UserResponseDTO;
import com.rmelo.vertical.core.domain.repository.OrderRepository;
import com.rmelo.vertical.core.domain.repository.UserRepository;
import com.rmelo.vertical.infrastructure.parser.TxtParser;
import com.rmelo.vertical.shared.exception.InvalidDateRangeException;
import com.rmelo.vertical.shared.utils.DateValidator;
import com.rmelo.vertical.shared.utils.ResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DataProcessingService {

    private final TxtParser txtParser;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private static final Logger log = LoggerFactory.getLogger(DataProcessingService.class);


    public DataProcessingService(TxtParser txtParser, UserRepository userRepository, OrderRepository orderRepository) {
        this.txtParser = txtParser;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public List<User> processAndSaveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado está vazio.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<User> parsedUsers = txtParser.processFile(reader);

            if (parsedUsers.isEmpty()) {
                log.warn("Nenhum usuário encontrado no arquivo.");
                return List.of();
            }

            userRepository.saveAll(parsedUsers);
            return parsedUsers;
        }
    }

    public Optional<UserResponseDTO> findOrderByOrderId(int orderId) {
        try {
            return orderRepository.findByOrderId(orderId)
                    .map(order -> ResponseMapper.toUserOneOrderDTO(order.getUser(),orderId));

        } catch (Exception e) {
            log.warn("Erro ao buscar detalhes do pedido ID {}: {}", orderId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    public List<UserResponseDTO> getAllOrders() {
        List<User> users = userRepository.findAllWithOrdersAndProducts();

        if (users.isEmpty()) {
            log.warn("Nenhum usuário encontrado.");
            return List.of();
        }

        return users.stream()
                .map(ResponseMapper::toUserDTO)
                .toList();
    }

    public List<UserResponseDTO> findOrdersByDateRange(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new InvalidDateRangeException("Os parâmetros startDate e endDate são obrigatórios.");
        }

        if (!DateValidator.isValidDateRange(startDate, endDate)) {
            throw new InvalidDateRangeException("A data inicial deve ser menor ou igual à data final.");
        }

        List<Order> orders = orderRepository.findByPurchaseDateBetween(startDate, endDate);

        Map<User, List<OrderDTO>> userOrdersMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getUser,
                        Collectors.mapping(ResponseMapper::toOrderDTO, Collectors.toList())));

        return userOrdersMap.entrySet().stream()
                .map(entry -> new UserResponseDTO(entry.getKey().getUserId(), entry.getKey().getName(), entry.getValue()))
                .toList();

    }
}
    