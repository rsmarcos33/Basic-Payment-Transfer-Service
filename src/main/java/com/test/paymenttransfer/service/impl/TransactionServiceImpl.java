package com.test.paymenttransfer.service.impl;

import com.test.paymenttransfer.dto.TransactionDto;
import com.test.paymenttransfer.exception.InvalidInputParameterException;
import com.test.paymenttransfer.mapper.TransactionMapper;
import com.test.paymenttransfer.model.Transaction;
import com.test.paymenttransfer.repository.TransactionRepository;
import com.test.paymenttransfer.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionDto recordTransaction(TransactionDto dto) {
        dto.setTimestamp(LocalDateTime.now());
        Transaction entity = transactionMapper.fromDtoToEntity(dto);
        Transaction createdEntity = transactionRepository.save(entity);
        return transactionMapper.fromEntityToDto(createdEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getByAccount(Long accountId) {
        List<Transaction> transactions = transactionRepository.findBySourceIdOrDestinationId(accountId, accountId);
        return transactionMapper.fromEntityListToDtoList(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getBetween(Long accountId, LocalDate start, LocalDate end) {
        if (accountId == null) {
            throw new InvalidInputParameterException("Account id must not be null");
        }
        if (start.isAfter(end)) {
            throw new InvalidInputParameterException("Start date must be before end date");
        } else {
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

            List<Transaction> allByAccountAndDateRange = transactionRepository.findAllByAccountAndDateRange(accountId, startDateTime, endDateTime);
            return transactionMapper.fromEntityListToDtoList(allByAccountAndDateRange);
        }
    }

}
