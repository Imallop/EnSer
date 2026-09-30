TRUNCATE permissions CASCADE;

INSERT INTO permissions(id, resource, action) VALUES
('users:read', 'users', 'read'),
('users:create', 'users', 'create'),
('users:update', 'users', 'update'),
('users:delete', 'users', 'delete');

