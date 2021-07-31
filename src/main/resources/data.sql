INSERT INTO USER(name, email, phone, password) VALUES('Pedro', 'user1@gmail.com', '+55 (34) 9 1234-1111', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq');
INSERT INTO USER(name, email, phone, password) VALUES('Luatane', 'user2@gmail.com', '+55 (61) 9 1234-2222', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq');

INSERT INTO GAME_PLATFORM(name) VALUES('Playstation 4');
INSERT INTO GAME_PLATFORM(name) VALUES('Playstation 5');

INSERT INTO GAME(title, game_platform_id, created_by, created_at) VALUES('God of War IV', 1, 'pedrokra@gmail.com', {ts '2021-07-07 08:22:52.69'});
INSERT INTO GAME(title, game_platform_id, created_by, created_at) VALUES('Horizon Zero Dawn: Forbidden West', 2, 'pedrokra@gmail.com', {ts '2021-07-07 08:22:52.69'});
INSERT INTO GAME(title, game_platform_id, created_by, created_at) VALUES('Ghost of Tsushima Directors Cut', 2, 'pedrokra@gmail.com', {ts '2021-07-07 08:22:52.69'});

INSERT INTO GAME_ANNOUNCEMENT(price, game_id, owner_id, created_by, created_at) VALUES(300.00, 1, 1, 'user1@gmail.com', {ts '2021-07-07 08:22:52.69'});
INSERT INTO GAME_ANNOUNCEMENT(price, game_id, owner_id, created_by, created_at) VALUES(500.00, 2, 1, 'user1@gmail.com', {ts '2021-07-07 08:22:52.69'});
INSERT INTO GAME_ANNOUNCEMENT(price, game_id, owner_id, created_by, created_at) VALUES(499.00, 2, 2, 'user2@gmail.com', {ts '2021-07-07 08:22:52.69'});