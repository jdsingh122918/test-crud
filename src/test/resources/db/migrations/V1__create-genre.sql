create sequence "hibernate_sequence" start 1;
create table genre
(
    id   bigint primary key not null,
    name varchar(255)       not null
)