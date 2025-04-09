package com.test.paymenttransfer.service;

import com.test.paymenttransfer.dto.TransactionDto;
import org.springframework.stereotype.Service;

@Service
public interface TransferService {

    TransactionDto transferFunds(TransactionDto transactionDto);

}
