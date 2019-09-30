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
    certificate_id integer
)
    returns table
            (
                id    integer,
                title varchar(200),
                tag_id integer,
                c_id integer
            )
as
$BODY$
begin
    return query
        select *
        from tag
                 join certificate_tag ct on tag.id = ct.tag_id
    where ct.certificate_id = id;
end;
$BODY$