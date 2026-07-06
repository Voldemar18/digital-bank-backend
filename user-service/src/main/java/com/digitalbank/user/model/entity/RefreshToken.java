package com.digitalbank.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, name = "expiry_date")
    private Instant expiryDate;

    public boolean isExpired() {
        return this.expiryDate.isBefore(Instant.now());
    }
}