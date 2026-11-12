package com.digitalbank.account.controller;

import com.digitalbank.account.mapper.account.AccountMapper;
import com.digitalbank.account.model.dto.*;
import com.digitalbank.account.model.entity.Account;
import com.digitalbank.account.model.entity.AccountStatus;
import com.digitalbank.account.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountController {

    private static final String UUID_REGEX =
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";//проверка валидности UUID

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping("/create")
    public ResponseEntity<AccountRs> createAccount(@Valid @RequestBody CreateAccountRq dto) {
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
    public ResponseEntity<Void> transferMoney(
            @RequestHeader("X-Idempotency-Key")
            @NotBlank(message = "X-Idempotency-Key must not be blank")
            @Pattern(regexp = UUID_REGEX, message = "X-Idempotency-Key must be a valid UUID")
            String idempotencyKey,
            @Valid @RequestBody TransferRq request) {

        log.info("Transfer request received: from={}, to={}, amount={}, idempotencyKey={}",
                request.getFromAccountId(), request.getToAccountId(),
                request.getAmount(), idempotencyKey);

        accountService.moneyTransfer(request, idempotencyKey);
        return ResponseEntity.ok().build();
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

        Sort sort = Sort.by(
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