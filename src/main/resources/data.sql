-- One admin user, named admin1 with passwor 4dm1n and authority admin
-- INSERT INTO admins(id, email, password, name)
--            VALUES (1, 'admin1@gmail.com', '4dm1n', 'admin1');
-- INSERT INTO players(id, email, password, name, spectating_id)
--             VALUES (1, 'player1@gmail.com', 'player1', 'player_1', null),
--                    (2, 'player2@gmail.com', 'player2', 'player_2', null);
-- 4dm1n --> $2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS
-- petris123 --> $2a$10$qwUnssgZUw7Ygejt7pQXnO8Yq6ITEdDp9/4LlEocqqhd3FISDvLSe
INSERT INTO authorities(id,authority) VALUES 
                       (1,'ADMIN'),
                       (2,'PLAYER');
INSERT INTO users(id, online, email, password, username, authority) VALUES 
                 (1, false, 'admin1@gmail.com','$2a$10$qwUnssgZUw7Ygejt7pQXnO8Yq6ITEdDp9/4LlEocqqhd3FISDvLSe','admin1',1),
                 (2, false, 'player1@gmail.com', '$2a$10$qwUnssgZUw7Ygejt7pQXnO8Yq6ITEdDp9/4LlEocqqhd3FISDvLSe', 'player1', 2),
                 (3, false, 'player2@gmail.com', '$2a$10$qwUnssgZUw7Ygejt7pQXnO8Yq6ITEdDp9/4LlEocqqhd3FISDvLSe', 'player2', 2), 
                 (4, false, 'admin2@gmail.com','$2a$10$qwUnssgZUw7Ygejt7pQXnO8Yq6ITEdDp9/4LlEocqqhd3FISDvLSe','admin2',1),
                 (5, true, 'player3@gmail.com', '$2a$10$qwUnssgZUw7Ygejt7pQXnO8Yq6ITEdDp9/4LlEocqqhd3FISDvLSe', 'player3', 2),
                 (6, true, 'player4@gmail.com', '$2a$10$qwUnssgZUw7Ygejt7pQXnO8Yq6ITEdDp9/4LlEocqqhd3FISDvLSe', 'player4', 2),
                 (7, true, 'player5@gmail.com', '$2a$10$qwUnssgZUw7Ygejt7pQXnO8Yq6ITEdDp9/4LlEocqqhd3FISDvLSe', 'player5', 2);
INSERT INTO admins(id,user_id) VALUES
                  (1,1), 
                  (2,4);
INSERT INTO stats(id, total_bacterium, total_sarcinas, victories, losses) VALUES 
                 (1, 10, 2, 5, 1),
                 (2, 12, 3, 1, 5),
                 (3, 0, 0, 0, 0),
                 (4, 0, 0, 0, 0),
                 (5, 0, 0, 0, 0);
Insert INTO players(id,user_id,stats_id) VALUES 
                   (1,2,1),
                   (2,3,2),
                   (3,5,3),
                   (4,6,4),
                   (5,7,5);
INSERT INTO player_friends(id1,id2) VALUES (1,2);
INSERT INTO friend_request(id,is_accepted,author,receiver) VALUES (1,true,1,2);

