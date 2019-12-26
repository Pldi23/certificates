create type tag_data as (t_title text);

create or replace function insert_certificates_list_and_return_boolean(name_in varchar(200),
                                                                       description_in varchar(1000),
                                                                       price_in numeric,
                                                                       creation_date_in date,
                                                                       modification_date_in date,
                                                                       expiration_date_in date,
                                                                       active_status_in boolean,
                                                                       tag_title_list tag_data[])
    returns BOOLEAN
    language 'plpgsql'
as
$BODY$
declare
    i            tag_data;
    temp_cert_id integer;
    temp_tag_id  integer;
begin
    insert into certificate(active_status, creation_date, description, expiration_date, modification_date, name, price)
    values (active_status_in, creation_date_in, description_in, expiration_date_in, modification_date_in, name_in,
            price_in) returning certificate.id
               into temp_cert_id;
    foreach i in array tag_title_list
        loop
            begin
                select tag.id from tag where title = i.t_title into temp_tag_id;
                if not FOUND then
                    insert into tag(title)
                    values (i.t_title) returning tag.id
                        into temp_tag_id;

                end if;
            exception when unique_violation then select tag.id from tag where title = i.t_title into temp_tag_id;
            end;
            insert into certificate_tag(certificate_id, tag_id) VALUES (temp_cert_id, temp_tag_id);
        end loop;
    return true;
exception
    WHEN unique_violation THEN
        return false;
end;
$BODY$;



create or replace function insert_certificates_list(name_in varchar(200),
                                                    description_in varchar(1000),
                                                    price_in numeric,
                                                    creation_date_in date,
                                                    modification_date_in date,
                                                    expiration_date_in date,
                                                    active_status_in boolean,
                                                    tag_title_list text)
    returns table
            (
                id                integer,
                name              varchar(200),
                description       varchar(1000),
                price             numeric,
                creation_date     date,
                modification_date date,
                expiration_date   date,
                active_status boolean
            )
    language 'plpgsql'
as
$BODY$
declare
    title_array  varchar(200)[];
    i            text;
    temp_cert_id integer;
    temp_tag_id  integer;
begin
    insert into certificate(active_status, creation_date, description, expiration_date, modification_date, name, price)
    values (active_status_in, creation_date_in, description_in, expiration_date_in, modification_date_in, name_in,
            price_in) returning certificate.id
               into temp_cert_id;
    select * into title_array from string_to_array(tag_title_list, ',');
    foreach i in array title_array
        loop
            select tag.id from tag where title = i into temp_tag_id;
            if not FOUND then
                insert into tag(title)
                values (i) returning tag.id
                    into temp_tag_id;
            end if;
            insert into certificate_tag(certificate_id, tag_id) VALUES (temp_cert_id, temp_tag_id);
        end loop;
    return query
        select * from certificate where certificate.id = temp_cert_id;
end;
$BODY$;

create or replace function insert_tag_list(tag_title_list text)
    returns table
            (
                id    integer,
                title varchar(200)
            )
    language 'plpgsql'
as
$BODY$
declare
    title_array varchar(500)[];
    i           text;
begin
    select * into title_array from string_to_array(tag_title_list, ',');
    FOREACH i IN ARRAY title_array
        loop
            return query
                select * from tag where tag.title = i;
            if not found then
                insert into tag (title) values (i);
                return query select * from tag where tag.title = i;
            end if;
        end loop;
end;
$BODY$;



CREATE OR REPLACE FUNCTION get_certificate_by_name_description(part_of_description varchar(200),
                                                               part_of_name varchar(200))
    RETURNS TABLE
            (
                id               integer,
                name             varchar(200),
                description      varchar(200),
                price            numeric,
                creationdate     date,
                modificationdate date,
                expirationdate   date,
                certificate_id   integer,
                tag_id           integer,
                t_id             integer,
                title            varchar(200)
            )
    LANGUAGE 'plpgsql'
AS
$BODY$
BEGIN
    RETURN QUERY
        SELECT *
        FROM certificate
                 join certificate_tag ct on certificate.id = ct.certificate_id
                 join tag t on ct.tag_id = t.id
        WHERE certificate.description like '%' || part_of_description || '%'
           or certificate.name like '%' || part_of_name || '%';
END
$BODY$;

create or replace function get_tags_by_certificate(
    in_id bigint
)
    returns table
            (
                id             integer,
                title          varchar(200),
                tag_id         integer,
                certificate_id integer
            )
    LANGUAGE 'plpgsql'
as
$BODY$
begin
    return query
        select *
        from tag
                 join certificate_tag ct on tag.id = ct.tag_id
        where ct.certificate_id = in_id;
end;
$BODY$;

create or replace function get_certificates_by_tag(
    in_id bigint
)
    returns table
            (
                id                 integer,
                name               varchar(200),
                description        varchar(200),
                price              numeric,
                creationdate       date,
                modificationdate   date,
                expirationdate     date,
                out_certificate_id integer,
                out_tag_id         integer,
                t_id               integer,
                title              varchar(200)
            )

    LANGUAGE 'plpgsql'
as
$BODY$
begin
    return query
        select *
        from certificate
                 join certificate_tag on certificate.id = certificate_id
                 left join tag as t on tag_id = t.id
        where certificate_id in
              (select certificate_id from certificate_tag where public.certificate_tag.tag_id in (in_id));
end;
$BODY$;
