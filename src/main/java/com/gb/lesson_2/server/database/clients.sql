create table clients
(
    id             int auto_increment primary key,
    login          varchar(20)   not null,
    password       varchar(20)   not null,
    name           varchar(30)   null,
    session_status int default 2 not null,
    constraint users_login_uindex
        unique (login),
    constraint user_session_statuses___fk
        foreign key (session_status) references client_session_statuses (id)
            on update cascade on delete cascade
);