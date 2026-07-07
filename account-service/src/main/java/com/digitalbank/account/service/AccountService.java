package com.digitalbank.account.service;

import com.digitalbank.account.mapper.account.AccountMapper;
import com.digitalbank.account.model.dto.AccountRs;
import com.digitalbank.account.model.dto.CreateAccountRq;
import com.digitalbank.account.model.entity.Account;
import com.digitalbank.account.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountRs createAccount(CreateAccountRq dto) {
        Account account = accountMapper.toEntity(dto);
        Account saveAccount = accountRepository.save(account);
        return accountMapper.toDto(saveAccount);
    }
}