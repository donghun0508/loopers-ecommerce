-- User 테스트 데이터 INSERT (3명)
INSERT INTO users (user_id, email, birth, gender, created_at, updated_at)
VALUES ('test', 'john.doe@example.com', '1995-03-15', 'M', NOW(), NOW()),
       ('alice456', 'alice.smith@example.com', '1992-07-22', 'F', NOW(), NOW()),
       ('bob789', 'bob.wilson@example.com', '1988-11-08', 'M', NOW(), NOW());

INSERT INTO point (user_id, balance, created_at, updated_at)
SELECT id, 0, NOW(), NOW()
FROM users
WHERE user_id = 'test'
UNION ALL
SELECT id, 50000, NOW(), NOW()
FROM users
WHERE user_id = 'alice456'
UNION ALL
SELECT id, 25000, NOW(), NOW()
FROM users
WHERE user_id = 'bob789';