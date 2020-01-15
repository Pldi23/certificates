--Develop single SQL statement that will return the list of all available news (news id, news title columns)
--and one more column that will display all concatenated tags values, available for current news as a single string.
--Make two versions of statement:
--a. By using previously developed custom function (#3).
--b. * By using Oracle 10g DB features.
SELECT
    n_id      id,
    n_title   news_title,
    tags_list_by_news(n_id, ',') tag_list
FROM
    news;

SELECT
    n_id      id,
    n_title   news_title,
    LISTAGG(t_title, ', ') WITHIN GROUP(ORDER BY t_title) "tags"
FROM
    news
        LEFT OUTER JOIN m2m_news_tags ON m2m_news_tags.news_id = news.n_id
        LEFT OUTER JOIN tags ON m2m_news_tags.tags_id = tags.t_id
GROUP BY
    n_id,
    n_title;