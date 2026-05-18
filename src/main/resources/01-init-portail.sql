
DROP TABLE IF EXISTS TB_APP_ENV_LIAISON CASCADE;
-- Suppression de la table des environnements
DROP TABLE IF EXISTS TB_ENV_ENVIRONNEMENT CASCADE;

-- Suppression de la table des applications si elle existe
DROP TABLE IF EXISTS TB_APP_APPLICATIONS CASCADE;

-- Table des applications (structure conforme à PORTS.md)
CREATE TABLE IF NOT EXISTS TB_APP_APPLICATIONS (
    code TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    url TEXT NOT NULL,
    icon TEXT,
    status TEXT NOT NULL DEFAULT 'ACTIVE',
    order_index INTEGER NOT NULL DEFAULT 0,
    created_by TEXT NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by TEXT,
    last_modified_date TIMESTAMP
);


CREATE TABLE IF NOT EXISTS TB_ENV_ENVIRONNEMENT (
    env_id SERIAL PRIMARY KEY,
    env_code TEXT NOT NULL UNIQUE,
    env_nom TEXT NOT NULL,
    env_description TEXT
);

-- Table de liaison application <-> environnement
CREATE TABLE IF NOT EXISTS TB_APP_ENV_LIAISON (
    app_code TEXT NOT NULL REFERENCES TB_APP_APPLICATIONS(code) ON DELETE CASCADE,
    env_id INTEGER NOT NULL REFERENCES TB_ENV_ENVIRONNEMENT(env_id) ON DELETE CASCADE,
    url TEXT,
    PRIMARY KEY (app_code, env_id)
);


INSERT INTO TB_ENV_ENVIRONNEMENT (env_code, env_nom, env_description) VALUES
    ('local', 'Local', 'Environnement de développement local'),
    ('container', 'Container', 'Environnement Docker/Container'),
    ('cloud', 'Cloudflared', 'Environnement exposé via Cloudflared');


-- Insertion des applications (doit précéder la liaison)
INSERT INTO TB_APP_APPLICATIONS (code, name, description, url, status, order_index, created_by)
VALUES
    ('portail-frontend', 'Portail', 'Interface principale de navigation', 'http://localhost:3000', 'ACTIVE', 1, 'admin'),
    ('lottosylvain', 'LottoSylvain UI', 'Interface de loterie avec graphiques', 'http://localhost:3001', 'ACTIVE', 2, 'admin'),
    ('sed', 'SED Frontend', 'Interface de recherche et exploration', 'http://localhost:3002', 'ACTIVE', 3, 'admin'),
    ('directai-frontend', 'DirectAI Admin', 'Administration des agents IA', 'http://localhost:3003', 'ACTIVE', 4, 'admin'),
    ('portail-backend', 'Portail Backend', 'API principale du portail', 'http://localhost:8081', 'ACTIVE', 5, 'admin'),
    ('lottoquebec-backend', 'LottoQuebec Backend', 'API de gestion des tirages LottoQuebec', 'http://localhost:8080', 'ACTIVE', 6, 'admin'),
    ('sed-backend', 'SED Backend', 'API de recherche et vectorisation', 'http://localhost:8082', 'ACTIVE', 7, 'admin'),
    ('directai-backend', 'DirectAI Backend', 'API d''orchestration et IA prédictive', 'http://localhost:8083', 'ACTIVE', 8, 'admin');





-- Liaison applications ↔ environnement via la table de liaison
-- LOCAL
INSERT INTO TB_APP_ENV_LIAISON (app_code, env_id, url) VALUES
    ('portail-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3000'),
    ('lottosylvain', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3001'),
    ('sed', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3002'),
    ('directai-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3003'),
    ('portail-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:8081'),
    ('lottoquebec-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:8080'),
    ('sed-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:8082'),
    ('directai-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:8083');

-- CONTAINER
INSERT INTO TB_APP_ENV_LIAISON (app_code, env_id, url) VALUES
    ('portail-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3010'),
    ('lottosylvain', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3011'),
    ('sed', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3012'),
    ('directai-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3013'),
    ('portail-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:8091'),
    ('lottoquebec-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:8090'),
    ('sed-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:8092'),
    ('directai-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:8093');

-- CLOUD
INSERT INTO TB_APP_ENV_LIAISON (app_code, env_id, url) VALUES
    ('portail-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://portail-frontend.sdfullstackdev.ca'),
    ('lottosylvain', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://lottosylvain-frontend.sdfullstackdev.ca'),
    ('sed', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://sed-frontend.sdfullstackdev.ca'),
    ('directai-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://directai-frontend.sdfullstackdev.ca'),
    ('portail-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://portail-backend.sdfullstackdev.ca'),
    ('lottoquebec-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://lottoquebec-backend.sdfullstackdev.ca'),
    ('sed-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://sed-backend.sdfullstackdev.ca'),
    ('directai-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://directai-backend.sdfullstackdev.ca');
