package com.digitalbank.account.controller;

import com.digitalbank.account.mapper.account.AccountMapper;
import com.digitalbank.account.model.dto.AccountRs;
import com.digitalbank.account.model.dto.CreateAccountRq;
import com.digitalbank.account.model.dto.TransferRq;
import com.digitalbank.account.model.dto.TransferRs;
import com.digitalbank.account.model.entity.Account;
import com.digitalbank.account.model.entity.AccountStatus;
import com.digitalbank.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
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
    public ResponseEntity<TransferRs> transferMoney(@Valid @RequestBody TransferRq request) {
        accountService.moneyTransfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount()
        );
        return ResponseEntity.ok(new TransferRs("SUCCESS", "Transfer completed successfully"));
    }

}