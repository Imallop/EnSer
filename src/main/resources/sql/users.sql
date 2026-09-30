TRUNCATE users, user_roles CASCADE;

INSERT INTO users(username, password) VALUES
('user', '{noop}1234'),
('admin', '{noop}1234');

INSERT INTO user_roles(username, role) VALUES
('user', 'USER'),
('admin', 'ADMIN');