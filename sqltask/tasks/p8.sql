-- Prepare a SQL Statement to calculate each table size in your schema.
SELECT
    segment_name,
    owner,
    segment_type,
    tablespace_name,
    bytes
FROM
    dba_segments
WHERE
        tablespace_name = 'TASK_TS'
  AND segment_type = 'TABLE'
  AND owner = 'SYSTEM';