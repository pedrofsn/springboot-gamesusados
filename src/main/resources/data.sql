INSERT INTO USER(name, email, phone, password, type, created_by, created_at) VALUES('Pedro', 'user1@gmail.com', '+55 (34) 9 1234-1111', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq', 'USER', 'manager@manager.com', {ts '2023-03-25 19:00:00.69'});
INSERT INTO USER(name, email, phone, password, type, created_by, created_at) VALUES('Luatane', 'user2@gmail.com', '+55 (61) 9 1234-2222', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq', 'USER', 'manager@manager.com', {ts '2023-03-25 19:00:00.69'});
INSERT INTO USER(name, email, phone, password, type, created_by, created_at) VALUES('Admin', 'admin@admin.com', '+55 (65) 9 1111-1111', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq', 'ADMIN', 'root', {ts '2023-03-25 19:00:00.69'});
INSERT INTO USER(name, email, phone, password, type, created_by, created_at) VALUES('Manager', 'manager@manager.com', '+55 (63) 9 2222-2222', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq', 'MANAGER', 'admin@admin.com', {ts '2023-03-25 19:00:00.69'});

INSERT INTO GAME_PLATFORM(name) VALUES('Playstation 4');
INSERT INTO GAME_PLATFORM(name) VALUES('Playstation 5');

INSERT INTO GAME(title, game_platform_id, created_by, created_at) VALUES('God of War IV', 1, 'pedrokra@gmail.com', {ts '2021-07-07 08:22:52.69'});
INSERT INTO GAME(title, game_platform_id, created_by, created_at) VALUES('Horizon Zero Dawn: Forbidden West', 2, 'pedrokra@gmail.com', {ts '2021-07-07 08:22:52.69'});
INSERT INTO GAME(title, game_platform_id, created_by, created_at) VALUES('Ghost of Tsushima Directors Cut', 2, 'pedrokra@gmail.com', {ts '2021-07-07 08:22:52.69'});

INSERT INTO GAME_ANNOUNCEMENT(price, game_id, owner_id, enabled, created_by, created_at) VALUES(300.00, 1, 1, true, 'user1@gmail.com', {ts '2021-07-07 08:22:52.69'});
INSERT INTO GAME_ANNOUNCEMENT(price, game_id, owner_id, enabled, created_by, created_at) VALUES(500.00, 2, 1, true, 'user1@gmail.com', {ts '2021-07-07 08:22:52.69'});
INSERT INTO GAME_ANNOUNCEMENT(price, game_id, owner_id, enabled, created_by, created_at) VALUES(499.00, 2, 2, true, 'user2@gmail.com', {ts '2021-07-07 08:22:52.69'});

INSERT INTO GAME_ANNOUNCEMENT(price, game_id, owner_id, enabled, created_by, created_at) VALUES(123.00, 1, 1, false, 'user1@gmail.com', {ts '2023-01-13 08:22:52.69'});
INSERT INTO GAME_ANNOUNCEMENT(price, game_id, owner_id, enabled, created_by, created_at) VALUES(321.00, 1, 2, false, 'user1@gmail.com', {ts '2023-01-12 08:22:52.69'});
INSERT INTO GAME_ANNOUNCEMENT(price, game_id, owner_id, enabled, created_by, created_at) VALUES(456.00, 1, 2, true, 'user1@gmail.com', {ts '2023-01-11 08:22:52.69'});

INSERT INTO REPORT_CONTENT(description, game_id, announcement_id, created_by, created_at) VALUES('Jogo denunciado via SQL [1]', 1, 1, 'user2@gmail.com', {ts '2023-01-01 08:22:52.69'});
INSERT INTO REPORT_CONTENT(description, game_id, announcement_id, created_by, created_at) VALUES('Jogo denunciado via SQL [2]', 1, 1, 'user2@gmail.com', {ts '2023-01-01 08:23:52.69'});