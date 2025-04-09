package com.test.paymenttransfer.mapper;

import com.test.paymenttransfer.dto.AccountDto;
import com.test.paymenttransfer.model.Account;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapper {

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    public AccountDto fromEntityToDto(Account entity) {
        AccountDto dto = dozerBeanMapper.map(entity, AccountDto.class);
        return dto;
    }

    public Account fromDtoToEntity(AccountDto dto) {
        Account entity = dozerBeanMapper.map(dto, Account.class);
        return entity;
    }

    public List<AccountDto> fromEntityListToDtoList(List<Account> entityList) {
        if (entityList == null) return Collections.emptyList();
        return entityList.stream()
                .map(this::fromEntityToDto)
                .collect(Collectors.toList());
    }
    
}
