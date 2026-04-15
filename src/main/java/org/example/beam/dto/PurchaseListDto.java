package org.example.beam.dto;

import org.example.beam.model.PaymentMethod;
import org.example.beam.model.PurchaseStatus;

import java.math.BigDecimal;

public class PurchaseListDto {
    private BigDecimal pricePaid;
    private PaymentMethod paymentMethod;
    private PurchaseStatus status;
    private String gameName;

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    public BigDecimal getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(BigDecimal pricePaid) {
        this.pricePaid = pricePaid;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
