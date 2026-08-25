package com.digitalbank.account.model.dto;

import com.digitalbank.account.model.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRq {
    private Long userId;
    private Currency currency;
    private BigDecimal balance;
}