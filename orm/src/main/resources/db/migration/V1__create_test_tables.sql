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

CREATE TABLE phone
(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    phone_number varchar(20) NOT NULL,
    CONSTRAINT phone_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE UNIQUE INDEX phone_user_id_uindex ON phone (user_id);
CREATE UNIQUE INDEX phone_phone_uindex ON phone (phone_number);

CREATE TABLE roles
(
  id UUID PRIMARY KEY,
  name varchar(255) NOT NULL
);
CREATE UNIQUE INDEX roles_name_uindex ON roles (name);

CREATE TABLE role_user
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT role_user_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT role_user_roles_id_fk FOREIGN KEY (role_id) REFERENCES roles (id)
);