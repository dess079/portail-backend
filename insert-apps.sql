-- ===================================================================
-- INSERT des 4 applications frontend pour db_apps
-- ===================================================================

-- 1. S'assurer que les types d'application existent
INSERT INTO TB_APP_APPLICATION_TYPE (code, type_name, description)
VALUES
    (1, 'backend', 'Applications backend (API, services)'),
    (2, 'frontend', 'Applications frontend (UI, web apps)'),
    (3, 'other', 'Autres types d''application')
ON CONFLICT (code) DO NOTHING;

-- 2. S'assurer que les environnements existent
INSERT INTO TB_ENV_ENVIRONNEMENT (env_code, env_nom, env_description)
VALUES
    ('local', 'Local', 'Environnement de développement local'),
    ('container', 'Container', 'Environnement Docker/Container'),
    ('cloud', 'Cloudflared', 'Environnement exposé via Cloudflared')
ON CONFLICT (env_code) DO NOTHING;

-- 3. Insérer les 4 applications frontend
INSERT INTO TB_APP_APPLICATIONS (
    code, name, description, url, status, order_index, created_by, created_date, last_modified_by, last_modified_date, app_type_code
) VALUES
    ('lottosylvain', 'LottoSylvain UI', 'Interface de loterie avec graphiques', 'http://localhost:3001', 'ACTIVE', 2, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('sed', 'SED Frontend', 'Interface de recherche et exploration', 'http://localhost:3002', 'ACTIVE', 3, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('directai-frontend', 'DirectAI Admin', 'Administration des agents IA', 'http://localhost:3003', 'ACTIVE', 4, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('planificateur', 'Planificateur', 'Plateforme de conseil en placement assistée par IA (REER, CELI, REEE, FERR)', 'http://localhost:3005', 'ACTIVE', 5, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2)
ON CONFLICT (code) DO UPDATE
    SET name = EXCLUDED.name,
        description = EXCLUDED.description,
        url = EXCLUDED.url,
        status = EXCLUDED.status,
        order_index = EXCLUDED.order_index,
        last_modified_by = EXCLUDED.last_modified_by,
        last_modified_date = EXCLUDED.last_modified_date,
        app_type_code = EXCLUDED.app_type_code;

-- 4. Insérer les liaisons app <-> environnement avec les URLs adaptées
INSERT INTO TB_APP_ENV_LIAISON (app_code, env_id, url)
VALUES
    -- LOTTOSYLVAIN FRONTEND
    ('lottosylvain', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3001'),
    ('lottosylvain', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3011'),
    ('lottosylvain', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://lottosylvain-frontend.sdfullstackdev.ca'),
    
    -- SED FRONTEND
    ('sed', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3002'),
    ('sed', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3012'),
    ('sed', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://sed-frontend.sdfullstackdev.ca'),
    
    -- DIRECTAI FRONTEND
    ('directai-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3003'),
    ('directai-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3013'),
    ('directai-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://directai-frontend.sdfullstackdev.ca'),
    
    -- PLANIFICATEUR FRONTEND
    ('planificateur', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3005'),
    ('planificateur', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3001'),
    ('planificateur', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://planificateur-frontend.sdfullstackdev.ca'),

    -- ORCHESTRA FRONTEND
    ('orchestra', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3200'),
    ('orchestra', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3200'),
    ('orchestra', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://orchestra-frontend.sdfullstackdev.ca'),

    -- CABINET DENTAIRE FRONTEND (port hôte 4000 — voir config-cabinet-frontend.yml)
    ('cabinet', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:4000'),
    ('cabinet', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:4000'),
    ('cabinet', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://cabinet-frontend.sdfullstackdev.ca'),

    -- PRIX CONCURRENTS FRONTEND
    ('prix-concurrents', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3030'),
    ('prix-concurrents', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3030'),
    ('prix-concurrents', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://prix-concurrents-frontend.sdfullstackdev.ca'),

    -- PORTAIL CABINET (hostname cloud : portal.sdfullstackdev.ca)
    ('cabinet-portal', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3020'),
    ('cabinet-portal', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3020'),
    ('cabinet-portal', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://portal.sdfullstackdev.ca')
ON CONFLICT (app_code, env_id) DO UPDATE
    SET url = EXCLUDED.url;
