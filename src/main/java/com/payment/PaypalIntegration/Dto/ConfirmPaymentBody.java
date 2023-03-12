package com.payment.PaypalIntegration.Dto;

public class ConfirmPaymentBody {
    private PaymentSourceDto payment_source;

    public PaymentSourceDto getPayment_source() {
        return payment_source;
    }

    public void setPayment_source(PaymentSourceDto payment_source) {
        this.payment_source = payment_source;
    }
}
