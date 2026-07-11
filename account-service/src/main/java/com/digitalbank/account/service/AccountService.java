package com.digitalbank.account.service;

import com.digitalbank.account.mapper.account.AccountMapper;
import com.digitalbank.account.model.dto.AccountRs;
import com.digitalbank.account.model.dto.CreateAccountRq;
import com.digitalbank.account.model.entity.Account;
import com.digitalbank.account.model.entity.AccountStatus;
import com.digitalbank.account.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    @Transactional
    public AccountRs updateAccountStatus(Long accountId, AccountStatus newStatus) {
        Account account = accountRepository.getOne(accountId);
        account.setStatus(newStatus);
        Account updateAccount = accountRepository.save(account);
        return accountMapper.toDto(updateAccount);
    }

    @Transactional
    public void moneyTransfer(Long fromAccountId,Long toAccountId,BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("amount must be greater than zero");
        }
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new EntityNotFoundException("Sender account not found"));
        Account toAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new EntityNotFoundException("Receiver account not found"));

        if(fromAccount.getBalance().compareTo(amount)<0){
            throw new IllegalArgumentException("amount must be greater than zero");
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

    }
}