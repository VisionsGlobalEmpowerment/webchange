UPDATE datasets
SET scheme = '{"fields": [{"name": "home-vaca-this-is", "type": "string"}, {"name": "home-vaca-question", "type": "string"}, {"name": "home-vaca-word", "type": "string"}, {"name": "home-group-word", "type": "string"}, {"name": "home-vaca-3-times", "type": "string"}, {"name": "home-group-3-times", "type": "string"}, {"name": "home-vaca-goodbye", "type": "string"}, {"name": "skin", "type": "string"}, {"name": "seesaw-voice-low", "type": "string"}, {"name": "seesaw-voice-high", "type": "string"}, {"name": "swings-dialog", "type": "string"}, {"name": "sandbox-this-is-letter", "type": "string"}, {"name": "sandbox-state-word-1", "type": "string"}, {"name": "sandbox-state-word-2", "type": "string"}, {"name": "sandbox-state-word-3", "type": "string"}, {"name": "sandbox-state-word-4", "type": "string"}, {"name": "concept-name", "type": "string"}, {"name": "sandbox-this-is-letter-action", "type": "action"}, {"name": "sandbox-state-word-1-action", "type": "action"}, {"name": "sandbox-state-word-2-action", "type": "action"}, {"name": "sandbox-state-word-3-action", "type": "action"}, {"name": "sandbox-state-word-4-action", "type": "action"}, {"name": "sandbox-change-skin-1-action", "type": "action"}, {"name": "sandbox-change-skin-2-action", "type": "action"}, {"name": "sandbox-change-skin-3-action", "type": "action"}, {"name": "sandbox-change-skin-4-action", "type": "action"}]}'
WHERE id = 2;

--;;

UPDATE dataset_items
SET data = '{"skin": "squirrel", "test-new": {"id": "init", "type": "action", "scene-id": "sandbox", "description": "Really long description for this action to test label"}, "concept-name": "ardilla", "swings-dialog": "dialog-ardilla", "home-vaca-word": "word-ardilla", "home-group-word": "group-word-ardilla", "seesaw-voice-low": "word-ardilla-low", "home-vaca-3-times": "vaca-3-times-ardilla", "home-vaca-goodbye": "goodbye-ardilla", "home-vaca-this-is": "this-is-ardilla", "seesaw-voice-high": "word-ardilla-high", "home-group-3-times": "group-3-times-ardilla", "home-vaca-question": "question-ardilla", "sandbox-state-word-1": "abeja", "sandbox-state-word-2": "arbol", "sandbox-state-word-3": "avion", "sandbox-state-word-4": "arana", "sandbox-this-is-letter": "mari-this-is-letter-a", "sandbox-state-word-1-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Abeja"}, "target": "word"}, {"id": "voice-1", "type": "audio", "start": 6.218, "duration": 0.871}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-abeja"}, "sandbox-state-word-2-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Arbol"}, "target": "word"}, {"id": "voice-1", "type": "audio", "start": 7.873, "duration": 0.871}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-arbol"}, "sandbox-state-word-3-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Avion"}, "target": "word"}, {"id": "voice-1", "type": "audio", "start": 9.506, "duration": 0.871}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-avion"}, "sandbox-state-word-4-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Arana"}, "target": "word"}, {"id": "voice-1", "type": "audio", "start": 11.144, "duration": 1.06}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-arana"}, "sandbox-change-skin-1-action": {"skin": "bee", "type": "set-skin", "target": "box1", "scene-id": "sandbox", "description": ":box-1-set-skin-abeja"}, "sandbox-change-skin-2-action": {"skin": "tree", "type": "set-skin", "target": "box2", "scene-id": "sandbox", "description": ":box-1-set-skin-arbol"}, "sandbox-change-skin-3-action": {"skin": "airplane", "type": "set-skin", "target": "box3", "scene-id": "sandbox", "description": ":box-1-set-skin-avion"}, "sandbox-change-skin-4-action": {"skin": "spider", "type": "set-skin", "target": "box4", "scene-id": "sandbox", "description": ":box-1-set-skin-arana"}, "sandbox-this-is-letter-action": {"data": [{"end": 16.639, "anim": "talk", "start": 14.552}, {"end": 18.999, "anim": "talk", "start": 17.341}, {"end": 20.475, "anim": "talk", "start": 19.373}, {"end": 21.997, "anim": "talk", "start": 20.885}, {"end": 30.417, "anim": "talk", "start": 23.173}], "type": "animation-sequence", "audio": "/raw/audio/l1/a4/Mari_Level1_Activity4.m4a", "start": "14.397", "track": "1", "offset": "14.397", "target": "mari", "duration": "16.202", "scene-id": "sandbox", "description": ":mari-this-is-letter-a"}}'
WHERE id = 7;

--;;

