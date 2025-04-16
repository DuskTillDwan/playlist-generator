CREATE SCHEMA IF NOT EXISTS users;

CREATE TABLE IF NOT EXISTS users.chat_members ( --
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,        -- Extracted from 'sender_name'
    facebook_id TEXT UNIQUE NULL -- Optional (if you extract IDs later)
);