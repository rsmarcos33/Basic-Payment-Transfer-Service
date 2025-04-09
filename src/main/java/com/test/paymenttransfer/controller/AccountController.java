package com.test.paymenttransfer.controller;

import com.test.paymenttransfer.dto.AccountDto;
import com.test.paymenttransfer.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Get account by ID", description = "Returns a single account by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getById(@PathVariable Long id) {
        log.info(String.format("Request: Find Account by id = %d", id));
        return ResponseEntity.ok(accountService.getById(id));
    }

    @Operation(summary = "Create new account", description = "Creates an account and returns the created AccountDto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<AccountDto> create(@RequestBody @Valid AccountDto dto) {
        log.info("Request: Create account");
        AccountDto createdDto = accountService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    @Operation(summary = "Update new account", description = "Update an account and returns the updated AccountDto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> update(@PathVariable("id") Long id, @RequestBody @Valid AccountDto dto) {
        log.info("Request: Update account");
        AccountDto updatedDto = accountService.update(dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

}
