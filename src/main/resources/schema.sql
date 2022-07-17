create TABLE IF NOT EXISTS USERS
(
    USER_ID   LONG PRIMARY KEY AUTO_INCREMENT,
    EMAIL     VARCHAR(255) NOT NULL,
    LOGIN     VARCHAR(255) NOT NULL,
    USER_NAME VARCHAR(255) NOT NULL,
    BIRTHDAY  DATE
);

create TABLE IF NOT EXISTS FILMS
(
    FILM_ID       LONG PRIMARY KEY AUTO_INCREMENT,
    NAME          VARCHAR(255) NOT NULL,
    DESCRIPTION   VARCHAR(255) NOT NULL,
    RELEASE_DATE  DATE,
    DURATION      INT          NOT NULL,
    MPA_RATING_ID INT
);

create table if not exists MPA_RATINGS
(
    MPA_RATING_ID int not null primary key auto_increment,
    NAME          varchar(255),
    constraint MPA_PK primary key (MPA_RATING_ID)
);

create table if not exists GENRES
(
    GENRE_ID int not null primary key auto_increment,
    NAME     varchar(255),
    constraint GENRES_PK primary key (GENRE_ID)
);

create table if not exists FRIENDSHIPS
(
    USER_ID   long not null REFERENCES USERS (USER_ID),
    FRIEND_ID long not null REFERENCES USERS (USER_ID),
    CONSTRAINT FRIENDSHIPS_PK PRIMARY KEY (USER_ID, FRIEND_ID)
);

create table if not exists FILM_GENRES
(
    FILM_ID  long references FILMS (FILM_ID),
    GENRE_ID int references GENRES (GENRE_ID),
    PRIMARY KEY (FILM_ID, GENRE_ID)
);

create table if not exists LIKES
(
    FILM_ID  long references FILMS (FILM_ID),
    LIKE_ID int references USERS (USER_ID),
    PRIMARY KEY (FILM_ID, LIKE_ID)
);