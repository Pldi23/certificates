--* Create custom LOGGING db table. The following corresponding columns must be created:
-- a. Record insert date;
-- b. Referenced Table Name - Table Name where new record was inserted;
-- c. Description - The list of key-value pairs, separated by semicolon.
-- Note: empty values and their column names must be omitted.
-- Add required changes to your DB Schema to populate current table columns each time
-- when new record was inserted to any db table (in a scope of your schema).
DROP TABLE log_table;

CREATE TABLE log_table (
                           created_at    DATE,
                           table_name    NVARCHAR2(200),
                           description   NCLOB
);


CREATE OR REPLACE TRIGGER log_on_insert_to_tags_trigger AFTER
INSERT ON tags
    FOR EACH ROW
BEGIN
INSERT INTO log_table (
    created_at,
    table_name,
    description
) VALUES (
             sysdate,
             'tags',
             't_title - ' || :new.t_title
         );
END;

CREATE OR REPLACE TRIGGER log_on_insert_to_authors_trigger AFTER
INSERT ON authors
    FOR EACH ROW
BEGIN
INSERT INTO log_table (
    created_at,
    table_name,
    description
) VALUES (
             sysdate,
             'authors',
             'a_name - ' || :new.a_name
         );
END;

CREATE OR REPLACE TRIGGER log_on_insert_to_comments_trigger AFTER
INSERT ON comments
    FOR EACH ROW
DECLARE
    des NCLOB;
BEGIN
des := 'news_id - ' || :new.news_id;
des := des || ',c_content - ' || :new.c_content;
des := des || ',c_author - ' || :new.c_author;
INSERT INTO log_table (
    created_at,
    table_name,
    description
) VALUES (
             sysdate,
             'comments',
             des
         );
END;


CREATE OR REPLACE TRIGGER log_on_insert_to_news_trigger AFTER
INSERT ON news
    FOR EACH ROW
DECLARE
    des NCLOB;
BEGIN
des := 'author_id - ' || :new.author_id;
IF :new.n_title is not NULL THEN
        des := des || ',n_title - ' || :new.n_title;
END IF;
IF :new.n_content is not NULL THEN
        des := des || ',n_content - ' || :new.n_content;
END IF;
INSERT INTO log_table (
    created_at,
    table_name,
    description
) VALUES (
             sysdate,
             'news',
             des
         );
END;