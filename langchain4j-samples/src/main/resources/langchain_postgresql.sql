CREATE TABLE ai_chat_conversation(
    conversation_id CHAR(36) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ai_chat_conversation_user_idx ON ai_chat_conversation(user_id);