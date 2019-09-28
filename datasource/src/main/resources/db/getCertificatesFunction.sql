CREATE OR REPLACE FUNCTION public.get_certificate_by_name_description(
    part_of_description text, part_of_name text)
    RETURNS TABLE(id integer, name varchar(200), description varchar(200), price numeric, creationdate date, modificationdate date,
                  expirationdate date, certificate_id integer,  tag_id integer, t_id integer, title varchar(200))
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
    ROWS 1000
AS $BODY$BEGIN
    RETURN QUERY
        SELECT *
        FROM   certificate join certificate_tag ct on certificate.id = ct.certificate_id join tag t on ct.tag_id = t.id
        WHERE  certificate.description like '%' || $1 || '%' or certificate.name like '%' || $2 || '%';
END
$BODY$;