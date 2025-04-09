package com.test.paymenttransfer.controller;

import com.test.paymenttransfer.dto.TransactionDto;
import com.test.paymenttransfer.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@Slf4j
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Operation(
            summary = "Transfer funds",
            description = "Transfers funds from one account to another based on the provided transaction details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<TransactionDto> transfer(@RequestBody @Valid TransactionDto dto) {
        log.info(String.format("Request: Transfer money from account = %d to %d", dto.getSourceId(), dto.getDestinationId()));
        return ResponseEntity.ok(transferService.transferFunds(dto));
    }

}
