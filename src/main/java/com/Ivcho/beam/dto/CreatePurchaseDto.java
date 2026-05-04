package com.Ivcho.beam.dto;

import com.Ivcho.beam.enumeration.PaymentMethod;
import com.Ivcho.beam.enumeration.PurchaseStatus;

import java.math.BigDecimal;


public record CreatePurchaseDto(

     BigDecimal pricePaid,
     PaymentMethod paymentMethod,
     PurchaseStatus status,
     Long userId,
     Long gameId
)
{}
