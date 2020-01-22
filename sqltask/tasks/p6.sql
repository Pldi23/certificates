--* Create custom LOGGING db table. The following corresponding columns must be created:
-- a. Record insert date;
-- b. Referenced Table Name - Table Name where new record was inserted;
-- c. Description - The list of key-value pairs, separated by semicolon.
-- Note: empty values and their column names must be omitted.
-- Add required changes to your DB Schema to populate current table columns each time
-- when new record was inserted to any db table (in a scope of your schema).

CREATE TABLE log_table (
                           log_id            NUMBER
                               GENERATED AS IDENTITY
                               CONSTRAINT log_table_pk PRIMARY KEY,
                           created_at        DATE,
                           table_name        NVARCHAR2(200),
                           object_id         NVARCHAR2(50),
                           field_name        NVARCHAR2(30),
                           new_field_value   NCLOB
)
    tablespace task_ts;

CREATE OR REPLACE TRIGGER log_on_insert_to_tags_trigger AFTER
INSERT ON tags
    FOR EACH ROW
BEGIN
INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'tags',
             :new.t_id,
             't_title',
             :new.t_title
         );

END;

CREATE OR REPLACE TRIGGER log_on_insert_to_authors_trigger AFTER
INSERT ON authors
    FOR EACH ROW
BEGIN
INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'authors',
             :new.a_id,
             'a_name',
             :new.a_name
         );

END;

CREATE OR REPLACE TRIGGER log_on_insert_to_m2m_news_tags_trigger AFTER
INSERT ON m2m_news_tags
    FOR EACH ROW
BEGIN
INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'm2m_news_tags',
             :new.news_id || ',' || :new.tags_id,
             'news_id',
             to_char(:new.news_id)
         );
INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'm2m_news_tags',
             :new.news_id || ',' || :new.tags_id,
             'tags_id',
             to_char(:new.tags_id)
         );

END;

CREATE OR REPLACE TRIGGER log_on_insert_to_comments_trigger AFTER
INSERT ON comments
    FOR EACH ROW
BEGIN
INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'comments',
             :new.c_id,
             'news_id',
             to_clob(:new.news_id)
         );

INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'comments',
             :new.c_id,
             'c_content',
             :new.c_content
         );

IF :new.c_author IS NOT NULL THEN
INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'comments',
             :new.c_id,
             'c_author',
             :new.c_author
         );

END IF;

END;

CREATE OR REPLACE TRIGGER log_on_insert_to_news_trigger AFTER
INSERT ON news
    FOR EACH ROW
BEGIN
INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'news',
             :new.n_id,
             'author_id',
             to_clob(:new.author_id)
         );

IF :new.n_title IS NOT NULL THEN
INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'news',
             :new.n_id,
             'n_title',
             :new.n_title
         );

END IF;

IF :new.n_content IS NOT NULL THEN
INSERT INTO log_table (
    created_at,
    table_name,
    object_id,
    field_name,
    new_field_value
) VALUES (
             sysdate,
             'news',
             :new.n_id,
             'n_content',
             :new.n_content
         );

END IF;

END;


CREATE OR REPLACE VIEW get_insertion_logs (
                                           created,
                                           tab_name,
                                           object_id,
                                           description
    ) as
SELECT
    created_at,
    table_name,
    object_id,
    LISTAGG(field_name || ' : ' || to_clob(DBMS_LOB.SUBSTR(new_field_value, 2000)), ';')
FROM
    log_table
GROUP BY
    created_at,
    table_name,
    object_id;