CREATE SCHEMA IF NOT EXISTS music;

CREATE TABLE IF NOT EXISTS music.platforms (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

-- Insert platforms only if they don't exist
INSERT INTO music.platforms (name)
VALUES ('SPOTIFY'), ('YOUTUBE'), ('SOUNDCLOUD')
ON CONFLICT (name) DO NOTHING;


