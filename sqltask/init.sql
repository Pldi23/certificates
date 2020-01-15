CREATE TABLESPACE task_ts
    datafile 'file_task_ts'
    size 50 M
    AUTOEXTEND ON NEXT 500 K MAXSIZE 100 M
    LOGGING;

CREATE TABLE authors
(
    a_id   NUMBER GENERATED AS IDENTITY
        CONSTRAINT authors_pk
            PRIMARY KEY,
    a_name NVARCHAR2(150) UNIQUE NOT NULL
)
    tablespace task_ts;

CREATE TABLE news
(
    n_id      NUMBER GENERATED AS IDENTITY
        CONSTRAINT news_pk
            PRIMARY KEY,
    n_title   NVARCHAR2(150),
    n_content NCLOB,
    author_id NUMBER(10) NOT NULL
        CONSTRAINT news_authors_id_fk
            REFERENCES authors,
    CHECK (n_title IS NOT NULL OR n_content IS NOT NULL)
)
    tablespace task_ts;


CREATE TABLE tags
(
    t_id    NUMBER GENERATED AS IDENTITY
        CONSTRAINT tags_pk
            PRIMARY KEY,
    t_title NVARCHAR2(30) UNIQUE NOT NULL
)
    tablespace task_ts;

CREATE TABLE m2m_news_tags
(
    news_id NUMBER NOT NULL
        CONSTRAINT m2m_news_tags_news_id_fk
            REFERENCES news,

    tags_id NUMBER NOT NULL
        CONSTRAINT m2m_news_tags_tags_id_fk
            REFERENCES tags,
    CONSTRAINT m2m_news_tags_table_pk
        PRIMARY KEY (news_id, tags_id)
)
    tablespace task_ts;

CREATE TABLE comments
(
    c_id      NUMBER GENERATED AS IDENTITY
        CONSTRAINT comments_pk
            PRIMARY KEY,
    news_id   NUMBER          NOT NULL
        CONSTRAINT comments_table_news_id_fk
            REFERENCES news,
    c_content NVARCHAR2(2000) NOT NULL,
    c_author  NVARCHAR2(150) DEFAULT 'anonymous'

)
    tablespace task_ts;


-- generate data to authors table
BEGIN
FOR i IN 1..100 LOOP
insert into AUTHORS(A_NAME)
values ('author ' || to_char(i));
END LOOP;
END;

-- generate data to tags table
BEGIN
FOR i IN 1..1000 LOOP
insert into TAGS(t_title)
values ('tag ' || to_char(i));
END LOOP;
END;

-- generate data to news table
declare
    s nvarchar2(2000);
r number;
id number;
BEGIN
FOR i IN 1..200 LOOP
        r := DBMS_RANDOM.value(1,999);
s := DBMS_RANDOM.string('x', r);
select a_id
into id
from authors
where a_id is not null
ORDER BY DBMS_RANDOM.VALUE FETCH NEXT 1 ROWS ONLY;
insert into NEWS(N_TITLE, N_CONTENT, AUTHOR_ID)
values ('post ' || to_char(i), s, id);
END LOOP;
END;

-- generate data to comments table
declare
    s nvarchar2(2000);
r number;
id number;
a NVARCHAR2(10);
BEGIN
FOR i IN 1..300 LOOP
        r := DBMS_RANDOM.value(1,999);
s := DBMS_RANDOM.string('x', r);
select n_id
into id
from news
where n_id is not null
ORDER BY DBMS_RANDOM.VALUE FETCH NEXT 1 ROWS ONLY;
a := 'user ' || DBMS_RANDOM.string('x', 3);
INSERT INTO comments(news_id, c_content, c_author)
values (id, s, a);
END LOOP;
END;

-- generate data to m2m_news_tags table using functional hint
declare
    new_id number;
tag_id number;
BEGIN
FOR i IN 1..20000 LOOP
select n_id
into new_id
from news
where n_id is not null
ORDER BY DBMS_RANDOM.VALUE FETCH NEXT 1 ROWS ONLY;
SELECT t_id
into tag_id
from tags
where t_id is not null
ORDER BY DBMS_RANDOM.VALUE FETCH NEXT 1 ROWS ONLY;
INSERT /*+ ignore_row_on_dupkey_index(m2m_news_tags, m2m_news_tags_table_pk) */ INTO m2m_news_tags(news_id, tags_id)
values (new_id, tag_id);
END LOOP;
END;