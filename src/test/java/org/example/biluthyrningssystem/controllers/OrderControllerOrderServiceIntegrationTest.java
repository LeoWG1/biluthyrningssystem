package org.example.biluthyrningssystem.controllers;

import jakarta.transaction.Transactional;
import org.example.biluthyrningssystem.models.entities.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Frida Jakobsson
 */
@SpringBootTest
@Transactional
@Rollback
class OrderControllerOrderServiceIntegrationTest {

    private final OrderController orderController;

    @Autowired
    OrderControllerOrderServiceIntegrationTest(OrderController orderController) {
        this.orderController = orderController;
    }

    @Test
    void getAllOrders_returnsUserOrders() {
        Principal mockPrincipal = () -> "19950505-7890";

        var response = orderController.getAllOrders(mockPrincipal);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<Order> orders = response.getBody();
        assertThat(orders).isNotNull();
    }

    @Test
    void getAllOrders_returnsAdminOrders() {
        var response = orderController.getAllOrdersAdmin();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<Order> orders = response.getBody();
        assertThat(orders).isNotNull();
    }

    @Test
    void removeOrderById_deletesOrderFromDatabase() {
        List<Order> allOrders = orderController.getAllOrdersAdmin().getBody();
        assertThat(allOrders).isNotEmpty();
        long idToDelete = allOrders.get(0).getId();

        var deleteResponse = orderController.removeOrderByID(idToDelete);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getStatistics_returnsCorrectStatistics() {
        var response = orderController.showStatistics();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var stats = response.getBody();
        assertThat(stats).isNotNull();
        assertThat(stats.getTotalOrders()).isGreaterThanOrEqualTo(0);
        assertThat(stats.getTotalActiveOrders()).isBetween(0L, stats.getTotalOrders());
        assertThat(stats.getTotalRevenue()).isGreaterThanOrEqualTo(0L);
    }
}
