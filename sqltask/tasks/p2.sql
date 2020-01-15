--Write SQL statement to select author names who wrote more than 3 000 characters,
--but the average number of characters per news is more than 500.
--Think about the shortest statement notation.

SELECT
    a_name author
FROM
    authors
WHERE
        a_id IN (
        SELECT
            author_id
        FROM
            news
        GROUP BY
            author_id
        HAVING
                SUM(length(n_content)) > 3000
           AND SUM(length(n_content)) / COUNT(n_id) > 500
    );