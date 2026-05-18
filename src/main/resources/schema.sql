-- Script de création de la base de données et de la table tb_apn_appli_name
-- Base de données: db_apps

-- Création de la base de données si elle n'existe pas
-- Note: Cette commande doit être exécutée en dehors d'une transaction
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'db_apps') THEN
      CREATE DATABASE db_apps;
   END IF;
END
$$;

-- Transaction pour supprimer la table si elle existe
BEGIN;
DROP TABLE IF EXISTS tb_apn_appli_name;
COMMIT;

-- Transaction pour créer la table et ses dépendances
BEGIN;

-- Création de la table des applications
CREATE TABLE TB_APP_APPLICATIONS (
    code TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    url TEXT NOT NULL,
    icon TEXT,
    status TEXT NOT NULL,
    order_index INTEGER NOT NULL,
    created_by TEXT NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by TEXT,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour améliorer les performances
CREATE INDEX idx_app_status ON TB_APP_APPLICATIONS(status);
CREATE INDEX idx_app_code ON TB_APP_APPLICATIONS(code);
CREATE INDEX idx_app_created_date ON TB_APP_APPLICATIONS(created_date);

-- Trigger pour mettre à jour last_modified_date automatiquement
CREATE OR REPLACE FUNCTION update_last_modified_date_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_modified_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

DROP TRIGGER IF EXISTS update_tb_app_applications_last_modified_date ON TB_APP_APPLICATIONS;
CREATE TRIGGER update_tb_app_applications_last_modified_date
    BEFORE UPDATE ON TB_APP_APPLICATIONS
    FOR EACH ROW EXECUTE FUNCTION update_last_modified_date_column();
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Tables pour la gestion des thèmes UI
CREATE TABLE TB_UI_THEME_TYPES (
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

CREATE TABLE TB_UI_THEMES (
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

CREATE TABLE TB_UI_THEME_TRANSLATIONS (
    id SERIAL PRIMARY KEY,
    theme_id INTEGER NOT NULL REFERENCES TB_UI_THEMES(id) ON DELETE CASCADE,
    language_code TEXT NOT NULL,
    name TEXT NOT NULL,
    UNIQUE(theme_id, language_code)
);

-- Index pour améliorer les performances des thèmes
CREATE INDEX idx_theme_status ON TB_UI_THEMES(status);
CREATE INDEX idx_theme_code ON TB_UI_THEMES(code);
CREATE INDEX idx_theme_order ON TB_UI_THEMES(order_index);
CREATE INDEX idx_theme_type_status ON TB_UI_THEME_TYPES(status);

-- Triggers pour les thèmes
DROP TRIGGER IF EXISTS update_tb_ui_themes_last_modified_date ON TB_UI_THEMES;
CREATE TRIGGER update_tb_ui_themes_last_modified_date
    BEFORE UPDATE ON TB_UI_THEMES
    FOR EACH ROW EXECUTE FUNCTION update_last_modified_date_column();

DROP TRIGGER IF EXISTS update_tb_ui_theme_types_last_modified_date ON TB_UI_THEME_TYPES;
CREATE TRIGGER update_tb_ui_theme_types_last_modified_date
    BEFORE UPDATE ON TB_UI_THEME_TYPES
    FOR EACH ROW EXECUTE FUNCTION update_last_modified_date_column();

COMMIT;