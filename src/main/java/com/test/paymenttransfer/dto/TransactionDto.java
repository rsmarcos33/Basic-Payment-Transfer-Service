package com.test.paymenttransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private Long id;

    private Long sourceId;

    private Long destinationId;

    private BigDecimal amount;

    private LocalDateTime timestamp;

}
