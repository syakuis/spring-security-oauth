create table client_registration
(
    id                      bigint auto_increment
        primary key,
    access_token_validity   int          not null,
    additional_information  varchar(255) null,
    authorities             varchar(255) null,
    authorized_grant_types  varchar(255) not null,
    autoapprove             varchar(255) null,
    client_id               varchar(255) not null,
    client_secret           varchar(255) not null,
    refresh_token_validity  int          not null,
    registered_by           varchar(255) not null,
    registered_on           datetime     not null,
    resource_ids            varchar(255) null,
    scope                   varchar(255) not null,
    updated_on              datetime     null,
    web_server_redirect_uri varchar(255) null,
    constraint UK_qurrt8bt8njjfk3b1864k0d2d
        unique (client_id)
);

