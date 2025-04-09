package com.test.paymenttransfer.service;

import com.test.paymenttransfer.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TransactionService {

    TransactionDto recordTransaction(TransactionDto dto);
    List<TransactionDto> getByAccount(Long accountId);
    List<TransactionDto> getBetween(Long id, LocalDate start, LocalDate end);

}
