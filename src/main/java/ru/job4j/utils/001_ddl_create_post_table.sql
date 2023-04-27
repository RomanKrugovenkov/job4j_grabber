CREATE TABLE IF NOT EXISTS post
(
    id      serial primary key,
    name    text,
    text    text,
    link    text,
    created timestamp,
    CONSTRAINT post_unique UNIQUE (name)
);