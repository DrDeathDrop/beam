package com.Ivcho.beam.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.Ivcho.beam.enumeration.PaymentMethod;
import com.Ivcho.beam.enumeration.PurchaseStatus;

import java.math.BigDecimal;


@Setter
@Getter
@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal pricePaid;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

}