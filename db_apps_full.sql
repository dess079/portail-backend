-- Full DB initialization script for db_apps (consolidated from all portal-backend SQL scripts)
--
-- Usage:
-- 1) Run against a PostgreSQL server that you can access (psql recommended).
-- 2) If you are not connected to db_apps yet, run:
--      psql -U <user> -d postgres -f db_apps_full.sql
--    or first connect to postgres, then run:
--      psql -U <user> -d db_apps -f db_apps_full.sql
--
-- This script creates the db_apps database (if missing), creates all needed tables,
-- and inserts default applications / environments / themes so the portal can list them.

-- ------------------------------------------------------------
-- 1) Create db_apps database (if it does not exist)
-- ------------------------------------------------------------
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'db_apps') THEN
        CREATE DATABASE db_apps;
    END IF;
END
$$;

-- ------------------------------------------------------------
-- 2) Schema + tables
-- ------------------------------------------------------------

-- Note: Run the remainder of this script while connected to db_apps.

-- =================================================================
-- Table: TB_APP_APPLICATION_TYPE (types of applications)
-- =================================================================
CREATE TABLE IF NOT EXISTS TB_APP_APPLICATION_TYPE (
    code SMALLINT PRIMARY KEY,
    type_name TEXT NOT NULL,
    description TEXT
);

-- =================================================================
-- Table: TB_APP_APPLICATIONS (applications registry)
-- =================================================================
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
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    app_type_code SMALLINT NOT NULL DEFAULT 3
);

-- Indexes for application queries
CREATE INDEX IF NOT EXISTS idx_app_status ON TB_APP_APPLICATIONS(status);
CREATE INDEX IF NOT EXISTS idx_app_code ON TB_APP_APPLICATIONS(code);
CREATE INDEX IF NOT EXISTS idx_app_created_date ON TB_APP_APPLICATIONS(created_date);
CREATE INDEX IF NOT EXISTS idx_tb_app_applications_app_type ON TB_APP_APPLICATIONS(app_type_code);

-- =================================================================
-- Table: TB_ENV_ENVIRONNEMENT (environments)
-- =================================================================
CREATE TABLE IF NOT EXISTS TB_ENV_ENVIRONNEMENT (
    env_id SERIAL PRIMARY KEY,
    env_code TEXT NOT NULL UNIQUE,
    env_nom TEXT NOT NULL,
    env_description TEXT
);

-- =================================================================
-- Table: TB_APP_ENV_LIAISON (applications <-> environments)
-- =================================================================
CREATE TABLE IF NOT EXISTS TB_APP_ENV_LIAISON (
    app_code TEXT NOT NULL REFERENCES TB_APP_APPLICATIONS(code) ON DELETE CASCADE,
    env_id INTEGER NOT NULL REFERENCES TB_ENV_ENVIRONNEMENT(env_id) ON DELETE CASCADE,
    url TEXT,
    PRIMARY KEY (app_code, env_id)
);

