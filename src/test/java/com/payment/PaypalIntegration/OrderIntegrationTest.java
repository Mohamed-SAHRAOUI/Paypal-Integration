package com.payment.PaypalIntegration;

import com.payment.PaypalIntegration.Service.PaypalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PaypalService paypalService;

    @Test
    void createOrder_shouldReturnSuccess() throws Exception {
        String expectedResponse = "someOrderString";
        when(paypalService.createOrder()).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void createOrder_shouldReturnInternalServerError() throws Exception {
        String expectedErrorMessage = "some error message";
        when(paypalService.createOrder()).thenThrow(new Exception(expectedErrorMessage));

        mockMvc.perform(post("/api/orders"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedErrorMessage));
    }
}
