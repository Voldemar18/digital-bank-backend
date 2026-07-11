package com.digitalbank.account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferRs {
    private String status;
    private String message;
}