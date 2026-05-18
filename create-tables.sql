-- Création des tables pour les thèmes MUI

-- Supprimer les tables dans l'ordre inverse des dépendances
DROP TABLE IF EXISTS TB_USER_PREFERENCES;
DROP TABLE IF EXISTS TB_UI_THEME_TRANSLATIONS;
DROP TABLE IF EXISTS TB_UI_THEMES;
DROP TABLE IF EXISTS TB_UI_THEME_TYPES;

-- Type de thème
CREATE TABLE IF NOT EXISTS TB_UI_THEME_TYPES (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_by VARCHAR(50),
    created_date TIMESTAMP DEFAULT NOW()
);

-- Thèmes
CREATE TABLE IF NOT EXISTS TB_UI_THEMES (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    theme_type_id INTEGER REFERENCES TB_UI_THEME_TYPES(id),
    theme_definition TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    order_index INTEGER DEFAULT 0,
    created_by VARCHAR(50),
    created_date TIMESTAMP DEFAULT NOW()
);

-- Traductions des thèmes
CREATE TABLE IF NOT EXISTS TB_UI_THEME_TRANSLATIONS (
    id SERIAL PRIMARY KEY,
    theme_id INTEGER REFERENCES TB_UI_THEMES(id) ON DELETE CASCADE,
    language_code VARCHAR(10) NOT NULL,
    name VARCHAR(200) NOT NULL,
    created_by VARCHAR(50),
    created_date TIMESTAMP DEFAULT NOW(),
    UNIQUE(theme_id, language_code)
);

-- Insérer le type de thème MUI s'il n'existe pas
INSERT INTO TB_UI_THEME_TYPES (code, name, status, created_by, created_date)
VALUES ('mui', 'Material-UI', 'ACTIVE', 'system', NOW())
ON CONFLICT (code) DO NOTHING;

-- Table des préférences utilisateur
CREATE TABLE IF NOT EXISTS TB_USER_PREFERENCES (
    id SERIAL PRIMARY KEY,
    user_id TEXT NOT NULL UNIQUE,
    language TEXT NOT NULL DEFAULT 'fr',
    theme TEXT NOT NULL DEFAULT 'default',
    created_date TIMESTAMP DEFAULT NOW(),
    last_modified_date TIMESTAMP
);

-- Assurer que user_id est unique même sur une base préexistante
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'uk_user_preferences_user_id'
        AND conrelid = 'tb_user_preferences'::regclass
    ) THEN
        ALTER TABLE TB_USER_PREFERENCES ADD CONSTRAINT uk_user_preferences_user_id UNIQUE (user_id);
    END IF;
END $$;

-- Index pour optimiser les recherches par user_id
CREATE INDEX IF NOT EXISTS idx_user_preferences_user_id ON TB_USER_PREFERENCES(user_id);

-- ==================================================
-- Nouvelle table : types d'applications et migration
-- ==================================================

-- Table des types d'application
CREATE TABLE IF NOT EXISTS TB_APP_APPLICATION_TYPE (
    code SMALLINT PRIMARY KEY,
    type_name TEXT NOT NULL,
    description TEXT
);

-- Insérer les types d'application (1=backend, 2=frontend, 3=other)
INSERT INTO TB_APP_APPLICATION_TYPE (code, type_name, description)
VALUES
    (1, 'backend', 'Applications backend (API, services)'),
    (2, 'frontend', 'Applications frontend (UI, web apps)'),
    (3, 'other', 'Autres types d''application')
ON CONFLICT (code) DO NOTHING;

-- Ajouter la colonne app_type_code dans la table des applications
ALTER TABLE IF EXISTS TB_APP_APPLICATION
    ADD COLUMN IF NOT EXISTS app_type_code SMALLINT DEFAULT 3;

-- Assurer que les enregistrements existants ont une valeur par défaut
UPDATE TB_APP_APPLICATION
SET app_type_code = 3
WHERE app_type_code IS NULL;

-- Ajouter contrainte FK vers TB_APP_APPLICATION_TYPE(code)
ALTER TABLE IF EXISTS TB_APP_APPLICATION
    ADD CONSTRAINT IF NOT EXISTS fk_tb_app_application_type
    FOREIGN KEY (app_type_code) REFERENCES TB_APP_APPLICATION_TYPE(code);

-- Index pour accélérer la recherche par type
CREATE INDEX IF NOT EXISTS idx_tb_app_application_app_type ON TB_APP_APPLICATION(app_type_code);

-- Exemple d'assignation manuelle pour marquer des applications connues comme frontend
-- UPDATE TB_APP_APPLICATION
-- SET app_type_code = 2
-- WHERE app_name ILIKE '%front%' OR app_key IN ('portal-ui','portail-frontend');

-- Vérifications
-- SELECT app_type_code, COUNT(*) FROM TB_APP_APPLICATION GROUP BY app_type_code;
-- SELECT * FROM TB_APP_APPLICATION_TYPE ORDER BY code;
