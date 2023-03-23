package com.payment.PaypalIntegration.Entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "paypal_order_id")
    private String paypalOrderId;

    @Column(name = "paypal_order_status")
    private OrderStatus paypalOrderStatus;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "amount_value")
    private String amountValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaypalOrderId() {
        return paypalOrderId;
    }

    public void setPaypalOrderId(String paypalOrderId) {
        this.paypalOrderId = paypalOrderId;
    }

    public OrderStatus getPaypalOrderStatus() {
        return paypalOrderStatus;
    }

    public void setPaypalOrderStatus(OrderStatus paypalOrderStatus) {
        this.paypalOrderStatus = paypalOrderStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(String amountValue) {
        this.amountValue = amountValue;
    }
}