-- INSERT INTO achievements(id, name, description, meter, limit, creator_id)
--                  VALUES (1, 'Logro 1', 'Descripcion 1', 'MATCH', 0, 1);
INSERT INTO achievements(id, name, description, meter, num_condition, creator_id, badge_image) VALUES 
(1, 'Primera partida jugada', 'Has jugado tu primera partida en Petris. ¡A por más!', 'MATCH', 1, 1,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd-8aDTzrK3ek8sN1rfBiNqlGOD2rWgOfhYcOvYd-baVwHC0cQqmSJjoTsxI-AJVO23hY&usqp=CAU'),
(2, 'Primeras 5 partidas jugadas', 'Has jugado tus 5 primeras partidas en Petris. ¡A por más!', 'MATCH', 5, 1, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd-8aDTzrK3ek8sN1rfBiNqlGOD2rWgOfhYcOvYd-baVwHC0cQqmSJjoTsxI-AJVO23hY&usqp=CAU'),
(3, 'Primeras 10  partidas jugadas', 'Has jugado tus 10 primeras partidas en Petris. ¡A por más!', 'MATCH', 10, 1, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd-8aDTzrK3ek8sN1rfBiNqlGOD2rWgOfhYcOvYd-baVwHC0cQqmSJjoTsxI-AJVO23hY&usqp=CAU'),
(4, '50 partidas jugadas', 'Has jugado 50 partidas en Petris. Estás convirtiéndote en todo un experto ¡Sigue así!', 'MATCH', 50, 1, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd-8aDTzrK3ek8sN1rfBiNqlGOD2rWgOfhYcOvYd-baVwHC0cQqmSJjoTsxI-AJVO23hY&usqp=CAU'),
(5, 'Primera partida ganada', 'Has ganado tu primera partida en Petris. ¡Enhorabuena!', 'VICTORY', 1, 1, 'https://previews.123rf.com/images/tatianasun/tatianasun1703/tatianasun170300104/75047415-pulgar-arriba-vector-logo-grunge-icono-verde-signo-como-s%C3%ADmbolo-redondo-simple-aislado.jpg'),
(6, 'Trío de victorias', 'Llevas 3 victorias en Petris. ¡A seguir sumando campeón!', 'VICTORY', 3, 1, 'https://previews.123rf.com/images/tatianasun/tatianasun1703/tatianasun170300104/75047415-pulgar-arriba-vector-logo-grunge-icono-verde-signo-como-s%C3%ADmbolo-redondo-simple-aislado.jpg'),
(7, 'Repóker de victorias', '¡Cinco victorias! ¡Que siga la racha!', 'VICTORY', 5, 1, 'https://previews.123rf.com/images/tatianasun/tatianasun1703/tatianasun170300104/75047415-pulgar-arriba-vector-logo-grunge-icono-verde-signo-como-s%C3%ADmbolo-redondo-simple-aislado.jpg'),
(8, '10 victorias', 'Ganar, ganar y ganar. ¡Supercampeón!', 'VICTORY', 10, 1, 'https://previews.123rf.com/images/tatianasun/tatianasun1703/tatianasun170300104/75047415-pulgar-arriba-vector-logo-grunge-icono-verde-signo-como-s%C3%ADmbolo-redondo-simple-aislado.jpg'),
(9, 'Primera partida perdida', 'Has perdido tu primera partida en Petris. ¡No pasa nada!', 'DEFEAT', 1, 1, 'https://st2.depositphotos.com/5266903/8942/v/950/depositphotos_89427162-stock-illustration-thumb-down-rounded-vector-icon.jpg'),
(10, '5 partidas perdidas', 'Has perdido 5 partidas. ¡La próxima ganarás!', 'DEFEAT', 5, 1, 'https://st2.depositphotos.com/5266903/8942/v/950/depositphotos_89427162-stock-illustration-thumb-down-rounded-vector-icon.jpg'),
(11, '10 partidas perdidas', 'Hay que empezar a sumar de tres. Te acercas al descenso', 'DEFEAT', 10, 2, 'https://st2.depositphotos.com/5266903/8942/v/950/depositphotos_89427162-stock-illustration-thumb-down-rounded-vector-icon.jpg');

-- INSERT INTO achievements_player(player_id, achievement_id)
--                         VALUES (1, 1);
INSERT INTO achievement_player(id,player_id, achievement_id,achievement_date) VALUES 
                              (1,1, 1,'2023-01-01 16:30'),
                              (2,1, 2,'2023-01-01 16:30'),
                              (3,1, 5,'2023-01-01 16:30'),
                              (4,1, 6,'2023-01-01 16:30'),
                              (5,1, 7,'2023-01-01 16:30'),
                              (6,1, 9,'2023-01-01 16:30'),
                              (7,2, 1,'2023-01-01 16:30'),
                              (8,2, 2,'2023-01-01 16:30'),
                              (9,2, 5,'2023-01-01 16:30'),
                              (10,2, 9,'2023-01-01 16:30'),
                              (11,2, 10,'2023-01-01 16:30');

-- INSERT INTO matches(id, name, startDate, endDate, numRounds, contaminationLevelBlue, contaminationLevelRed, isPrivated, winner_id, creator_id, player_id)
--             VALUES (1, 'Partida 1');
INSERT INTO matches(id, name, start_date, end_date, num_turn, contamination_level_blue, contamination_level_red, is_privated, code, winner_id, player_blue_id, player_red_id) VALUES 
                    (1, 'Partida 1', '2023-01-01 16:25', '2023-01-01 16:55', 1, 0, 0, TRUE, 'code1', 1, 1, 2),
                    (2, 'Partida 2', '2023-01-04 11:05', '2023-01-04 11:55', 2, 3, 5, TRUE, 'code2', 1, 1, 2),
                    (3, 'Partida 3', '2023-01-05 19:00', '2023-01-05 19:35', 1, 4, 8, TRUE, 'code3', 1, 2, 2),
                    (4, 'Partida 4', '2023-01-06 13:03', '2023-01-06 13:35', 2, 8, 6, TRUE, 'code4', 2, 1, 2),
                    (5, 'Partida 5', '2023-01-11 11:10', '2023-01-11 11:55', 2, 4, 6, TRUE, 'code5', 1, 1, 2),
                    (6, 'Partida 6', '2023-01-21 16:25', '2023-01-21 16:55', 2, 4, 5, TRUE, 'code6', 1, 2, 1),
                    (7, 'Partida 7' , '2024-01-12 10:37', null, 2,1,0,TRUE, 'code7',null,3,4);

-- INSERT INTO comments(id, message, commentDate, player_id, match_id)
--              VALUES (1, "Comentario 1", '2023-01-01', 1, 1);
INSERT INTO comments(id, message, comment_date, player_id, match_id) VALUES 
                    (1, 'Voy a ganar!!!!', '2023-01-01 16:30', 1, 1),
                    (2, 'Mmmm ya veremos!', '2023-01-01 16:32', 2, 1),
                    (3, '1, 2, 3 y vas a perder jajajaja', '2023-01-04 11:10', 1, 2),
                    (4, 'Con que esas tenemos... Remontaré!!', '2023-01-05 19:12', 2, 3),
                    (5, 'Tomaa voy a ganar!!!!', '2023-01-06 13:33', 2, 4),
                    (6, 'Que jugada!! Magistral', '2023-01-11 11:20', 1, 5);

-- INSERT INTO matchInvitations(id, isAccepted, match_id, creator_id, invited_id)
--                      VALUES (1, false, 1, 1, 2);
INSERT INTO match_invitations(id, is_accepted, match_id, author, receiver) VALUES 
                             (1, TRUE, 1, 1, 2),
                             (2, TRUE, 2, 1, 2),
                             (3, TRUE, 3, 2, 1),
                             (4, TRUE, 4, 1, 2),
                             (5, TRUE, 5, 1, 2),
                             (6, TRUE, 6, 2, 1);

-- INSERT INTO petrisDishes(id, index, chipsPlayerBlue, chipsPlayerRed)
--                 VALUES (1, 1, 0, 0);
INSERT INTO petris_dishes(id, index, chips_player_blue, chips_player_red, match_id) VALUES 
                        (1, 0, 0, 0, 1),
                        (2, 1, 2, 1, 1),
                        (3, 2, 1, 0, 1),
                        (4, 3, 0, 0, 1),
                        (5, 4, 0, 1, 1),
                        (6, 5, 0, 0, 1),
                        (7, 6, 0, 0, 1);
