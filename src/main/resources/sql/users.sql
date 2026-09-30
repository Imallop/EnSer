TRUNCATE users CASCADE;

INSERT INTO users(username, password) VALUES
('user', '{noop}1234'),
('admin', '{noop}1234');