package com.digitalbank.account.controller;

import com.digitalbank.account.mapper.account.AccountMapper;
import com.digitalbank.account.model.dto.*;
import com.digitalbank.account.model.entity.Account;
import com.digitalbank.account.model.entity.AccountStatus;
import com.digitalbank.account.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;


    @PostMapping("/create")
    public ResponseEntity<AccountRs> createAccount (@RequestBody CreateAccountRq dto) {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }

    @PatchMapping("/{accountId}/status")
    public ResponseEntity<AccountRs> updateAccountStatus(
            @PathVariable Long accountId,
            @RequestParam AccountStatus status) {
        AccountRs account = accountService.updateAccountStatus(accountId, status);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    public void transferMoney(@Valid @RequestBody TransferRq request) {
        accountService.moneyTransfer(request);
    }
    @PostMapping("/deposit")
    public ResponseEntity<DepositRs> deposit(@Valid @RequestBody DepositRq request) {
        log.info("Deposit request received: accountId={}, amount={}",
                request.getAccountId(), request.getAmount());

        DepositRs response = accountService.processDeposit(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<AccountRs>> getAllAccounts(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = Sort.by( //сортировка стабильного порядка
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy
        );

        Page<Account> accountPage = accountService.getAccountsByUserId(
                userId,
                PageRequest.of(page, size, sort)
        );

        return ResponseEntity.ok(accountPage.map(accountMapper::toDto));
    }
}