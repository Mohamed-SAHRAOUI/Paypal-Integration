package com.payment.PaypalIntegration.Dto;

public class CardDto {
    private String number;
    private String expiry;
    private String securityCode;
    private String name;
    private BillingAddressDto billingAddress;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BillingAddressDto getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddressDto billingAddress) {
        this.billingAddress = billingAddress;
    }
}
