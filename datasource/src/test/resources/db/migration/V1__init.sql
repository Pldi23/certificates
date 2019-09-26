create table certificate
(
    id               bigint not null,
    name             varchar(200),
    description      varchar(1000),
    price            numeric,
    creationDate     timestamp,
    modificationDate timestamp,
    expirationDate   timestamp,


    constraint certificate_pkey1 PRIMARY KEY (id)
--     constraint unique_id UNIQUE (id)
);

create table tag
(
    id    bigint not null,
    title varchar(200),

    constraint tag_pkey1 primary key (id)
--     constraint unique_id UNIQUE (id)
);

create table certificate_tag
(
    certificate_id integer,
    tag_id        integer,

    constraint foreign_key_certificate_id foreign key (certificate_id)
        references certificate (id),
    constraint foreign_key_tag_id foreign key (tag_id)
        references tag (id)
);

insert into certificate(id, name, description, price, creationDate, modificationDate, expirationDate)
    values (1, 'sport car', '1 hour lamborghini ride', 250, '2019-01-01', '2019-01-06', '2021-01-01'),
           (2, 'bike', '1 hour yamaha ride', 120, '2019-01-03', '2019-01-07', '2021-01-01'),
           (3, 'skydiving', '1 jump', 100, '2019-01-05', '2019-01-08', '2021-01-01');

insert into tag(id, title)
    values (1, 'extreme'), (2, 'emotions'), (3, 'luxury'), (4, 'bike'), (5, 'car'), (6, 'air'), (7, 'jump');

insert into certificate_tag(certificate_id, tag_id)
    values (1, 3),
           (1, 1),
           (1, 5),
           (2, 1),
           (2, 2),
           (2, 4),
           (3, 1),
           (3, 2),
           (3, 6),
           (3, 7);
