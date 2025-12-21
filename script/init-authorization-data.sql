-- =========================
-- INSERT ROLES
-- =========================
INSERT INTO roles (name)
VALUES ('MENTOR'), ('INTERN')
ON CONFLICT (name) DO NOTHING;

-- =========================
-- INSERT PERMISSIONS
-- =========================
INSERT INTO permissions (name)
VALUES
    ('USERS_VIEW'),
    ('INTERN_LOG_READ'),
    ('INTERN_LOG_CREATE'),
    ('INTERN_LOG_UPDATE'),
    ('INTERN_LOG_DELETE')
ON CONFLICT (name) DO NOTHING;

-- =========================
-- MAP ROLE ↔ PERMISSION
-- =========================

-- ADMIN: tất cả permission
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'USERS_VIEW',
    'INTERN_LOG_READ',
    'INTERN_LOG_CREATE',
    'INTERN_LOG_UPDATE',
    'INTERN_LOG_DELETE'
)
WHERE r.name = 'MENTOR'
ON CONFLICT DO NOTHING;

-- MEMBER: 
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'INTERN_LOG_READ',
    'INTERN_LOG_CREATE',
    'INTERN_LOG_UPDATE',
    'INTERN_LOG_DELETE'
)
WHERE r.name = 'INTERN'
ON CONFLICT DO NOTHING;
