package com.digitalbank.account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepositRs {
    private Long accountId;
    private BigDecimal newBalance;
    private String status;
    private String message;
}