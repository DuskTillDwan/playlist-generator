CREATE SCHEMA IF NOT EXISTS music;

CREATE TABLE IF NOT EXISTS music.song (
    id SERIAL PRIMARY KEY,
    platform_id INT REFERENCES music.platforms(id),
    title TEXT,
    url TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT NOW()
);
