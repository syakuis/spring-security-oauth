create table account
(
    id            bigint auto_increment
        primary key,
    blocked       bit          not null,
    deleted       bit          not null,
    disabled      bit          not null,
    name          varchar(45)  not null,
    password      varchar(150) not null,
    registered_on datetime(6)  not null,
    uid           binary(16)   not null,
    updated_on    datetime(6)  null,
    username      varchar(150) not null,
    constraint UK_account_uid_1
        unique (uid),
    constraint UK_account_username_1
        unique (username)
);

create index IDX_account_blocked_and_deleted_1
    on account (blocked, deleted);

create index IDX_account_deleted_1
    on account (deleted);

create index IDX_account_disabled_and_deleted_1
    on account (disabled, deleted);

create index IDX_account_uid_and_deleted_1
    on account (uid, deleted);

create index IDX_account_username_and_deleted_1
    on account (username, deleted);

