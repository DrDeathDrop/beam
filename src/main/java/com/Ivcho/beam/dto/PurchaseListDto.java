package com.Ivcho.beam.dto;


import com.Ivcho.beam.enumeration.PaymentMethod;
import com.Ivcho.beam.enumeration.PurchaseStatus;
import java.math.BigDecimal;

public record PurchaseListDto(
        BigDecimal pricePaid,
        PaymentMethod paymentMethod,
        PurchaseStatus status,
        String gameName
) {}
