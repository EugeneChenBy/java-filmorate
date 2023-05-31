drop table if exists film cascade;
create table if not exists film
(
    film_id       int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    name          varchar(64) not null,
    description   varchar(500),
    creation_date date,
    duration      int,
    mpa	          int,
    last_update   timestamp
);

drop table if exists mpa cascade;
create table if not exists mpa
(
    mpa_id        int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    name          varchar(64) not null,
    last_update   timestamp
);

drop table if exists genre cascade;
create table if not exists genre
(
    genre_id      int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    name          varchar(64) not null,
    last_update   timestamp
);

drop table if exists film_genre;
create table if not exists film_genre
(
    film_id       int,
    genre_id      int,
    last_update   timestamp,
    CONSTRAINT fk_film_id FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genre (genre_id),
    PRIMARY KEY (film_id, genre_id)
);

drop table if exists users cascade;
create table if not exists users
(
    user_id       int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    email         varchar(64),
    login         varchar(20),
    name          varchar(100),
    birthday      date,
    last_update   timestamp
);

drop table if exists friend cascade ;
create table if not exists friend
(
    user1_id      int,
    user2_id      int,
    status        int,
    last_update   timestamp,
    CONSTRAINT fk_frined1_id FOREIGN KEY (user1_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_frined2_id FOREIGN KEY (user2_id) REFERENCES users (user_id) ON DELETE CASCADE,
    PRIMARY KEY (user1_id, user2_id)
);

drop table if exists film_like cascade ;
create table if not exists film_like
(
    film_id      int,
    user_id      int,
    last_update   timestamp,
    CONSTRAINT fk_likefilm_id FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    CONSTRAINT fk_likeuser_id FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);