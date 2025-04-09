package com.test.paymenttransfer.service.impl;

import com.test.paymenttransfer.dto.AccountDto;
import com.test.paymenttransfer.exception.AccountNotFoundException;
import com.test.paymenttransfer.exception.InvalidInputParameterException;
import com.test.paymenttransfer.mapper.AccountMapper;
import com.test.paymenttransfer.model.Account;
import com.test.paymenttransfer.repository.AccountRepository;
import com.test.paymenttransfer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public AccountDto getById(Long id) {
        AccountDto dto = accountMapper.fromEntityToDto(accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id)));
        return dto;
    }

    @Override
    @Transactional
    public AccountDto getByIdForUpdate(Long id) {
        AccountDto dto = accountMapper.fromEntityToDto(accountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id)));
        return dto;
    }

    @Override
    @Transactional
    public AccountDto create(AccountDto dto) {
        if (dto.getId()!=null)
            throw new InvalidInputParameterException("Account id must be null");
        Account entity = accountMapper.fromDtoToEntity(dto);
        Account createdEntity = accountRepository.save(entity);
        return accountMapper.fromEntityToDto(createdEntity);
    }

    @Override
    @Transactional
    public AccountDto update(AccountDto dto) {
        if (dto.getId()==null)
            throw new InvalidInputParameterException("Account id must not be null");
        Optional<Account> optional = accountRepository.findById(dto.getId());
        if (optional.isEmpty()) {
            throw new AccountNotFoundException("Account with provided id not found");
        }
        Account entity = accountMapper.fromDtoToEntity(dto);
        Account updatedEntity = accountRepository.save(entity);
        return accountMapper.fromEntityToDto(updatedEntity);
    }

}
