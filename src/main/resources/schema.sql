CREATE TABLE IF NOT EXISTS USERS
(
    USERNAME   varchar(100) not null,
    PASSWORD varchar(100) not null,
    ENABLE    boolean      not null
);

CREATE TABLE IF NOT EXISTS IMAGES
(
    ID          int          not null AUTO_INCREMENT,
    NAME        varchar(100) not null,
    DESCRIPTION varchar(255),
    DATA        blob,
    PRIMARY KEY (ID)
);