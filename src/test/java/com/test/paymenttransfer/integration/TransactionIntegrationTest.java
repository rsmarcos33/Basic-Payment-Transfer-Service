package com.test.paymenttransfer.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.paymenttransfer.model.Transaction;
import com.test.paymenttransfer.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldFetchTransactionsByAccountId() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setSourceId(1L);
        transaction.setDestinationId(2L);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        mockMvc.perform(get("/transactions/account/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sourceId").value(1L))
                .andExpect(jsonPath("$[0].amount").value(100));
    }

    @Test
    void shouldFetchTransactionsByDateRange() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setSourceId(1L);
        transaction.setDestinationId(2L);
        transaction.setAmount(BigDecimal.valueOf(200));
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        LocalDate today = LocalDate.now();

        mockMvc.perform(get("/transactions")
                        .param("accountId", "1")
                        .param("start", today.minusDays(1).toString())
                        .param("end", today.plusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sourceId").value(1L))
                .andExpect(jsonPath("$[0].amount").value(200));
    }

}

