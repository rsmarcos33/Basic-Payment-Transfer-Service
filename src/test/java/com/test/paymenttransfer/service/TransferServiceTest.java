package com.test.paymenttransfer.service;

import com.test.paymenttransfer.dto.TransactionDto;
import com.test.paymenttransfer.exception.AccountNotFoundException;
import com.test.paymenttransfer.exception.InsufficientFundsException;
import com.test.paymenttransfer.exception.InvalidInputParameterException;
import com.test.paymenttransfer.exception.TransferToSameAccountException;
import com.test.paymenttransfer.model.Account;
import com.test.paymenttransfer.repository.AccountRepository;
import com.test.paymenttransfer.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransferServiceImpl transferService;

    private Account sourceAccount;
    private Account destinationAccount;
    private TransactionDto transactionDto;

    @BeforeEach
    void setUp() {
        sourceAccount = new Account(1L, BigDecimal.valueOf(1000));
        destinationAccount = new Account(2L, BigDecimal.valueOf(500));
        transactionDto = new TransactionDto(null, 1L, 2L, BigDecimal.valueOf(200), null);
    }

    @Test
    void shouldTransferFundsWhenValid() {
        TransactionDto inputDto = new TransactionDto();
        inputDto.setSourceId(sourceAccount.getId());
        inputDto.setDestinationId(destinationAccount.getId());
        inputDto.setAmount(transactionDto.getAmount());

        Account source = new Account();
        source.setId(sourceAccount.getId());
        source.setBalance(new BigDecimal("500.00"));

        Account destination = new Account();
        destination.setId(destinationAccount.getId());
        destination.setBalance(new BigDecimal("200.00"));

        TransactionDto recordedTransaction = new TransactionDto();

        when(accountRepository.findByIdForUpdate(sourceAccount.getId())).thenReturn(Optional.of(source));
        when(accountRepository.findByIdForUpdate(destinationAccount.getId())).thenReturn(Optional.of(destination));
        when(transactionService.recordTransaction(inputDto)).thenReturn(recordedTransaction);

        TransactionDto result = transferService.transferFunds(inputDto);

        assertEquals(recordedTransaction, result);
        assertEquals(new BigDecimal("300.00"), source.getBalance());
        assertEquals(new BigDecimal("400.00"), destination.getBalance());

        verify(accountRepository).save(source);
        verify(accountRepository).save(destination);
        verify(transactionService).recordTransaction(inputDto);
    }

    @Test
    void shouldTrowExceptionOnTransferFundsWhenSourceIdIsNull() {
        TransactionDto dto = new TransactionDto();
        dto.setDestinationId(destinationAccount.getId());
        dto.setAmount(transactionDto.getAmount());

        assertThrows(InvalidInputParameterException.class, () -> transferService.transferFunds(dto));
    }

    @Test
    void shouldThrowExceptionOnTransferFundsWhenDestinationIdIsNull() {
        TransactionDto dto = new TransactionDto();
        dto.setSourceId(sourceAccount.getId());
        dto.setAmount(transactionDto.getAmount());

        assertThrows(InvalidInputParameterException.class, () -> transferService.transferFunds(dto));
    }

    @Test
    void shouldThrowOnTransferFundsWhenSourceAndDestinationAreSame() {
        TransactionDto dto = new TransactionDto();
        dto.setSourceId(1L);
        dto.setDestinationId(1L);
        dto.setAmount(transactionDto.getAmount());

        assertThrows(TransferToSameAccountException.class, () -> transferService.transferFunds(dto));
    }

    @Test
    void shouldThrowOnTransferFundsWhenSourceAccountNotFound() {
        TransactionDto dto = new TransactionDto();
        dto.setSourceId(sourceAccount.getId());
        dto.setDestinationId(destinationAccount.getId());
        dto.setAmount(transactionDto.getAmount());

        when(accountRepository.findByIdForUpdate(sourceAccount.getId())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transferService.transferFunds(dto));
    }

    @Test
    void shouldThrowOnTransferFundsWhenDestinationAccountNotFound() {
        TransactionDto dto = new TransactionDto();
        dto.setSourceId(sourceAccount.getId());
        dto.setDestinationId(destinationAccount.getId());
        dto.setAmount(transactionDto.getAmount());

        Account source = new Account();
        source.setId(sourceAccount.getId());
        source.setBalance(new BigDecimal("300.00"));

        when(accountRepository.findByIdForUpdate(sourceAccount.getId())).thenReturn(Optional.of(source));
        when(accountRepository.findByIdForUpdate(destinationAccount.getId())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transferService.transferFunds(dto));
    }

    @Test
    void shouldThrowOnTransferFundsWhenInsufficientFunds() {
        TransactionDto dto = new TransactionDto();
        dto.setSourceId(sourceAccount.getId());
        dto.setDestinationId(destinationAccount.getId());
        dto.setAmount(new BigDecimal("1000.00"));

        Account source = new Account();
        source.setId(sourceAccount.getId());
        source.setBalance(new BigDecimal("300.00"));

        Account destination = new Account();
        destination.setId(destinationAccount.getId());
        destination.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findByIdForUpdate(sourceAccount.getId())).thenReturn(Optional.of(source));
        when(accountRepository.findByIdForUpdate(destinationAccount.getId())).thenReturn(Optional.of(destination));

        assertThrows(InsufficientFundsException.class, () -> transferService.transferFunds(dto));
    }

}

