package com.digitalbank.account.model.dto;

import com.digitalbank.account.model.entity.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEvent {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private Currency currency;
    private OffsetDateTime date;
}