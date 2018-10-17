CREATE TABLE users
(
    id UUID PRIMARY KEY,
    name varchar(255) NOT NULL,
    email varchar(255)
);
CREATE UNIQUE INDEX users_email_uindex ON users (email);

CREATE TABLE posts
(
    id UUID PRIMARY KEY,
    title varchar(255) NOT NULL,
    slug varchar(255) NOT NULL,
    content text,
    user_id UUID NOT NULL,
    CONSTRAINT posts_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE UNIQUE INDEX posts_slug_uindex ON posts (slug);
