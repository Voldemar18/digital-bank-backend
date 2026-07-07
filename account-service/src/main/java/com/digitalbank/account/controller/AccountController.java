package com.digitalbank.account.controller;

import com.digitalbank.account.model.dto.AccountRs;
import com.digitalbank.account.model.dto.CreateAccountRq;
import com.digitalbank.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountRs> createAccount (@RequestBody CreateAccountRq dto) {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }
}