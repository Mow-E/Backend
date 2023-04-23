create table if not exists users
(
    username varchar_ignorecase(50)  not null primary key unique,
    password varchar_ignorecase(500) not null,
    enabled  boolean                 not null
        unique
);

create table if not exists authorities
(
    username  varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);

create table if not exists groups
(
    id         bigint generated by default as identity (start with 0) primary key,
    group_name varchar_ignorecase(50) not null
);

create table if not exists group_authorities
(
    group_id  bigint      not null,
    authority varchar(50) not null,
    constraint fk_group_authorities_group foreign key (group_id) references groups (id)
);

create table if not exists group_members
(
    id       bigint generated by default as identity (start with 0) primary key,
    username varchar(50) not null,
    group_id bigint      not null,
    constraint fk_group_members_group foreign key (group_id) references groups (id)
);