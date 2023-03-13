package com.payment.PaypalIntegration.Controllers;

import com.payment.PaypalIntegration.Dto.ConfirmPaymentBodyDto;
import com.payment.PaypalIntegration.Service.PaypalService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api")
public class OrderController {

    private final PaypalService paypalService;

    public OrderController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }


    @PostMapping("/orders")
    public ResponseEntity<String> createOrder() {
        try {
            String order = paypalService.createOrder();
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/orders/{orderID}/capture")
    public ResponseEntity<String> capturePayment(@PathVariable String orderID) {
        try {
            String captureData = paypalService.capturePayment(orderID);
            return ResponseEntity.ok(captureData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/orders/{orderId}/confirm-payment")
    public ResponseEntity<String> confirmPayment(@PathVariable String orderId, @RequestBody ConfirmPaymentBodyDto confirmPaymentBodyDto) throws Exception {
        try {
            ResponseEntity<String> response = paypalService.confirmPayment(orderId, confirmPaymentBodyDto);
            return response;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
