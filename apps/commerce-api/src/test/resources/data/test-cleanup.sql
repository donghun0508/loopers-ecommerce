-- test-cleanup.sql
SET FOREIGN_KEY_CHECKS = 0;

SET @tables = NULL;
SELECT GROUP_CONCAT('TRUNCATE TABLE `', table_name, '`') INTO @tables
FROM information_schema.tables
WHERE table_schema = DATABASE();

PREPARE stmt FROM @tables;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET FOREIGN_KEY_CHECKS = 1;