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
                id             integer,
                name          varchar(200),
                description         varchar(200),
                price numeric,
                creationdate date,
                modificationdate date,
                expirationdate date,
                out_certificate_id integer,
                out_tag_id integer,
                t_id integer,
                title varchar(200)
            )

    LANGUAGE 'plpgsql'
as
$BODY$
begin
    return query
        select *
        from certificate join certificate_tag on certificate.id = certificate_id
                         left join tag as t on tag_id = t.id
        where certificate_id in (select certificate_id from certificate_tag where public.certificate_tag.tag_id in (in_id));
end;
$BODY$;
