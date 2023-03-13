package com.payment.PaypalIntegration.Entity;


import javax.persistence.*;

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
}
