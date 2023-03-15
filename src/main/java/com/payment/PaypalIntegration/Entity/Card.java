package com.payment.PaypalIntegration.Entity;

import javax.persistence.*;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "expiry")
    private String expiry;

    @Column(name = "securityCode")
    private String securityCode;

    @Column(name = "name")
    private String name;

//    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private BillingAddress billingAddress;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billingAddress_id", referencedColumnName = "id")
    private BillingAddress billingAddress;

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

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }
}
