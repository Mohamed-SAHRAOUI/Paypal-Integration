package com.payment.PaypalIntegration;

import com.payment.PaypalIntegration.Dto.CardDto;
import com.payment.PaypalIntegration.Dto.ConfirmPaymentBodyDto;
import com.payment.PaypalIntegration.Dto.PaymentSourceDto;
import com.payment.PaypalIntegration.Entity.Order;
import com.payment.PaypalIntegration.Repository.BillingAddressRepository;
import com.payment.PaypalIntegration.Repository.CardRepository;
import com.payment.PaypalIntegration.Repository.OrderRepository;
import com.payment.PaypalIntegration.Service.PaypalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureMockMvc
public class PaypalServiceIntegrationTest {

    private final String accessToken = "ACCESS_TOKEN";
    private final String orderId = "ORDER_ID";
    private final String status = "COMPLETED";
    private final String base = "https://api-m.sandbox.paypal.com";

    private final String confirmOrderUrl = "https://cors.api.sandbox.paypal.com/v2/checkout/orders/";

    @Autowired
    private PaypalService paypalService;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private CardRepository cardRepository;
    @MockBean
    private BillingAddressRepository billingAddressRepository;



    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void generateAccessTokenTest() throws Exception {
        //Create Access Token
        createAccessToken();

        // call the method being tested
        String accessToken = paypalService.generateAccessToken();

        // verify that the method returned the expected access token
        assertEquals("ACCESS_TOKEN", accessToken);

        // Verify the REST endpoint was called as expected
        mockServer.verify();
    }

    @Test
    public void createOrderTest() throws Exception {
        //Create Access Token
        createAccessToken();

        // Mock the REST endpoint
        mockServer.expect(requestTo("https://api-m.sandbox.paypal.com/v2/checkout/orders"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header("Authorization", "Bearer " + accessToken))
                .andRespond(MockRestResponseCreators.withSuccess("{\"id\": \"" + orderId + "\", \"status\": \"" + status + "\"}", MediaType.APPLICATION_JSON));

        // Invoke the method being tested
        String result = paypalService.createOrder();

        // Verify the result
        assertEquals("{\"id\": \"" + orderId + "\", \"status\": \"" + status + "\"}", result);

        // Verify the REST endpoint was called as expected
        mockServer.verify();
    }

    @Test
    public void capturePaymentTest() throws Exception {
        //Create Access Token
        createAccessToken();

        // setup
        String url = base + "/v2/checkout/orders/" + orderId + "/capture";
        String responseBody = "{ \"status\": \"COMPLETED\" }";
        Order order = new Order();
        when(orderRepository.findByPaypalOrderId(orderId)).thenReturn(order);

        mockServer.expect(requestTo(url))
                .andExpect(header("Authorization", "Bearer " + accessToken))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        // execution
        String result = paypalService.capturePayment(orderId);

        // verification
        mockServer.verify();

        assertEquals("{ \"status\": \"COMPLETED\" }", result);
    }

    @Test
    public void testConfirmPayment() throws Exception {
        //Create Access Token
        createAccessToken();

        // Given
        ConfirmPaymentBodyDto confirmPaymentBodyDto = new ConfirmPaymentBodyDto();
        PaymentSourceDto paymentSourceDto = new PaymentSourceDto();
        CardDto cardDto = new CardDto();
        cardDto.setName("TEST test");
        cardDto.setNumber("1234-5678-9012-3456");
        cardDto.setExpiry("12/24");
        cardDto.setSecurityCode("123");
        paymentSourceDto.setCard(cardDto);
        confirmPaymentBodyDto.setPayment_source(paymentSourceDto);

        Order order = new Order();
        when(orderRepository.findByPaypalOrderId(orderId)).thenReturn(order);


        mockServer.expect(requestTo(confirmOrderUrl + orderId + "/confirm-payment-source"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(jsonPath("$.payment_source.card.name").value(confirmPaymentBodyDto.getPayment_source().getCard().getName()))
                .andExpect(jsonPath("$.payment_source.card.number").value(confirmPaymentBodyDto.getPayment_source().getCard().getNumber()))
                .andExpect(jsonPath("$.payment_source.card.expiry").value(confirmPaymentBodyDto.getPayment_source().getCard().getExpiry()))
                .andExpect(jsonPath("$.payment_source.card.securityCode").value(confirmPaymentBodyDto.getPayment_source().getCard().getSecurityCode()))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        // When
        ResponseEntity<String> response = paypalService.confirmPayment(orderId, confirmPaymentBodyDto);

        // Then
        mockServer.verify();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private void createAccessToken() {
        // expected response body
        String responseBody = "{ \"access_token\": \"ACCESS_TOKEN\" }";


        // mock the postForEntity method to return the response entity
        mockServer.expect(requestTo(base + "/v1/oauth2/token"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
    }
}
