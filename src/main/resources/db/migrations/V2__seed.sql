insert into Roles (name) values
('ADMIN'),
('REGULAR');

insert into Permissions (name) values
('create:permission'),
('delete:permission'),
('view:permission'),
('create:role'),
('compose:role'),
('view:role'),
('delete:role'),
('compose:user'),
('delete:user'),
('view:user');

insert into role_permissions (role, permission) values
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10);
