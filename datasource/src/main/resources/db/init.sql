create table certificate
(
    id               bigint not null,
    name             varchar(200),
    description      varchar(1000),
    price            numeric,
    creationDate     date,
    modificationDate date,
    expirationDate   date,


    constraint certificate_pkey1 PRIMARY KEY (id),
    constraint unique_id UNIQUE (id)
);

create table tag
(
    id    bigint not null,
    title varchar(200),

    constraint tag_pkey1 primary key (id),
    constraint unique_id UNIQUE (id)
);

create table certificate_tag
(
    certificate_id integer,
    tag_id        integer,

    constraint foreign_key_certificate_id foreign key (certificate_id)
        references certificate (id) match simple,
    constraint foreign_key_tag_id foreign key (tag_id)
        references tag (id) match simple
)
