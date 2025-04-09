package com.test.paymenttransfer.service;

import com.test.paymenttransfer.dto.AccountDto;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    AccountDto getById(Long id);

    AccountDto getByIdForUpdate(Long id);

    AccountDto create(AccountDto account);

    AccountDto update(AccountDto account);

}
