package com.digitalbank.account.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusRs {
    private Long id;
    private String status; // "ACTIVE", "BLOCKED", "BANNED"
}