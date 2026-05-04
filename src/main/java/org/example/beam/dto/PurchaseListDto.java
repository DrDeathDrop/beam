package org.example.beam.dto;


import org.example.beam.enumeration.PaymentMethod;
import org.example.beam.enumeration.PurchaseStatus;
import java.math.BigDecimal;

public record PurchaseListDto(
        BigDecimal pricePaid,
        PaymentMethod paymentMethod,
        PurchaseStatus status,
        String gameName
) {}
