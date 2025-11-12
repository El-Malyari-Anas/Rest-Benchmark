
INSERT INTO category (id, name) VALUES (1, 'Électronique') ON CONFLICT (id) DO NOTHING;
INSERT INTO category (id, name) VALUES (2, 'Livres') ON CONFLICT (id) DO NOTHING;
INSERT INTO category (id, name) VALUES (3, 'Mobilier') ON CONFLICT (id) DO NOTHING;

ALTER SEQUENCE category_id_seq RESTART WITH 4;