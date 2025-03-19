CREATE TABLE ai_conversation(
    conversation_id VARCHAR(50) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted INT NOT NULL DEFAULT 0
);

CREATE INDEX ai_conversation_user_id ON ai_conversation(deleted, user_id, conversation_id);


CREATE TABLE ai_chat_message(
    id BIGINT NOT NULL,
    conversation_id VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    role VARCHAR(20) NOT NULL,
    "timestamp" BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX at_chat_message_conversation_id ON ai_chat_message(conversation_id);
