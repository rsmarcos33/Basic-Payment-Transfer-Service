package com.test.paymenttransfer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long sourceId;

    @NotNull
    private Long destinationId;

    @Min(20)
    private BigDecimal amount;

    @NotNull
    private LocalDateTime timestamp;

    public Transaction(Long sourceId, Long destinationId, BigDecimal amount, LocalDateTime timestamp) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

}
