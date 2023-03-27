package com.payment.PaypalIntegration.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.PaypalIntegration.Dto.ConfirmPaymentBodyDto;
import com.payment.PaypalIntegration.Dto.OrderResponseDto;
import com.payment.PaypalIntegration.Entity.BillingAddress;
import com.payment.PaypalIntegration.Entity.Card;
import com.payment.PaypalIntegration.Entity.Order;
import com.payment.PaypalIntegration.Repository.BillingAddressRepository;
import com.payment.PaypalIntegration.Repository.CardRepository;
import com.payment.PaypalIntegration.Repository.OrderRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Date;

@Service
public class PaypalService {
    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secret;

    @Value("${paypal.base}")
    private String base;

    @Value("${paypal.confirmOrder}")
    private String confirmOrderUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    private final CardRepository cardRepository;

    private final BillingAddressRepository billingAddressRepository;


    public PaypalService(RestTemplateBuilder restTemplateBuilder,
                         OrderRepository orderRepository,
                         ObjectMapper objectMapper,
                         CardRepository cardRepository,
                         BillingAddressRepository billingAddressRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.cardRepository = cardRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public String createOrder() throws Exception {
        String purchaseAmount = "45.00"; // TODO: pull price from a database
        String currencyCode = "USD";
        String accessToken = generateAccessToken();
        String url = base + "/v2/checkout/orders";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        JSONObject body = new JSONObject();
        body.put("intent", "CAPTURE");
        JSONArray purchaseUnits = new JSONArray();
        JSONObject purchaseUnit = new JSONObject();
        JSONObject amount = new JSONObject();
        amount.put("currency_code", currencyCode);
        amount.put("value", purchaseAmount);
        purchaseUnit.put("amount", amount);
        purchaseUnits.put(purchaseUnit);
        body.put("purchase_units", purchaseUnits);

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        OrderResponseDto orderResponseDto = objectMapper.readValue(response.getBody(), OrderResponseDto.class);
        Order order = new Order();
        order.setPaypalOrderId(orderResponseDto.getId());
        order.setPaypalOrderStatus(orderResponseDto.getStatus());
        order.setCreateDate(new Date());
        order.setAmountValue(purchaseAmount);
        order.setCurrencyCode(currencyCode);
        saveOrder(order);

        return handleResponse(response);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public String capturePayment(String orderId) throws Exception {
        String accessToken = generateAccessToken();
        String url = base + "/v2/checkout/orders/" + orderId + "/capture";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        Order order = updateOrder(response, orderId);
        saveOrder(order);

        return handleResponse(response);
    }

    private Order updateOrder(ResponseEntity<String> response, String orderId) throws JsonProcessingException {
        OrderResponseDto orderResponseDto = objectMapper.readValue(response.getBody(), OrderResponseDto.class);
        Order order = orderRepository.findByPaypalOrderId(orderId);
        order.setPaypalOrderStatus(orderResponseDto.getStatus());
        return order;
    }

    public ResponseEntity<String> confirmPayment(String orderId, ConfirmPaymentBodyDto confirmPaymentBodyDto) throws Exception {
        String url = confirmOrderUrl + orderId + "/confirm-payment-source";

        String accessToken = generateAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<ConfirmPaymentBodyDto> request = new HttpEntity<>(confirmPaymentBodyDto, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        Order order = updateOrder(response, orderId);
        order.setPayWithCreditCard(true);
        saveOrder(order);
        saveCardDetails(confirmPaymentBodyDto);

        return response;
    }

    private void saveCardDetails(ConfirmPaymentBodyDto confirmPaymentBodyDto) {
        Card card = objectMapper.convertValue(confirmPaymentBodyDto.getPayment_source().getCard(), Card.class);
        if (this.cardRepository.findByNumber(card.getNumber()) == null) {
            BillingAddress billingAddress = card.getBillingAddress();
            billingAddress = this.billingAddressRepository.save(billingAddress);
            card.setBillingAddress(billingAddress);
            this.cardRepository.save(card);
        }
    }

    private String generateAccessToken() throws Exception {
        String auth = Base64.getEncoder().encodeToString((clientId + ":" + secret).getBytes("UTF-8"));
        String url = base + "/v1/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(auth);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        JSONObject jsonData = new JSONObject(response.getBody());
        return jsonData.getString("access_token");
    }

    private String handleResponse(ResponseEntity<String> response) throws Exception {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        throw new Exception(response.getBody());
    }
}
