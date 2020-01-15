--* Make a writers competition cross-map.
-- Create a statement that will generate random authors distribution by competition pairs.
-- Statement will generate a list of author names pairs selected for a single round of tournament, displayed as two separate columns.
-- Each author must be presented in resulting set (in both columns) once only.
-- (The total number of authors must be even. Do not use custom functions during current implementation.)
WITH ct AS (
    SELECT tb.*,
           mod(ROW_NUMBER() OVER( ORDER BY dbms_random.value), 2) rn,
           ceil(ROW_NUMBER() OVER( ORDER BY dbms_random.value) / 2) grp
    FROM authors tb
)
SELECT
    a1,
    a2
FROM (SELECT MAX( CASE WHEN rn = 1 THEN tb.a_name END) a1,
             MAX(CASE WHEN rn = 0 THEN tb.a_name END) a2
      FROM ct tb
      GROUP BY grp
     ) res
WHERE
    res.a1 IS NOT NULL
  AND res.a2 IS NOT NULL;