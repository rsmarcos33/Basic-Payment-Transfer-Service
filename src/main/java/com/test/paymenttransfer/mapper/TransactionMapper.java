package com.test.paymenttransfer.mapper;

import com.test.paymenttransfer.dto.TransactionDto;
import com.test.paymenttransfer.model.Transaction;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    public TransactionDto fromEntityToDto(Transaction entity) {
        TransactionDto dto = dozerBeanMapper.map(entity, TransactionDto.class);
        return dto;
    }

    public Transaction fromDtoToEntity(TransactionDto dto) {
        Transaction entity = dozerBeanMapper.map(dto, Transaction.class);
        return entity;
    }

    public List<TransactionDto> fromEntityListToDtoList(List<Transaction> entityList) {
        if (entityList == null) return Collections.emptyList();
        return entityList.stream()
                .map(this::fromEntityToDto)
                .collect(Collectors.toList());
    }

}
