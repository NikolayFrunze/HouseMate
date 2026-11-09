-- Fictitious test data for HouseMate.
-- Creates 5 users, one house owned by the demo user with 5 members,
-- and 4 expenses: Light, Gas, Trash and Wi-Fi.
--
-- Login for the app:  demo@housemate.test  /  password: demo1234
-- (all the test users use the same password)
--
-- Requirement: start the backend at least once (Hibernate creates the house_members table).
-- Run only once: if repeated, the expenses are duplicated (the other steps are safe to repeat).

BEGIN;

-- 1) Fictitious users (password "demo1234", BCrypt hash)
INSERT INTO users (username, email, password) VALUES
    ('Demo User', 'demo@housemate.test',         '$2a$10$lKRXowanl9jkDbREXdMz2ODud6ZSZRKJvg91jgwXreijqAyxo8QzC'),
    ('Ana',       'ana.demo@housemate.test',     '$2a$10$lKRXowanl9jkDbREXdMz2ODud6ZSZRKJvg91jgwXreijqAyxo8QzC'),
    ('Luis',      'luis.demo@housemate.test',    '$2a$10$lKRXowanl9jkDbREXdMz2ODud6ZSZRKJvg91jgwXreijqAyxo8QzC'),
    ('Marta',     'marta.demo@housemate.test',   '$2a$10$lKRXowanl9jkDbREXdMz2ODud6ZSZRKJvg91jgwXreijqAyxo8QzC'),
    ('Pablo',     'pablo.demo@housemate.test',   '$2a$10$lKRXowanl9jkDbREXdMz2ODud6ZSZRKJvg91jgwXreijqAyxo8QzC')
ON CONFLICT (email) DO NOTHING;

-- 2) Fictitious house owned by the demo user
INSERT INTO houses (user_id, address, postal_code, housing_code)
SELECT id, 'Main Street 12', '28013', '3B'
FROM users
WHERE email = 'demo@housemate.test'
ON CONFLICT (user_id) DO NOTHING;

-- 3) The 5 users are members of the house
INSERT INTO house_members (house_id, user_id)
SELECT h.id, u.id
FROM houses h
JOIN users u ON u.email IN (
    'demo@housemate.test',
    'ana.demo@housemate.test',
    'luis.demo@housemate.test',
    'marta.demo@housemate.test',
    'pablo.demo@housemate.test'
)
WHERE h.user_id = (SELECT id FROM users WHERE email = 'demo@housemate.test')
ON CONFLICT (user_id) DO NOTHING;

-- 4) Expenses of the house (only if it has none yet)
-- With 5 members: Light 80 -> 16 each, Gas 45.50 -> 9.10, Trash 25 -> 5, Wi-Fi 35 -> 7. Total 185.50
INSERT INTO expenses (house_id, name, amount)
SELECT h.id, v.name, v.amount
FROM houses h
CROSS JOIN (VALUES
    ('Light',   80.00::numeric),
    ('Gas',     45.50::numeric),
    ('Trash',   25.00::numeric),
    ('Wi-Fi',   35.00::numeric)
) AS v(name, amount)
WHERE h.user_id = (SELECT id FROM users WHERE email = 'demo@housemate.test')
  AND NOT EXISTS (SELECT 1 FROM expenses e WHERE e.house_id = h.id);

COMMIT;
