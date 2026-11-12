package com.digitalbank.account.repository;

import com.digitalbank.account.model.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {

    Optional<IdempotencyKey> findByKeyAndCreatedAtAfter(String key, OffsetDateTime threshold);

    boolean existsByKeyAndCreatedAtAfter(String key, OffsetDateTime threshold);
}