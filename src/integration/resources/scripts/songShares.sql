CREATE SCHEMA IF NOT EXISTS music;

CREATE TABLE IF NOT EXISTS music.song (
    id SERIAL PRIMARY KEY,
    platform_id INT REFERENCES music.platforms(id),
    title TEXT,
    url TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS music.song_shares (
    user_id INT REFERENCES users.chat_members(id),
    song_id INT REFERENCES music.song(id),
    shared_at TIMESTAMP NOT NULL,
    PRIMARY KEY (song_id, user_id, shared_at)
);

