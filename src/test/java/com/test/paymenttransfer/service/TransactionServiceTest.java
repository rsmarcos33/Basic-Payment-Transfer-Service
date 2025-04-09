package com.test.paymenttransfer.service;

import com.test.paymenttransfer.dto.TransactionDto;
import com.test.paymenttransfer.mapper.TransactionMapper;
import com.test.paymenttransfer.model.Transaction;
import com.test.paymenttransfer.repository.TransactionRepository;
import com.test.paymenttransfer.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void shouldSaveTimestampRecordTransactionAndReturnDto() {
        TransactionDto inputDto = new TransactionDto();
        Transaction mappedEntity = new Transaction();
        Transaction savedEntity = new Transaction();
        TransactionDto expectedDto = new TransactionDto();

        when(transactionMapper.fromDtoToEntity(any(TransactionDto.class))).thenReturn(mappedEntity);
        when(transactionRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(transactionMapper.fromEntityToDto(savedEntity)).thenReturn(expectedDto);

        TransactionDto result = transactionService.recordTransaction(inputDto);

        assertNotNull(result);
        verify(transactionMapper).fromDtoToEntity(argThat(dto -> dto.getTimestamp() != null));
        verify(transactionRepository).save(mappedEntity);
        verify(transactionMapper).fromEntityToDto(savedEntity);
    }

    @Test
    void shouldReturnTransactionsByAccount() {
        Long accountId = 1L;
        List<Transaction> entityList = List.of(new Transaction(), new Transaction());
        List<TransactionDto> dtoList = List.of(new TransactionDto(), new TransactionDto());

        when(transactionRepository.findBySourceIdOrDestinationId(accountId, accountId)).thenReturn(entityList);
        when(transactionMapper.fromEntityListToDtoList(entityList)).thenReturn(dtoList);

        List<TransactionDto> result = transactionService.getByAccount(accountId);

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(dtoList);
    }

    @Test
    void shouldReturnTransactionsBetweenDates() {
        Long accountId = 1L;
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        List<Transaction> entityList = List.of(new Transaction());
        List<TransactionDto> dtoList = List.of(new TransactionDto());

        when(transactionRepository.findAllByAccountAndDateRange(accountId, startDateTime, endDateTime)).thenReturn(entityList);
        when(transactionMapper.fromEntityListToDtoList(entityList)).thenReturn(dtoList);

        List<TransactionDto> result = transactionService.getBetween(accountId, start, end);

        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(dtoList);
    }

}

