package com.test.paymenttransfer.service.impl;

import com.test.paymenttransfer.dto.TransactionDto;
import com.test.paymenttransfer.exception.AccountNotFoundException;
import com.test.paymenttransfer.exception.InsufficientFundsException;
import com.test.paymenttransfer.exception.InvalidInputParameterException;
import com.test.paymenttransfer.exception.TransferToSameAccountException;
import com.test.paymenttransfer.mapper.TransactionMapper;
import com.test.paymenttransfer.model.Account;
import com.test.paymenttransfer.repository.AccountRepository;
import com.test.paymenttransfer.repository.TransactionRepository;
import com.test.paymenttransfer.service.TransactionService;
import com.test.paymenttransfer.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionDto transferFunds(TransactionDto dto) throws InsufficientFundsException {
        Long sourceId = dto.getSourceId();
        Long destinationId = dto.getDestinationId();
        BigDecimal amount = dto.getAmount();

        if (dto.getSourceId()==null)
            throw new InvalidInputParameterException("Source account id must not be null");
        if (dto.getDestinationId()==null)
            throw new InvalidInputParameterException("Destination account id must not be null");

        if (sourceId.equals(destinationId)) {
            throw new TransferToSameAccountException("Cannot transfer to the same account");
        }

        Account source = accountRepository.findByIdForUpdate(sourceId)
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));

        Account destination = accountRepository.findByIdForUpdate(destinationId)
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));

        accountRepository.save(source);
        accountRepository.save(destination);

        TransactionDto createdTransaction = transactionService.recordTransaction(dto);
        return createdTransaction;
    }
}
