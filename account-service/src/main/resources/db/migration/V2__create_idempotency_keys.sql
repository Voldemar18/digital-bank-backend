CREATE TABLE idempotency_keys (
    key             VARCHAR(36) PRIMARY KEY,
    response_status INTEGER     NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_idempotency_keys_created_at ON idempotency_keys (created_at);