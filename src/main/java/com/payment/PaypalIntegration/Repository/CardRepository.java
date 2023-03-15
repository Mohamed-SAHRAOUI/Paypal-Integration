package com.payment.PaypalIntegration.Repository;

import com.payment.PaypalIntegration.Entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String number);
}
