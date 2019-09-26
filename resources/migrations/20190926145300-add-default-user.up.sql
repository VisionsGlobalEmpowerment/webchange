DELETE FROM users
WHERE email='demo@example.com';

--;;

INSERT INTO users (first_name, last_name, email, password, active, created_at, last_login)
VALUES ('User', 'Default', 'demo@example.com', 'bcrypt+sha512$90ae4b44bdfdf69229fb4f38f4b42fa7$12$bbcc6b3aa35f5f3d9143f669e7ff6803e14b78faa185c78d', true, '2019-09-26 14:47:43.218+04', '2019-09-26 14:47:43.218+04');
