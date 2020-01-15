-- Prepare a SQL statement to output total/available space within each tablespace. Calculate “Used %” column value.
SELECT
    fs.tablespace_name   "Tablespace",
    ( df.totalspace - fs.freespace ) "Used MB",
    fs.freespace         "Free MB",
    df.totalspace        "Total MB",
    round(100 *((df.totalspace - fs.freespace) / df.totalspace)) "Used %"
FROM
    (
        SELECT
            tablespace_name,
            round(SUM(bytes) / 1048576) totalspace
        FROM
            dba_data_files
        GROUP BY
            tablespace_name
    ) df,
    (
        SELECT
            tablespace_name,
            round(SUM(bytes) / 1048576) freespace
        FROM
            dba_free_space
        GROUP BY
            tablespace_name
    ) fs
WHERE
        df.tablespace_name = fs.tablespace_name;