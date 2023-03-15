package com.payment.PaypalIntegration.Entity;

import javax.persistence.*;

@Entity
@Table(name = "billingAddress")
public class BillingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "addressLine1")
    private String addressLine1;

    @Column(name = "adminArea2")
    private String adminArea2;

    @Column(name = "postalCode")
    private String postalCode;

    @Column(name = "countryCode")
    private String countryCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAdminArea2() {
        return adminArea2;
    }

    public void setAdminArea2(String adminArea2) {
        this.adminArea2 = adminArea2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
