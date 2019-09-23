create table certificate
(
    name             varchar(200),
    description      varchar(1000),
    price            numeric,
    creationDate     timestamp,
    modificationDate timestamp,
    expirationDate   timestamp,


    constraint certificate_pkey1 PRIMARY KEY (name),
    constraint unique_name UNIQUE (name)
);

create table tag
(
    title varchar(200),

    constraint tag_pkey1 primary key (title),
    constraint unique_title UNIQUE (title)
);

create table certificate_tag
(
    certificate_name varchar(200),
    tag_title        varchar(200),

    constraint foreign_key_certificate_name foreign key (certificate_name)
        references certificate (name) match simple,
    constraint foreign_key_tag_title foreign key (tag_title)
        references tag (title) match simple
)
