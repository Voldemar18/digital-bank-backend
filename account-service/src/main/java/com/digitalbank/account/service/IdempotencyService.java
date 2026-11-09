package com.digitalbank.account.service;

import com.digitalbank.account.model.entity.IdempotencyKey;
import com.digitalbank.account.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdempotencyService {

    private static final int TTL_HOURS = 24;

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    /**
     * Проверяет, был ли ключ уже использован за последние 24 часа.
     * Если да — возвращает true (повтор), если нет — сохраняет ключ и возвращает false
     * */
    @Transactional
    public boolean checkAndStore(String idempotencyKey) {
        OffsetDateTime threshold = OffsetDateTime.now().minusHours(TTL_HOURS);

        if (idempotencyKeyRepository.existsByKeyAndCreatedAtAfter(idempotencyKey, threshold)) {
            log.warn("Duplicate transfer detected by idempotency key: {}", idempotencyKey);
            return true;
        }

        IdempotencyKey entity = new IdempotencyKey(
                idempotencyKey,
                200,
                OffsetDateTime.now()
        );
        idempotencyKeyRepository.save(entity);
        log.info("Idempotency key stored: {}", idempotencyKey);
        return false;
    }
}