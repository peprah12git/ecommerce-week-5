-- Update existing admin user role to uppercase
UPDATE Users SET role = 'ADMIN' WHERE email = 'admin@test.com';

-- Verify the update
SELECT user_id, name, email, role FROM Users WHERE email = 'admin@test.com';
