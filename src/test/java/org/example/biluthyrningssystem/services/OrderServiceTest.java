package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.models.entities.Customer;
import org.example.biluthyrningssystem.models.entities.Order;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.example.biluthyrningssystem.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Frida Jakobsson
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private Car testCar;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCar = new Car();
        testCar.setId(1L);
        testCar.setPricePerDay(500);

        testCustomer = new Customer();
        testCustomer.setSocialSecurityNumber("19950505-7890");

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCar(testCar);
        testOrder.setCustomer(testCustomer);
        testOrder.setStartDate(LocalDate.now().plusDays(5));
        testOrder.setEndDate(LocalDate.now().plusDays(10));
    }

    @Test
    void createOrder_successfulCreation() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(testCar));
        when(orderRepository.findActiveOrdersWithin(eq(1L), any(), any())).thenReturn(Collections.emptyList());
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.createOrder(testOrder);

        assertNotNull(result);
        assertEquals(2500, result.getPrice()); // 500 * 5 dagar
        assertTrue(result.isActive());

        verify(orderRepository).save(result);
    }

    @Test
    void createOrder_invalidStartDate_throwsException() {
        testOrder.setStartDate(LocalDate.now().minusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(testOrder));
        assertEquals("Start date must be today or in the future", exception.getMessage());
    }

    @Test
    void createOrder_invalidEndDate_throwsException() {
        testOrder.setEndDate(LocalDate.now().minusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(testOrder));
        assertEquals("End date must be after start date", exception.getMessage());
    }

    @Test
    void createOrder_carNotFound_throwsException() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(testOrder));

        assertEquals("Car", exception.getItem());
    }

    @Test
    void createOrder_conflictingDates_throwsException() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(testCar));
        when(orderRepository.findActiveOrdersWithin(eq(1L), any(), any())).thenReturn(List.of(new Order()));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> orderService.createOrder(testOrder));

        assertEquals("Car isn't available during the selected dates", exception.getMessage());
    }

    @Test
    void getAllOrders_returnsAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(testOrder));

        List<Order> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(testOrder, result.get(0));
    }

    @Test
    void getOrderById_returnsCorrectOrder() {
        when(orderRepository.findOrderById(1L)).thenReturn(testOrder);

        Order result = orderService.getOrderById(1L);

        assertEquals(testOrder, result);
    }

    @Test
    void getAllActiveOrders_returnsActiveOrders() {
        testOrder.setActive(true);
        when(orderRepository.findByActiveTrue()).thenReturn(List.of(testOrder));

        List<Order> result = orderService.getAllActiveOrders();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }

    @Test
    void getAllInactiveOrders_returnsInactiveOrders() {
        testOrder.setActive(false);
        when(orderRepository.findByActiveFalse()).thenReturn(List.of(testOrder));

        List<Order> result = orderService.getAllInactiveOrders();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isActive());
    }

    @Test
    void getAllOrdersByUsername_returnsCorrectOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(testOrder));

        List<Order> result = orderService.getAllOrdersByUsername("19950505-7890");

        assertEquals(1, result.size());
        assertEquals(testOrder, result.get(0));
    }

    @Test
    void getAllActiveOrdersByUsername_returnsCorrectOrders() {
        testOrder.setActive(true);
        when(orderRepository.findAll()).thenReturn(List.of(testOrder));

        List<Order> result = orderService.getAllActiveOrdersByUsername("19950505-7890");

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }

    @Test
    void removeOrderById_deletesOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        orderService.removeOrderById(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void removeOrderBeforeDate_deletesOrdersBeforeDate() {
        when(orderRepository.findByEndDateBefore(any())).thenReturn(List.of(testOrder));

        orderService.removeOrderBeforeDate(LocalDate.now());

        verify(orderRepository).deleteAll(List.of(testOrder));
    }

    @Test
    void cancelOrder_setsOrderInactiveAndSaves() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(carRepository.save(any())).thenReturn(testCar);
        when(orderRepository.save(any())).thenReturn(testOrder);

        Order result = orderService.cancelOrder(1L);

        assertFalse(result.isActive());
    }

    @Test
    void getStatistics_returnsCorrectStats() {
        testOrder.setActive(true);
        testOrder.setPrice(1000);
        when(orderRepository.findAll()).thenReturn(List.of(testOrder));

        var stats = orderService.getStatistics();

        assertEquals(1, stats.getTotalOrders());
        assertEquals(1, stats.getTotalActiveOrders());
        assertEquals(1000, stats.getTotalRevenue());
    }

    @Test
    void getCarStatistics_returnsCorrectStats() {
        testOrder.setActive(true);
        testOrder.setPrice(2000);
        testOrder.setEndDate(LocalDate.now().plusDays(3));
        when(orderRepository.findByCarId(1L)).thenReturn(List.of(testOrder));

        var stats = orderService.getCarStatistics(1L);

        assertEquals(1, stats.getTotalOrders());
        assertEquals(1, stats.getTotalActiveOrders());
        assertEquals(2000, stats.getTotalRevenue());
        assertEquals(testOrder.getEndDate(), stats.getLatestOrder());
    }
}
