package com.payment.PaypalIntegration.Dto;

import com.payment.PaypalIntegration.Entity.OrderStatus;
import lombok.Data;

import java.util.List;
public class OrderResponseDto {
    private String id;
    private OrderStatus status;
    private List<LinkDto> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<LinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDto> links) {
        this.links = links;
    }
}
