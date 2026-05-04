package org.example.beam.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.beam.enumeration.PaymentMethod;
import org.example.beam.enumeration.PurchaseStatus;

import java.math.BigDecimal;


public record CreatePurchaseDto(

     BigDecimal pricePaid,
     PaymentMethod paymentMethod,
     PurchaseStatus status,
     Long userId,
     Long gameId
)
{}
