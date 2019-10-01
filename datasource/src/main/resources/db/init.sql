create table certificate
(
    id               serial,
    name             varchar(200),
    description      varchar(1000),
    price            numeric,
    creationDate     date,
    modificationDate date,
    expirationDate   date,


    constraint certificate_pkey1 PRIMARY KEY (id)
);

create table tag
(
    id    serial,
    title varchar(200),

    constraint tag_pkey1 primary key (id)
);

create table certificate_tag
(
    certificate_id integer,
    tag_id        integer,

    constraint foreign_key_certificate_id foreign key (certificate_id)
        references certificate (id),
    constraint foreign_key_tag_id foreign key (tag_id)
        references tag (id)
)
