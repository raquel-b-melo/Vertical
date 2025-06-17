package com.rmelo.vertical.application.service;

import com.rmelo.vertical.core.domain.model.Order;
import com.rmelo.vertical.core.domain.model.User;
import com.rmelo.vertical.core.domain.model.dto.UserResponseDTO;
import com.rmelo.vertical.core.domain.repository.OrderRepository;
import com.rmelo.vertical.core.domain.repository.UserRepository;
import com.rmelo.vertical.infrastructure.parser.TxtParser;
import com.rmelo.vertical.shared.utils.ResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataProcessingServiceTest {

    @Mock
    private TxtParser txtParser;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DataProcessingService dataProcessingService;

    @BeforeEach
    void setUp() {
        dataProcessingService = new DataProcessingService(txtParser, userRepository, orderRepository);
    }

    @Test
    void shouldProcessAndSaveFileSuccessfully() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "user1,user2".getBytes(StandardCharsets.UTF_8));

        List<User> parsedUsers = List.of(new User(1, "User1"), new User(2, "User2"));

        when(txtParser.processFile(any())).thenReturn(parsedUsers);

        List<User> result = dataProcessingService.processAndSaveFile(file);

        assertEquals(parsedUsers, result);
        verify(userRepository, times(1)).saveAll(parsedUsers);
    }

    @Test
    void shouldReturnEmptyWhenFileIsEmpty() {
        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        assertThrows(IllegalArgumentException.class, () -> dataProcessingService.processAndSaveFile(file));
    }

    @Test
    void shouldFindOrderByOrderIdSuccessfully() {
        Order mockOrder = new Order(1001, LocalDate.now(), new User(1, "User1"));

        when(orderRepository.findByOrderId(1001)).thenReturn(Optional.of(mockOrder));

        try (MockedStatic<ResponseMapper> mockedMapper = Mockito.mockStatic(ResponseMapper.class)) {
            mockedMapper.when(() -> ResponseMapper.toUserOneOrderDTO(mockOrder.getUser(), 1001))
                    .thenReturn(new UserResponseDTO(1, "User1", List.of()));

            Optional<UserResponseDTO> response = dataProcessingService.findOrderByOrderId(1001);

            assertTrue(response.isPresent());
            assertEquals(1, response.get().userId());
            assertEquals("User1", response.get().name());
        }
    }

}