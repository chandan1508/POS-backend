package com.chandan.pos.modal;

import java.time.LocalDateTime;

import com.chandan.pos.domain.PaymentType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    private String reason;

    private Double amount;

    @ManyToOne
    @JsonIgnore
    private ShiftReport shiftReport;

    @ManyToOne
    private User cashier;

    @ManyToOne
    private Branch branch;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}