-- =================================================================
-- Tables for UI themes and preferences
-- =================================================================
CREATE TABLE IF NOT EXISTS TB_UI_THEME_TYPES (
    id SERIAL PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL DEFAULT 'ACTIVE',
    created_by TEXT NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by TEXT,
    last_modified_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS TB_UI_THEMES (
    id SERIAL PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    theme_type_id INTEGER NOT NULL REFERENCES TB_UI_THEME_TYPES(id),
    theme_definition JSONB NOT NULL,
    status TEXT NOT NULL DEFAULT 'ACTIVE',
    order_index INTEGER NOT NULL DEFAULT 0,
    created_by TEXT NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by TEXT,
    last_modified_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS TB_UI_THEME_TRANSLATIONS (
    id SERIAL PRIMARY KEY,
    theme_id INTEGER NOT NULL REFERENCES TB_UI_THEMES(id) ON DELETE CASCADE,
    language_code TEXT NOT NULL,
    name TEXT NOT NULL,
    UNIQUE(theme_id, language_code)
);

CREATE TABLE IF NOT EXISTS TB_USER_PREFERENCES (
    id SERIAL PRIMARY KEY,
    user_id TEXT NOT NULL UNIQUE,
    language TEXT NOT NULL DEFAULT 'fr',
    theme TEXT NOT NULL DEFAULT 'default',
    created_date TIMESTAMP DEFAULT NOW(),
    last_modified_date TIMESTAMP DEFAULT NOW()
);

-- =================================================================
-- Triggers to keep last_modified_date up to date
-- =================================================================
CREATE OR REPLACE FUNCTION update_last_modified_date_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_modified_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS update_tb_app_applications_last_modified_date ON TB_APP_APPLICATIONS;
CREATE TRIGGER update_tb_app_applications_last_modified_date
    BEFORE UPDATE ON TB_APP_APPLICATIONS
    FOR EACH ROW EXECUTE FUNCTION update_last_modified_date_column();

DROP TRIGGER IF EXISTS update_tb_ui_themes_last_modified_date ON TB_UI_THEMES;
CREATE TRIGGER update_tb_ui_themes_last_modified_date
    BEFORE UPDATE ON TB_UI_THEMES
    FOR EACH ROW EXECUTE FUNCTION update_last_modified_date_column();

DROP TRIGGER IF EXISTS update_tb_ui_theme_types_last_modified_date ON TB_UI_THEME_TYPES;
CREATE TRIGGER update_tb_ui_theme_types_last_modified_date
    BEFORE UPDATE ON TB_UI_THEME_TYPES
    FOR EACH ROW EXECUTE FUNCTION update_last_modified_date_column();

-- =================================================================
-- Default reference data (apps, environments, themes)
-- =================================================================

-- Application types
INSERT INTO TB_APP_APPLICATION_TYPE (code, type_name, description)
VALUES
    (1, 'backend', 'Applications backend (API, services)'),
    (2, 'frontend', 'Applications frontend (UI, web apps)'),
    (3, 'other', 'Autres types d''application')
ON CONFLICT (code) DO NOTHING;

-- Environments
INSERT INTO TB_ENV_ENVIRONNEMENT (env_code, env_nom, env_description)
VALUES
    ('local', 'Local', 'Environnement de développement local'),
    ('container', 'Container', 'Environnement Docker/Container'),
    ('cloud', 'Cloudflared', 'Environnement exposé via Cloudflared')
ON CONFLICT (env_code) DO NOTHING;

-- Applications
INSERT INTO TB_APP_APPLICATIONS (
    code, name, description, url, status, order_index, created_by, created_date, last_modified_by, last_modified_date, app_type_code
) VALUES
    ('lottosylvain', 'LottoSylvain UI', 'Interface de loterie avec graphiques', 'http://localhost:3001', 'ACTIVE', 2, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('sed', 'SED Frontend', 'Interface de recherche et exploration', 'http://localhost:3002', 'ACTIVE', 3, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('directai-frontend', 'DirectAI Admin', 'Administration des agents IA', 'http://localhost:3003', 'ACTIVE', 4, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('planificateur', 'Planificateur', 'Plateforme de conseil en placement assistée par IA (REER, CELI, REEE, FERR)', 'http://localhost:3005', 'ACTIVE', 5, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('orchestra', 'Orchestra', 'Plateforme d''import de données assistée par IA (schéma, DDL, import CSV/JSON)', 'http://localhost:3200', 'ACTIVE', 6, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('cabinet', 'Cabinet Dentaire', 'App RDV cabinet dentaire', 'http://localhost:4000', 'ACTIVE', 7, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('prix-concurrents', 'Prix Concurrents', 'Comparaison des prix concurrents', 'http://localhost:3030', 'ACTIVE', 8, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('cabinet-portal', 'Portail Cabinet', 'Portail interne du cabinet dentaire', 'http://localhost:3020', 'ACTIVE', 9, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 2),
    ('portail-backend', 'Portail Backend', 'API principale du portail', 'http://localhost:8081', 'ACTIVE', 5, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 1),
    ('lottoquebec-backend', 'LottoQuebec Backend', 'API de gestion des tirages LottoQuebec', 'http://localhost:8080', 'ACTIVE', 6, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 1),
    ('sed-backend', 'SED Backend', 'API de recherche et vectorisation', 'http://localhost:8082', 'ACTIVE', 7, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 1),
    ('directai-backend', 'DirectAI Backend', 'API d''orchestration et IA prédictive', 'http://localhost:8083', 'ACTIVE', 8, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 1)
ON CONFLICT (code) DO UPDATE
    SET name = EXCLUDED.name,
        description = EXCLUDED.description,
        url = EXCLUDED.url,
        status = EXCLUDED.status,
        order_index = EXCLUDED.order_index,
        last_modified_by = EXCLUDED.last_modified_by,
        last_modified_date = EXCLUDED.last_modified_date,
        app_type_code = EXCLUDED.app_type_code;

-- Application ↔ Environnement liaison (URLs per environment)
INSERT INTO TB_APP_ENV_LIAISON (app_code, env_id, url)
VALUES
    -- LOCAL
    ('lottosylvain', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3001'),
    ('sed', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3002'),
    ('directai-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3003'),
    ('planificateur', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3005'),
    ('orchestra', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3200'),
    ('cabinet', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:4000'),
    ('prix-concurrents', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3030'),
    ('cabinet-portal', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:3020'),
    ('portail-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:8081'),
    ('lottoquebec-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:8080'),
    ('sed-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:8082'),
    ('directai-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'local'), 'http://localhost:8083'),
    -- CONTAINER
    ('lottosylvain', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3011'),
    ('sed', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3012'),
    ('directai-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3013'),
    ('planificateur', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3001'),
    ('orchestra', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3200'),
    ('cabinet', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:4000'),
    ('prix-concurrents', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3030'),
    ('cabinet-portal', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:3020'),
    ('portail-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:8091'),
    ('lottoquebec-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:8090'),
    ('sed-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:8092'),
    ('directai-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'container'), 'http://localhost:8093'),
    -- CLOUD
    ('lottosylvain', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://lottosylvain-frontend.sdfullstackdev.ca'),
    ('sed', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://sed-frontend.sdfullstackdev.ca'),
    ('directai-frontend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://directai-frontend.sdfullstackdev.ca'),
    ('planificateur', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://planificateur-frontend.sdfullstackdev.ca'),
    ('orchestra', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://orchestra-frontend.sdfullstackdev.ca'),
    ('cabinet', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://cabinet-frontend.sdfullstackdev.ca'),
    ('prix-concurrents', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://prix-concurrents-frontend.sdfullstackdev.ca'),
    ('cabinet-portal', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://portal.sdfullstackdev.ca'),
    ('portail-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://portail-backend.sdfullstackdev.ca'),
    ('lottoquebec-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://lottoquebec-backend.sdfullstackdev.ca'),
    ('sed-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://sed-backend.sdfullstackdev.ca'),
    ('directai-backend', (SELECT env_id FROM TB_ENV_ENVIRONNEMENT WHERE env_code = 'cloud'), 'https://directai-backend.sdfullstackdev.ca')
ON CONFLICT (app_code, env_id) DO UPDATE
    SET url = EXCLUDED.url;

-- =================================================================
-- Themes (MUI)
-- =================================================================
INSERT INTO TB_UI_THEME_TYPES (code, name, description, status, created_by, created_date, last_modified_by, last_modified_date)
VALUES
    ('MUI', 'Material-UI', 'Thèmes basés sur Material Design', 'ACTIVE', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP)
ON CONFLICT (code) DO NOTHING;

INSERT INTO TB_UI_THEMES (code, name, theme_type_id, theme_definition, status, order_index, created_by, created_date, last_modified_by, last_modified_date)
VALUES
    (
        'DEFAULT',
        'Thème par défaut',
        (SELECT id FROM TB_UI_THEME_TYPES WHERE code = 'MUI'),
        '{
            "palette": {
                "mode": "light",
                "primary": { "main": "#1976d2" },
                "secondary": { "main": "#dc004e" }
            }
        }'::jsonb,
        'ACTIVE',
        1,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    ),
    (
        'DARK',
        'Thème sombre',
        (SELECT id FROM TB_UI_THEME_TYPES WHERE code = 'MUI'),
        '{
            "palette": {
                "mode": "dark",
                "primary": { "main": "#90caf9" },
                "secondary": { "main": "#f48fb1" },
                "background": { "default": "#121212", "paper": "#1e1e1e" }
            }
        }'::jsonb,
        'ACTIVE',
        2,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    )
ON CONFLICT (code) DO NOTHING;

INSERT INTO TB_UI_THEME_TRANSLATIONS (theme_id, language_code, name)
VALUES
    ((SELECT id FROM TB_UI_THEMES WHERE code = 'DEFAULT'), 'fr', 'Thème par défaut'),
    ((SELECT id FROM TB_UI_THEMES WHERE code = 'DEFAULT'), 'en', 'Default Theme'),
    ((SELECT id FROM TB_UI_THEMES WHERE code = 'DARK'), 'fr', 'Thème sombre'),
    ((SELECT id FROM TB_UI_THEMES WHERE code = 'DARK'), 'en', 'Dark Theme')
ON CONFLICT (theme_id, language_code) DO NOTHING;
