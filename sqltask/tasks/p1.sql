-- Develop a query to calculate the number of news, written by each author,
-- the average number of comments per news for a current author
-- and the most popular tag, referred to author news.
-- All these information must be output in one single result set.
-- Based on these query create a custom db view.

-- Service view
CREATE OR REPLACE VIEW authors_news_number_and_avg_comments_view (
                                                                  auth_id,
                                                                  news_quantity,
                                                                  avg_comments
    ) AS
SELECT
    news.author_id,
    COUNT(n_id) AS q,
    COUNT(c_id) / COUNT(n_id) AS av
FROM
    news
        LEFT OUTER JOIN comments ON news.n_id = comments.news_id
GROUP BY
    news.author_id;

-- Service view

CREATE OR REPLACE VIEW authors_id_with_most_popular_tags_view (
                                                               author_id,
                                                               tag_title,
                                                               popularity_index
    ) AS
SELECT
    aid,
    tt,
    uq
FROM
    (
        SELECT
            tags.t_id,
            tags.t_title     tt,
            news.author_id   AS aid,
            COUNT(m2m_news_tags.tags_id) AS uq
        FROM
            tags
                JOIN m2m_news_tags ON m2m_news_tags.tags_id = tags.t_id
                JOIN news ON m2m_news_tags.news_id = news.n_id
        GROUP BY
            tags.t_id,
            tags.t_title,
            news.author_id
    ) auth_used_tags
WHERE
        auth_used_tags.uq = (
        SELECT
            MAX(COUNT(m2m_news_tags.tags_id))
        FROM
            m2m_news_tags
                JOIN news ON m2m_news_tags.news_id = news.n_id
        WHERE
                news.author_id = auth_used_tags.aid
        GROUP BY
            m2m_news_tags.tags_id,
            news.author_id
    );

-- Resulting view.

CREATE OR REPLACE VIEW authors_statistic_view (
                                               author,
                                               news_quantity,
                                               avg_comments_per_news,
                                               popular_tag,
                                               popularity_index
    ) AS
SELECT
    authors.a_name,
    authors_news_number_and_avg_comments_view.news_quantity,
    authors_news_number_and_avg_comments_view.avg_comments,
    authors_id_with_most_popular_tags_view.tag_title,
    authors_id_with_most_popular_tags_view.popularity_index
FROM
    authors
        LEFT OUTER JOIN authors_news_number_and_avg_comments_view ON authors.a_id = authors_news_number_and_avg_comments_view.auth_id
        LEFT OUTER JOIN authors_id_with_most_popular_tags_view ON authors.a_id = authors_id_with_most_popular_tags_view.author_id
;