package com.rmelo.vertical.infrastructure.rest.api;

import com.rmelo.vertical.application.service.DataProcessingService;
import com.rmelo.vertical.core.domain.model.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

    @Mock
    private DataProcessingService dataProcessingService;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    void setUp() {
        searchController = new SearchController(dataProcessingService);
    }

    @Test
    void shouldReturnOrderByOrderIdSuccessfully() {
        UserResponseDTO userResponseDTO = new UserResponseDTO(1, "User1", List.of());
        when(dataProcessingService.findOrderByOrderId(123)).thenReturn(Optional.of(userResponseDTO));

        ResponseEntity<UserResponseDTO> response = searchController.findOrderByOrderId(123);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().userId());
    }

    @Test
    void shouldReturnNotFoundForInvalidOrderId() {
        when(dataProcessingService.findOrderByOrderId(999)).thenReturn(Optional.empty());

        ResponseEntity<UserResponseDTO> response = searchController.findOrderByOrderId(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnAllOrdersSuccessfully() {
        List<UserResponseDTO> users = List.of(new UserResponseDTO(1, "User1", List.of()), new UserResponseDTO(2, "User2", List.of()));
        when(dataProcessingService.getAllOrders()).thenReturn(users);

        ResponseEntity<List<UserResponseDTO>> response = searchController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void shouldReturnNoContentWhenNoOrdersAvailable() {
        when(dataProcessingService.getAllOrders()).thenReturn(List.of());

        ResponseEntity<List<UserResponseDTO>> response = searchController.getAllOrders();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldReturnOrdersByDateRangeSuccessfully() {
        List<UserResponseDTO> users = List.of(new UserResponseDTO(1, "User1", List.of()));
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(dataProcessingService.findOrdersByDateRange(startDate, endDate)).thenReturn(users);

        ResponseEntity<List<UserResponseDTO>> response = searchController.getOrdersByDateRange(startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void shouldReturnNoContentWhenNoOrdersFoundInDateRange() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(dataProcessingService.findOrdersByDateRange(startDate, endDate)).thenReturn(List.of());

        ResponseEntity<List<UserResponseDTO>> response = searchController.getOrdersByDateRange(startDate, endDate);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}