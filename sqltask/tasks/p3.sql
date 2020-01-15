-- Create custom db function that will return the list of all tags referred to a current news,
-- concatenated by specified separator character.
-- Function must accept the news id and separator character as input parameters
-- and return a single string as a result of tag values concatenation.

CREATE OR REPLACE FUNCTION tags_list_by_news(news_id_in IN NUMBER, separator IN NVARCHAR2)
    RETURN NVARCHAR2
IS
    return_text  NVARCHAR2(2000) := NULL;
BEGIN
FOR x IN
(
    SELECT t_title
    FROM tags
    JOIN m2m_news_tags on tags.t_id = m2m_news_tags.tags_id
    WHERE m2m_news_tags.news_id = news_id_in
)
LOOP
    return_text := return_text || separator || x.t_title ;
END LOOP;
RETURN LTRIM(return_text, separator);
END;