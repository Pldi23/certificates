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
        references certificate (name),
    constraint foreign_key_tag_title foreign key (tag_title)
        references tag (title)
);

insert into certificate(name, description, price, creationDate, modificationDate, expirationDate)
    values ('sport car', '1 hour lamborghini ride', 250, '2019-01-01', '2019-01-06', '2021-01-01'),
           ('bike', '1 hour yamaha ride', 120, '2019-01-03', '2019-01-07', '2021-01-01'),
           ('skydiving', '1 jump', 100, '2019-01-05', '2019-01-08', '2021-01-01');

insert into tag(title)
    values ('extreme'), ('emotions'), ('luxury'), ('bike'), ('car'), ('air'), ('jump');

insert into certificate_tag(certificate_name, tag_title)
    values ('sport car', 'luxury'),
           ('sport car', 'extreme'),
           ('sport car', 'car'),
           ('bike', 'extreme'),
           ('bike', 'emotions'),
           ('bike', 'bike'),
           ('skydiving', 'extreme'),
           ('skydiving', 'emotions'),
           ('skydiving', 'air'),
           ('skydiving', 'jump');
