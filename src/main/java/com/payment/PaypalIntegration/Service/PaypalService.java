package com.payment.PaypalIntegration.Service;

import com.payment.PaypalIntegration.Dto.ConfirmPaymentBody;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
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

@Service
public class PaypalService {
//    @Value("${paypal.clientId}")
//    private String clientId;
//
//    @Value("${paypal.secret}")
//    private String secret;
//
//    @Value("${paypal.base}")
//    private String base;
//
//    @Value("${paypal.confirmOrder}")
//    private String confirmOrderUrl;
//
//    private final RestTemplate restTemplate;
//
//    public PaypalService() {
//        restTemplate = new RestTemplate();
//    }
//
//
//    public String createOrder() throws Exception {
//        String purchaseAmount = "45.00"; // TODO: pull price from a database
//        String accessToken = generateAccessToken();
//        String url = base + "/v2/checkout/orders";
//        JSONObject body = new JSONObject();
//        body.put("intent", "CAPTURE");
//        JSONArray purchaseUnits = new JSONArray();
//        JSONObject purchaseUnit = new JSONObject();
//        JSONObject amount = new JSONObject();
//        amount.put("currency_code", "USD");
//        amount.put("value", purchaseAmount);
//        purchaseUnit.put("amount", amount);
//        purchaseUnits.put(purchaseUnit);
//        body.put("purchase_units", purchaseUnits);
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost(url);
//        StringEntity params = new StringEntity(body.toString());
//        request.addHeader("content-type", "application/json");
//        request.addHeader("Authorization", "Bearer " + accessToken);
//        request.setEntity(params);
//        CloseableHttpResponse response = httpClient.execute(request);
//        return handleResponseWithHttpResponse(response);
//    }
//
//    private String generateAccessToken() throws Exception {
//        String auth = Base64.getEncoder().encodeToString((clientId + ":" + secret).getBytes("UTF-8"));
//        String url = base + "/v1/oauth2/token";
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost(url);
//        request.addHeader("Authorization", "Basic " + auth);
//        request.addHeader("content-type", "application/x-www-form-urlencoded");
//        request.setEntity(new StringEntity("grant_type=client_credentials"));
//        CloseableHttpResponse response = httpClient.execute(request);
//        JSONObject jsonData = new JSONObject(EntityUtils.toString(response.getEntity()));
//        return jsonData.getString("access_token");
//    }
//
//    private String handleResponseWithHttpResponse(CloseableHttpResponse response) throws Exception {
//        int status = response.getStatusLine().getStatusCode();
//        if (status == 200 || status == 201) {
//            String responseString = EntityUtils.toString(response.getEntity());
//            JSONObject jsonResponse = new JSONObject(responseString);
//            return jsonResponse.toString();
//        } else {
//            String errorMessage = EntityUtils.toString(response.getEntity());
//            throw new Exception(errorMessage);
//        }
//    }
//
//    public String capturePayment(String orderId) throws Exception {
//        String accessToken = generateAccessToken();
//        String url = base + "/v2/checkout/orders/" + orderId + "/capture";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<String> request = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//        return handleResponseWithResponseEntity(response);
//    }
//
//    private String handleResponseWithResponseEntity(ResponseEntity<String> response) throws Exception {
//        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
//            return response.getBody();
//        }
//
//        String errorMessage = response.getBody();
//        throw new Exception(errorMessage);
//    }
//
//    public ResponseEntity<String> confirmPayment(String orderId, ConfirmPaymentBody confirmPaymentBody) throws Exception {
//        String url = confirmOrderUrl + orderId +"/confirm-payment-source";
//
//        String accessToken = generateAccessToken();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//
//        HttpEntity<ConfirmPaymentBody> request = new HttpEntity<>(confirmPaymentBody, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//        return response;
//    }
    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secret;

    @Value("${paypal.base}")
    private String base;

    @Value("${paypal.confirmOrder}")
    private String confirmOrderUrl;

    private final RestTemplate restTemplate;

    public PaypalService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String createOrder() throws Exception {
        String purchaseAmount = "45.00"; // TODO: pull price from a database
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
        amount.put("currency_code", "USD");
        amount.put("value", purchaseAmount);
        purchaseUnit.put("amount", amount);
        purchaseUnits.put(purchaseUnit);
        body.put("purchase_units", purchaseUnits);

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return handleResponse(response);
    }

    public String capturePayment(String orderId) throws Exception {
        String accessToken = generateAccessToken();
        String url = base + "/v2/checkout/orders/" + orderId + "/capture";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return handleResponse(response);
    }

    public ResponseEntity<String> confirmPayment(String orderId, ConfirmPaymentBody confirmPaymentBody) throws Exception {
        String url = confirmOrderUrl + orderId + "/confirm-payment-source";

        String accessToken = generateAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<ConfirmPaymentBody> request = new HttpEntity<>(confirmPaymentBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response;
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
