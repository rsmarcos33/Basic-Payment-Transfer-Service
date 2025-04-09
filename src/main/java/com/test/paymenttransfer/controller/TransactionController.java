package com.test.paymenttransfer.controller;

import com.test.paymenttransfer.dto.TransactionDto;
import com.test.paymenttransfer.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Get transactions by account ID", description = "Returns a list of transactions for a specific account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/account/{id}")
    public ResponseEntity<List<TransactionDto>> getByAccount(@PathVariable Long id) {
        log.info(String.format("Request: Find transactions by account id = %d", id));
        return ResponseEntity.ok(transactionService.getByAccount(id));
    }

    @Operation(summary = "Get transactions in a date range", description = "Returns a list of transactions for an account within the specified date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getBetween(
            @RequestParam("accountId") Long accountId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        log.info(String.format("Request: Find transactions by account id = %d between dates %s and %s", accountId, start.toString(), end.toString()));
        return ResponseEntity.ok(transactionService.getBetween(accountId, start, end));
    }

}
