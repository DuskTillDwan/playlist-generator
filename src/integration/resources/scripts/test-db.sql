CREATE SCHEMA music;
CREATE SCHEMA users;


CREATE TABLE IF NOT EXISTS music.platforms (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

-- Insert platforms only if they don't exist
INSERT INTO music.platforms (name)
VALUES ('SPOTIFY'), ('YOUTUBE'), ('SOUNDCLOUD')
ON CONFLICT (name) DO NOTHING;


CREATE TABLE IF NOT EXISTS users.chat_members ( --
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,        -- Extracted from 'sender_name'
    facebook_id TEXT UNIQUE NULL -- Optional (if you extract IDs later)
);


CREATE TABLE IF NOT EXISTS users.logins (
    id SERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL
);

CREATE TABLE music.song (
    id SERIAL PRIMARY KEY,
    platform_id INT REFERENCES music.platforms(id),
    title TEXT NOT NULL,
    url TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS music.song_shares (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users.chat_members(id),
    song_id INT REFERENCES music.song(id),
    shared_at TIMESTAMP NOT NULL
);

CREATE TABLE music.playlists (
    id SERIAL PRIMARY KEY,
    platform INT REFERENCES music.platforms(id),
    name TEXT NOT NULL,
    external_id TEXT NOT NULL UNIQUE, -- Spotify/YouTube/SoundCloud playlist ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

CREATE TABLE music.playlist_song (
    playlist_id INT REFERENCES music.playlists(id) ON DELETE CASCADE,
    song_id INT REFERENCES music.song(id) ON DELETE CASCADE,
    PRIMARY KEY (playlist_id, song_id)
);

CREATE TABLE users.playlist_collaborator (
    playlist_id INT REFERENCES music.playlists(id) ON DELETE CASCADE,
    user_id INT REFERENCES users.chat_members(id) ON DELETE CASCADE,
    PRIMARY KEY (playlist_id, user_id)
);

