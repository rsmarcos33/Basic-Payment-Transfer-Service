package com.test.paymenttransfer.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.paymenttransfer.dto.TransactionDto;
import com.test.paymenttransfer.model.Account;
import com.test.paymenttransfer.repository.AccountRepository;
import com.test.paymenttransfer.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TransferIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void shouldTransferFundsSuccessfully() throws Exception {
        Account sourceAccount = new Account(null, BigDecimal.valueOf(500));
        Account destinationAccount = new Account(null, BigDecimal.valueOf(100));
        sourceAccount = accountRepository.save(sourceAccount);
        destinationAccount = accountRepository.save(destinationAccount);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSourceId(sourceAccount.getId());
        transactionDto.setDestinationId(destinationAccount.getId());
        transactionDto.setAmount(BigDecimal.valueOf(100));

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sourceId").value(sourceAccount.getId()));
    }

    @Test
    void shouldReturnBadRequestForInvalidTransaction() throws Exception {
        Account sourceAccount = new Account(null, BigDecimal.valueOf(500));
        Account destinationAccount = new Account(null, BigDecimal.valueOf(100));
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSourceId(sourceAccount.getId());
        transactionDto.setDestinationId(sourceAccount.getId());
        transactionDto.setAmount(BigDecimal.valueOf(100));

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForInsufficientFunds() throws Exception {
        Account sourceAccount = new Account(null, BigDecimal.valueOf(50));
        Account destinationAccount = new Account(null, BigDecimal.valueOf(100));
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSourceId(sourceAccount.getId());
        transactionDto.setDestinationId(destinationAccount.getId());
        transactionDto.setAmount(BigDecimal.valueOf(100));

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isBadRequest());
    }

}