UPDATE dataset_items
SET data = '{"skin": "bear", "concept-name": "oso", "swings-dialog": "dialog-oso", "home-vaca-word": "word-oso", "home-group-word": "group-word-oso", "seesaw-voice-low": "word-oso-low", "home-vaca-3-times": "vaca-3-times-oso", "home-vaca-goodbye": "goodbye-oso", "home-vaca-this-is": "this-is-oso", "seesaw-voice-high": "word-oso-high", "home-group-3-times": "group-3-times-oso", "home-vaca-question": "question-oso", "sandbox-state-word-1": "ocho", "sandbox-state-word-2": "oreja", "sandbox-state-word-3": "oveja", "sandbox-state-word-4": "ojos", "sandbox-this-is-letter": "mari-this-is-letter-o", "sandbox-state-word-1-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Ocho"}, "target": "word"}, {"id": "voice-2", "type": "audio", "start": 6.496, "duration": 0.864}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-ocho"}, "sandbox-state-word-2-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Oreja"}, "target": "word"}, {"id": "voice-2", "type": "audio", "start": 7.532, "duration": 1.017}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-oreja"}, "sandbox-state-word-3-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Oveja"}, "target": "word"}, {"id": "voice-2", "type": "audio", "start": 8.511, "duration": 1.103}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-oveja"}, "sandbox-state-word-4-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Ojos"}, "target": "word"}, {"id": "voice-2", "type": "audio", "start": 9.873, "duration": 0.835}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-ojos"}, "sandbox-change-skin-1-action": {"skin": "8", "type": "set-skin", "target": "box1", "scene-id": "sandbox", "description": ":box-1-set-skin-ocho"}, "sandbox-change-skin-2-action": {"skin": "ear", "type": "set-skin", "target": "box2", "scene-id": "sandbox", "description": ":box-1-set-skin-oreja"}, "sandbox-change-skin-3-action": {"skin": "sheep", "type": "set-skin", "target": "box3", "scene-id": "sandbox", "description": ":box-1-set-skin-oveja"}, "sandbox-change-skin-4-action": {"skin": "eyes", "type": "set-skin", "target": "box4", "scene-id": "sandbox", "description": ":box-1-set-skin-ojos"}, "sandbox-this-is-letter-action": {"data": [{"end": 61.472, "anim": "talk", "start": 53.717}, {"end": 69.746, "anim": "talk", "start": 62.374}], "type": "animation-sequence", "audio": "/raw/audio/l1/a4/Mari_Level1_Activity4.m4a", "start": "53.626", "track": "1", "offset": "53.626", "target": "mari", "duration": "16.284", "scene-id": "sandbox", "description": ":mari-this-is-letter-o"}}'
WHERE id = 8;

--;;

UPDATE dataset_items
SET data = '{"skin": "magnet", "concept-name": "iman", "swings-dialog": "dialog-incendio", "home-vaca-word": "word-incendio", "home-group-word": "group-word-incendio", "seesaw-voice-low": "word-incendio-low", "home-vaca-3-times": "vaca-3-times-incendio", "home-vaca-goodbye": "goodbye-incendio", "home-vaca-this-is": "this-is-incendio", "seesaw-voice-high": "word-incendio-high", "home-group-3-times": "group-3-times-incendio", "home-vaca-question": "question-incendio", "sandbox-state-word-1": "iguana", "sandbox-state-word-2": "incendio", "sandbox-state-word-3": "insecto", "sandbox-state-word-4": "isla", "sandbox-this-is-letter": "mari-this-is-letter-i", "sandbox-state-word-1-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Iguana"}, "target": "word"}, {"id": "voice-3", "type": "audio", "start": 4.828, "duration": 1.27}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-iguana"}, "sandbox-state-word-2-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Incendio"}, "target": "word"}, {"id": "voice-3", "type": "audio", "start": 6.591, "duration": 1.375}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-incendio"}, "sandbox-state-word-3-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Insecto"}, "target": "word"}, {"id": "voice-3", "type": "audio", "start": 8.699, "duration": 1.465}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-insecto"}, "sandbox-state-word-4-action": {"data": [{"id": "show", "type": "state", "params": {"text": "Isla"}, "target": "word"}, {"id": "voice-3", "type": "audio", "start": 10.911, "duration": 1.151}, {"id": "default", "type": "state", "target": "word"}], "type": "sequence-data", "scene-id": "sandbox", "description": ":sandbox-state-word-isla"}, "sandbox-change-skin-1-action": {"skin": "iguana", "type": "set-skin", "target": "box1", "scene-id": "sandbox", "description": ":box-1-set-skin-iguana"}, "sandbox-change-skin-2-action": {"skin": "fire", "type": "set-skin", "target": "box2", "scene-id": "sandbox", "description": ":box-1-set-skin-incendio"}, "sandbox-change-skin-3-action": {"skin": "insect", "type": "set-skin", "target": "box3", "scene-id": "sandbox", "description": ":box-1-set-skin-insecto"}, "sandbox-change-skin-4-action": {"skin": "island", "type": "set-skin", "target": "box4", "scene-id": "sandbox", "description": ":box-1-set-skin-isla"}, "sandbox-this-is-letter-action": {"data": [{"end": 86.148, "anim": "talk", "start": 83.942}, {"end": 92.025, "anim": "talk", "start": 86.795}, {"end": 100.281, "anim": "talk", "start": 93.228}], "type": "animation-sequence", "audio": "/raw/audio/l1/a4/Mari_Level1_Activity4.m4a", "start": "83.778", "track": "1", "offset": "83.778", "target": "mari", "duration": "16.63", "scene-id": "sandbox", "description": ":mari-this-is-letter-i"}}'
WHERE id = 9;

--;;

DELETE FROM dataset_items
WHERE id = 11;

--;;

DELETE FROM dataset_items
WHERE id = 12;

--;;

DELETE FROM dataset_items
WHERE id = 13;

--;;

DELETE FROM dataset_items
WHERE id = 14;

--;;

DELETE FROM dataset_items
WHERE id = 15;

--;;

DELETE FROM dataset_items
WHERE id = 16;

--;;

DELETE FROM lesson_sets
WHERE id = 5;
