create table certificate
(
    id               serial,
    name             varchar(200),
    description      varchar(1000),
    price            numeric,
    creationDate     date,
    modificationDate date,
    expirationDate   date,
--
    active_status    boolean,
--

    constraint certificate_pkey1 PRIMARY KEY (id)
);

create table tag
(
    id    serial,
    title varchar(200),

    constraint tag_pkey1 primary key (id),
    constraint tag_pkey2 primary key (title)
);

create table certificate_tag
(
    certificate_id integer,
    tag_id         integer,

    constraint foreign_key_certificate_id foreign key (certificate_id)
        references certificate (id),
    constraint foreign_key_tag_id foreign key (tag_id)
        references tag (id)
);

create table application_role
(
    id   serial,
    role varchar(200),

    CONSTRAINT role_pkey1 PRIMARY KEY (id)
);

create table application_user
(
    id            serial,
    email         varchar(200),
    password      varchar(200),
    role_id       integer,
    refresh_token varchar(1000),

    constraint user_pkey1 primary key (id),
    CONSTRAINT foreign_key_role_id FOREIGN KEY (role_id)
        REFERENCES application_role (id)
);

CREATE TABLE application_order
(
    id          serial,
    user_id     integer,
    created_at  timestamp,
--
    paid_status boolean,
--

    constraint order_pkey1 PRIMARY KEY (id),
    constraint foreign_key_user_email foreign key (user_id) references application_user (id)

);



create table order_certificate
(
    order_id        integer,
    certificate_id  integer,
--
    fixed_price     numeric,
    expiration_date date,
--

    constraint foreign_key_certificate_id foreign key (certificate_id)
        references certificate (id),
    constraint foreign_key_order_id foreign key (order_id)
        references application_order (id)
);

insert into application_role (role)
values ('ROLE_ADMIN'),
       ('ROLE_USER'),
       ('ROLE_GUEST');
