package com.digitalbank.account.service;

import com.digitalbank.account.exception.AccountFrozenException;
import com.digitalbank.account.exception.AccountNotFoundException;
import com.digitalbank.account.exception.InsufficientFundsException;
import com.digitalbank.account.exception.PaymentSystemException;
import com.digitalbank.account.mapper.account.AccountMapper;
import com.digitalbank.account.model.dto.*;
import com.digitalbank.account.model.entity.Account;
import com.digitalbank.account.model.entity.AccountStatus;
import com.digitalbank.account.repository.AccountRepository;
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
    private final CurrencyConverterService currencyConverterService;

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
    public void moneyTransfer(TransferRq request) {
        BigDecimal amount = request.getAmount();

        Account fromAccount = findAccountOrThrow(request.getFromAccountId(), "Sender account not found");
        Account toAccount = findAccountOrThrow(request.getToAccountId(), "Receiver account not found");

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient balance");
        }

        if (fromAccount.getCurrency() != toAccount.getCurrency()) {
            BigDecimal convertedAmount = currencyConverterService.convert(amount, fromAccount.getCurrency(), toAccount.getCurrency());

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));

            toAccount.setBalance(toAccount.getBalance().add(convertedAmount));

        } else {
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));
        }
    }

    private Account findAccountOrThrow(Long accountId, String errorMessage) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(errorMessage));
    }

    @Transactional
    public DepositRs processDeposit(DepositRq request) {
        log.info("Processing deposit: accountId={}, amount={}",
                request.getAccountId(), request.getAmount());

        Account account = findAccountOrThrow(
                request.getAccountId(),
                "Account not found for deposit"
        );

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new AccountFrozenException("Cannot deposit to blocked account");
        }

        boolean paymentSuccess = simulateBankPayment(request.getAmount());

        if (paymentSuccess) {
            account.setBalance(account.getBalance().add(request.getAmount()));
            Account updatedAccount = accountRepository.save(account);
            log.info("Deposit successful. Account {} new balance: {}",
                    updatedAccount.getId(), updatedAccount.getBalance());

            return new DepositRs(
                    updatedAccount.getId(),
                    updatedAccount.getBalance(),
                    "SUCCESS",
                    "Deposit completed successfully"
            );
        } else {
            log.error("Deposit failed for account {} due to bank error", request.getAccountId());
            throw new PaymentSystemException("Payment processing failed due to bank error. Please try again later.");
        }
    }

    // Симулятор банковского платежа(85 на 15)
    private boolean simulateBankPayment(BigDecimal amount) {
        int random = (int) (Math.random() * 100);

        boolean success = random < 85;

        if (success) {
            log.info("Bank payment approved: amount={}", amount);
        } else {
            log.warn("Bank payment failed: amount={}, random={}", amount, random);
        }

        return success;
    }
}