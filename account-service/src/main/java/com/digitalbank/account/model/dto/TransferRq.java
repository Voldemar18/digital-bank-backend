package com.digitalbank.account.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRq {
    @NotNull(message = "Sender account ID is required")
    private Long fromAccountId;

    @NotNull(message = "Receiver account ID is required")
    private Long toAccountId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}