package com.payment.PaypalIntegration;

import com.payment.PaypalIntegration.Controllers.OrderController;
import com.payment.PaypalIntegration.Service.PaypalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderTest {
    @Mock
    private PaypalService paypalService;

    private OrderController orderController;

    @BeforeEach
    public void setup() {
        orderController = new OrderController(paypalService);
    }

    @Test
    public void testCreateOrderSuccess() throws Exception {
        String expectedOrder = "order-123";
        when(paypalService.createOrder()).thenReturn(expectedOrder);

        ResponseEntity<String> response = orderController.createOrder();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOrder, response.getBody());
    }

    @Test
    public void testCreateOrderError() throws Exception {
        String expectedErrorMessage = "Une erreur est survenue";
        when(paypalService.createOrder()).thenThrow(new Exception(expectedErrorMessage));

        ResponseEntity<String> response = orderController.createOrder();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedErrorMessage, response.getBody());
    }
}
