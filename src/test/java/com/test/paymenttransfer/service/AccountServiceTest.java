package com.test.paymenttransfer.service;

import com.test.paymenttransfer.dto.AccountDto;
import com.test.paymenttransfer.exception.AccountNotFoundException;
import com.test.paymenttransfer.exception.InvalidInputParameterException;
import com.test.paymenttransfer.mapper.AccountMapper;
import com.test.paymenttransfer.model.Account;
import com.test.paymenttransfer.repository.AccountRepository;
import com.test.paymenttransfer.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void shouldReturnAccountById() {
        Long id = 1L;
        Account account = new Account(id, BigDecimal.valueOf(1000));
        AccountDto accountDto = new AccountDto(id, BigDecimal.valueOf(1000));

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountMapper.fromEntityToDto(account)).thenReturn(accountDto);

        AccountDto result = accountService.getById(id);

        assertThat(result).isEqualTo(accountDto);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundById() {
        Long id = 1L;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getById(id))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found with id: " + id);
    }

    @Test
    void shouldReturnAccountByIdForUpdate() {
        Long id = 1L;
        Account account = new Account(id, BigDecimal.valueOf(1000));
        AccountDto accountDto = new AccountDto(id, BigDecimal.valueOf(1000));

        when(accountRepository.findByIdForUpdate(id)).thenReturn(Optional.of(account));
        when(accountMapper.fromEntityToDto(account)).thenReturn(accountDto);

        AccountDto result = accountService.getByIdForUpdate(id);

        assertThat(result).isEqualTo(accountDto);
    }

    @Test
    void shouldCreateAccountWithDefaultBalance() {
        AccountDto inputDto = new AccountDto(null, BigDecimal.valueOf(1000));
        Account entityToSave = new Account(null, BigDecimal.valueOf(1000));
        entityToSave.setBalance(BigDecimal.ZERO);

        Account savedEntity = new Account(1L, BigDecimal.valueOf(1000));
        savedEntity.setBalance(BigDecimal.ZERO);

        AccountDto expectedDto = new AccountDto(1L, BigDecimal.valueOf(1000));

        when(accountMapper.fromDtoToEntity(inputDto)).thenReturn(entityToSave);
        when(accountRepository.save(entityToSave)).thenReturn(savedEntity);
        when(accountMapper.fromEntityToDto(savedEntity)).thenReturn(expectedDto);

        AccountDto result = accountService.create(inputDto);

        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void shouldThrowExceptionOnUpdateWhenIdIsNull() {
        AccountDto dto = new AccountDto();
        dto.setId(null);

        assertThrows(InvalidInputParameterException.class, () -> {
            accountService.update(dto);
        });

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(accountMapper);
    }

    @Test
    void shouldUpdateAndReturnDtoWhenInputIsValid() {
        Long id = 1L;
        AccountDto inputDto = new AccountDto();
        inputDto.setId(id);

        Account entity = new Account();
        Account savedEntity = new Account();
        AccountDto outputDto = new AccountDto();
        outputDto.setId(id);

        when(accountMapper.fromDtoToEntity(inputDto)).thenReturn(entity);
        when(accountRepository.save(entity)).thenReturn(savedEntity);
        when(accountMapper.fromEntityToDto(savedEntity)).thenReturn(outputDto);

        AccountDto result = accountService.update(inputDto);

        assertNotNull(result);
        assertEquals(id, result.getId());

        verify(accountMapper).fromDtoToEntity(inputDto);
        verify(accountRepository).save(entity);
        verify(accountMapper).fromEntityToDto(savedEntity);
    }

}
