package org.example.beam.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.beam.enumeration.PaymentMethod;
import org.example.beam.enumeration.PurchaseStatus;

import java.math.BigDecimal;

@Setter
@Getter
public class CreatePurchaseDto {

    private BigDecimal pricePaid;
    private PaymentMethod paymentMethod;
    private PurchaseStatus status;
    private Long userId;
    private Long gameId;


}
