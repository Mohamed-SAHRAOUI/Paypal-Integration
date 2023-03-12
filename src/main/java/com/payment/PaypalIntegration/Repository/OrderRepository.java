package com.payment.PaypalIntegration.Repository;

import com.payment.PaypalIntegration.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
