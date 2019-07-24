SELECT setval('dataset_items_id_seq', 16, true);

--;;
INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('tomate', '{"letter": "t", "chanting_video_src": "/raw/video/l2a1/letter-t.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 4.681, "duration": 3.098}, {"data": [{"end": 5.518, "anim": "talk", "start": 4.794}, {"end": 6.332, "anim": "talk", "start": 5.608}, {"end": 7.689, "anim": "talk", "start": 6.671}], "type": "animation-sequence", "track": 1, "offset": 4.681, "target": "senoravaca"}], "type": "parallel", "description": "Tomate, tomate, t-t-t!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 5.529, "duration": 0.916}, {"data": [{"end": 6.343, "anim": "talk", "start": 5.62}], "type": "animation-sequence", "track": 1, "offset": 5.529, "target": "senoravaca"}], "type": "parallel", "description": "Tomate"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('diamante', '{"letter": "d", "chanting_video_src": "/raw/video/l2a1/letter-d.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 8.56, "duration": 3.426}, {"data": [{"end": 9.555, "anim": "talk", "start": 8.616}, {"end": 10.335, "anim": "talk", "start": 9.588}, {"end": 11.872, "anim": "talk", "start": 10.719}], "type": "animation-sequence", "track": 1, "offset": 8.56, "target": "senoravaca"}], "type": "parallel", "description": "Diamante, diamante, d-d-d!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 9.532, "duration": 0.882}, {"data": [{"end": 10.335, "anim": "talk", "start": 9.555}], "type": "animation-sequence", "track": 1, "offset": 9.532, "target": "senoravaca"}], "type": "parallel", "description": "Diamante"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('rana', '{"letter": "r", "chanting_video_src": "/raw/video/l2a1/letter-r.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 12.585, "duration": 3.076}, {"data": [{"end": 13.512, "anim": "talk", "start": 12.664}, {"end": 14.247, "anim": "talk", "start": 13.636}, {"end": 15.593, "anim": "talk", "start": 14.484}], "type": "animation-sequence", "track": 1, "offset": 12.585, "target": "senoravaca"}], "type": "parallel", "description": "Rana, rana, r-r-r!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 13.535, "duration": 0.791}, {"data": [{"end": 14.236, "anim": "talk", "start": 13.614}], "type": "animation-sequence", "track": 1, "offset": 13.535, "target": "senoravaca"}], "type": "parallel", "description": "Rana"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('casa', '{"letter": "c", "chanting_video_src": "/raw/video/l2a1/letter-c.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 17.277, "duration": 3.37}, {"data": [{"end": 18.035, "anim": "talk", "start": 17.356}, {"end": 18.951, "anim": "talk", "start": 18.264}, {"end": 20.522, "anim": "talk", "start": 19.46}], "type": "animation-sequence", "track": 1, "offset": 17.277, "target": "senoravaca"}], "type": "parallel", "description": "Casa, casa, c-c-c!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 18.204, "duration": 0.859}, {"data": [{"end": 18.951, "anim": "talk", "start": 18.284}], "type": "animation-sequence", "track": 1, "offset": 18.204, "target": "senoravaca"}], "type": "parallel", "description": "Casa"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('niño', '{"letter": "n", "chanting_video_src": "/raw/video/l2a1/letter-n.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 25.758, "duration": 3.381}, {"data": [{"end": 26.662, "anim": "talk", "start": 25.871}, {"end": 27.499, "anim": "talk", "start": 26.764}, {"end": 29.059, "anim": "talk", "start": 27.94}], "type": "animation-sequence", "track": 1, "offset": 25.758, "target": "senoravaca"}], "type": "parallel", "description": "Niño, niño, n-n-n!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 26.764, "duration": 0.859}, {"data": [{"end": 27.499, "anim": "talk", "start": 26.764}], "type": "animation-sequence", "track": 1, "offset": 26.764, "target": "senoravaca"}], "type": "parallel", "description": "Niño"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('flor', '{"letter": "f", "chanting_video_src": "/raw/video/l2a1/letter-f.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 30.02, "duration": 3.053}, {"data": [{"end": 30.427, "anim": "talk", "start": 30.133}, {"end": 31.411, "anim": "talk", "start": 31.038}, {"end": 32.949, "anim": "talk", "start": 31.92}], "type": "animation-sequence", "track": 1, "offset": 30.02, "target": "senoravaca"}], "type": "parallel", "description": "Flor, flor, f-f-f!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 30.936, "duration": 0.577}, {"data": [{"end": 31.411, "anim": "talk", "start": 31.038}], "type": "animation-sequence", "track": 1, "offset": 30.936, "target": "senoravaca"}], "type": "parallel", "description": "Flor"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('bebe', '{"letter": "b", "chanting_video_src": "/raw/video/l2a1/letter-b.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 33.548, "duration": 3.37}, {"data": [{"end": 34.125, "anim": "talk", "start": 33.65}, {"end": 35.109, "anim": "talk", "start": 34.6}, {"end": 36.805, "anim": "talk", "start": 35.685}], "type": "animation-sequence", "track": 1, "offset": 33.548, "target": "senoravaca"}], "type": "parallel", "description": "Bebe, bebe, b-b-b!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 33.593, "duration": 0.633}, {"data": [{"end": 34.125, "anim": "talk", "start": 33.65}], "type": "animation-sequence", "track": 1, "offset": 33.593, "target": "senoravaca"}], "type": "parallel", "description": "Bebe"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('gato', '{"letter": "g", "chanting_video_src": "/raw/video/l2a1/letter-g.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 41.712, "duration": 3.109}, {"data": [{"end": 42.707, "anim": "talk", "start": 41.825}, {"end": 43.363, "anim": "talk", "start": 42.73}, {"end": 44.731, "anim": "talk", "start": 43.612}], "type": "animation-sequence", "track": 1, "offset": 37.37, "target": "senoravaca"}], "type": "parallel", "description": "Gato, gato, g-g-g!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 42.707, "duration": 0.712}, {"data": [{"end": 43.363, "anim": "talk", "start": 42.73}], "type": "animation-sequence", "track": 1, "offset": 42.707, "target": "senoravaca"}], "type": "parallel", "description": "Gato"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('jardín', '{"letter": "j", "chanting_video_src": "/raw/video/l2a1/letter-j.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 37.37, "duration": 3.268}, {"data": [{"end": 38.037, "anim": "talk", "start": 37.46}, {"end": 39.123, "anim": "talk", "start": 38.422}, {"end": 40.581, "anim": "talk", "start": 39.53}], "type": "animation-sequence", "track": 1, "offset": 37.37, "target": "senoravaca"}], "type": "parallel", "description": "Jardín, jardín, j-j-j!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", "type": "audio", "start": 37.37, "duration": 0.758}, {"data": [{"end": 38.037, "anim": "talk", "start": 37.46}], "type": "animation-sequence", "track": 1, "offset": 37.37, "target": "senoravaca"}], "type": "parallel", "description": "Jardín"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('chocolate', '{"letter": "ch", "chanting_video_src": "/raw/video/l2a1/letter-ch.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 5.429, "duration": 4.072}, {"data": [{"end": 6.431, "anim": "talk", "start": 5.502}, {"end": 7.611, "anim": "talk", "start": 6.651}, {"end": 9.417, "anim": "talk", "start": 8.175}], "type": "animation-sequence", "track": 1, "offset": 5.429, "target": "senoravaca"}], "type": "parallel", "description": "Chocolate, chocolate, ch-ch-ch!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 6.588, "duration": 1.128}, {"data": [{"end": 7.611, "anim": "talk", "start": 6.651}], "type": "animation-sequence", "track": 1, "offset": 6.588, "target": "senoravaca"}], "type": "parallel", "description": "Chocolate"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('ñandu', '{"letter": "ñ", "chanting_video_src": "/raw/video/l2a1/letter-ñ.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 10.399, "duration": 4.281}, {"data": [{"end": 11.286, "anim": "talk", "start": 10.524}, {"end": 12.675, "anim": "talk", "start": 11.819}, {"end": 14.586, "anim": "talk", "start": 13.354}], "type": "animation-sequence", "track": 1, "offset": 10.399, "target": "senoravaca"}], "type": "parallel", "description": "Ñandu, ñandu, ñ-ñ-ñ!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 11.725, "duration": 1.075}, {"data": [{"end": 12.675, "anim": "talk", "start": 11.819}], "type": "animation-sequence", "track": 1, "offset": 11.725, "target": "senoravaca"}], "type": "parallel", "description": "Ñandu"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('violin', '{"letter": "v", "chanting_video_src": "/raw/video/l2a1/letter-v.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 15.703, "duration": 3.957}, {"data": [{"end": 16.496, "anim": "talk", "start": 15.765}, {"end": 17.603, "anim": "talk", "start": 16.851}, {"end": 19.566, "anim": "talk", "start": 18.313}], "type": "animation-sequence", "track": 1, "offset": 15.703, "target": "senoravaca"}], "type": "parallel", "description": "Violin, violin, v-v-v!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 16.736, "duration": 0.981}, {"data": [{"end": 17.603, "anim": "talk", "start": 16.851}], "type": "animation-sequence", "track": 1, "offset": 16.736, "target": "senoravaca"}], "type": "parallel", "description": "Violin"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('llave', '{"letter": "ll", "chanting_video_src": "/raw/video/l2a1/letter-ll.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 20.422, "duration": 3.56}, {"data": [{"end": 21.602, "anim": "talk", "start": 20.526}, {"end": 22.343, "anim": "talk", "start": 21.623}, {"end": 23.867, "anim": "talk", "start": 22.74}], "type": "animation-sequence", "track": 1, "offset": 20.422, "target": "senoravaca"}], "type": "parallel", "description": "Llave, llave, ll-ll-ll!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 21.602, "duration": 0.835}, {"data": [{"end": 22.343, "anim": "talk", "start": 21.623}], "type": "animation-sequence", "track": 1, "offset": 21.602, "target": "senoravaca"}], "type": "parallel", "description": "Llave"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('hoja', '{"letter": "h", "chanting_video_src": "/raw/video/l2a1/letter-h.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 24.911, "duration": 1.827}, {"data": [{"end": 25.747, "anim": "talk", "start": 24.984}, {"end": 26.634, "anim": "talk", "start": 25.935}], "type": "animation-sequence", "track": 1, "offset": 24.911, "target": "senoravaca"}], "type": "parallel", "description": "Hoja, hoja!, h-h-h!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 25.872, "duration": 0.856}, {"data": [{"end": 26.634, "anim": "talk", "start": 25.935}], "type": "animation-sequence", "track": 1, "offset": 25.872, "target": "senoravaca"}], "type": "parallel", "description": "Hoja"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('queso', '{"letter": "q", "chanting_video_src": "/raw/video/l2a1/letter-q.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 29.202, "duration": 3.425}, {"data": [{"end": 30.173, "anim": "talk", "start": 29.296}, {"end": 30.936, "anim": "talk", "start": 30.299}, {"end": 32.46, "anim": "talk", "start": 31.332}], "type": "animation-sequence", "track": 1, "offset": 29.202, "target": "senoravaca"}], "type": "parallel", "description": "Queso, queso, qu-qu-qu!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 30.215, "duration": 0.814}, {"data": [{"end": 30.936, "anim": "talk", "start": 30.299}], "type": "animation-sequence", "track": 1, "offset": 30.215, "target": "senoravaca"}], "type": "parallel", "description": "Queso"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('zapato', '{"letter": "z", "chanting_video_src": "/raw/video/l2a1/letter-z.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 33.483, "duration": 3.842}, {"data": [{"end": 34.558, "anim": "talk", "start": 33.577}, {"end": 35.592, "anim": "talk", "start": 34.569}, {"end": 37.221, "anim": "talk", "start": 35.989}], "type": "animation-sequence", "track": 1, "offset": 33.483, "target": "senoravaca"}], "type": "parallel", "description": "Zapato, zapato, z-z-z!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 34.569, "duration": 1.117}, {"data": [{"end": 35.592, "anim": "talk", "start": 34.569}], "type": "animation-sequence", "track": 1, "offset": 34.569, "target": "senoravaca"}], "type": "parallel", "description": "Zapato"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('kimono', '{"letter": "k", "chanting_video_src": "/raw/video/l2a1/letter-k.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 38.683, "duration": 3.686}, {"data": [{"end": 39.758, "anim": "talk", "start": 38.776}, {"end": 40.739, "anim": "talk", "start": 39.821}, {"end": 42.18, "anim": "talk", "start": 41.063}], "type": "animation-sequence", "track": 1, "offset": 38.683, "target": "senoravaca"}], "type": "parallel", "description": "Kimono, kimono, k-k-k!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", "type": "audio", "start": 39.747, "duration": 1.086}, {"data": [{"end": 40.739, "anim": "talk", "start": 39.821}], "type": "animation-sequence", "track": 1, "offset": 39.747, "target": "senoravaca"}], "type": "parallel", "description": "Kimono"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('yoyo', '{"letter": "y", "chanting_video_src": "/raw/video/l2a1/letter-y.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_6.m4a", "type": "audio", "start": 5.757, "duration": 3.386}, {"data": [{"end": 6.861, "anim": "talk", "start": 5.888}, {"end": 7.496, "anim": "talk", "start": 6.863}, {"end": 8.997, "anim": "talk", "start": 7.864}], "type": "animation-sequence", "track": 1, "offset": 5.757, "target": "senoravaca"}], "type": "parallel", "description": "Yoyo, yoyo, y-y-y!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_6.m4a", "type": "audio", "start": 6.863, "duration": 0.738}, {"data": [{"end": 7.496, "anim": "talk", "start": 6.863}], "type": "animation-sequence", "track": 1, "offset": 6.863, "target": "senoravaca"}], "type": "parallel", "description": "Yoyo"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('xilofono', '{"letter": "x", "chanting_video_src": "/raw/video/l2a1/letter-x.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_6.m4a", "type": "audio", "start": 10.025, "duration": 3.662}, {"data": [{"end": 11.092, "anim": "talk", "start": 10.13}, {"end": 12.067, "anim": "talk", "start": 11.158}, {"end": 13.582, "anim": "talk", "start": 12.422}], "type": "animation-sequence", "track": 1, "offset": 10.025, "target": "senoravaca"}], "type": "parallel", "description": "Xilofono, xilofono, x-x-x,!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_6.m4a", "type": "audio", "start": 11.079, "duration": 1.12}, {"data": [{"end": 12.067, "anim": "talk", "start": 11.158}], "type": "animation-sequence", "track": 1, "offset": 11.079, "target": "senoravaca"}], "type": "parallel", "description": "Xilofono"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));

--;;

INSERT INTO dataset_items(name, data, dataset_id)
VALUES ('web', '{"letter": "w", "chanting_video_src": "/raw/video/l2a1/letter-w.mp4", "vaca_chanting_song": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_6.m4a", "type": "audio", "start": 1.541, "duration": 3.609}, {"data": [{"end": 2.503, "anim": "talk", "start": 1.713}, {"end": 3.254, "anim": "talk", "start": 2.832}, {"end": 4.993, "anim": "talk", "start": 3.82}], "type": "animation-sequence", "track": 1, "offset": 1.541, "target": "senoravaca"}], "type": "parallel", "description": "Sitio Web, Web w-w-w!"}, "vaca_chanting_word": {"data": [{"id": "/raw/audio/l2/a1/L2_A1_Vaca_6.m4a", "type": "audio", "start": 2.727, "duration": 0.619}, {"data": [{"end": 3.254, "anim": "talk", "start": 2.832}], "type": "animation-sequence", "track": 1, "offset": 2.727, "target": "senoravaca"}], "type": "parallel", "description": "Web"}}',
(SELECT id
FROM datasets
WHERE name='concepts'));
