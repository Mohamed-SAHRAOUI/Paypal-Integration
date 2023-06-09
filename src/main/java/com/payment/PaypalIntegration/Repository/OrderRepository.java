package com.payment.PaypalIntegration.Repository;

import com.payment.PaypalIntegration.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByPaypalOrderId(String paypalOrderId);
}
