-- Création de la table des types d'application
CREATE TABLE IF NOT EXISTS TB_APP_APPLICATION_TYPE (
    code SMALLINT PRIMARY KEY,
    type_name TEXT NOT NULL,
    description TEXT
);

-- Insérer les types (ignore si existent)
INSERT INTO TB_APP_APPLICATION_TYPE (code, type_name, description)
VALUES
  (1, 'backend', 'Applications backend (API, services)'),
  (2, 'frontend', 'Applications frontend (UI, web apps)'),
  (3, 'other', 'Autres types d''application')
ON CONFLICT (code) DO NOTHING;

-- Ajouter la colonne app_type_code dans la table des applications (valeur par défaut = 3)
ALTER TABLE IF EXISTS TB_APP_APPLICATION
  ADD COLUMN IF NOT EXISTS app_type_code SMALLINT DEFAULT 3;

-- Mettre à jour les lignes NULL
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.tables
    WHERE table_schema = current_schema() AND table_name = 'tb_app_application'
  ) THEN
    -- Mettre à jour les lignes NULL
    EXECUTE 'UPDATE TB_APP_APPLICATION SET app_type_code = 3 WHERE app_type_code IS NULL';

    -- Ajouter contrainte FK (optionnel)
    BEGIN
      EXECUTE 'ALTER TABLE TB_APP_APPLICATION ADD CONSTRAINT fk_tb_app_application_type FOREIGN KEY (app_type_code) REFERENCES TB_APP_APPLICATION_TYPE(code)';
    EXCEPTION WHEN duplicate_object THEN
      -- ignore if constraint already exists
      NULL;
    END;

    -- Créer index pour recherche par type
    BEGIN
      EXECUTE 'CREATE INDEX idx_tb_app_application_app_type ON TB_APP_APPLICATION(app_type_code)';
    EXCEPTION WHEN duplicate_table THEN
      NULL;
    END;
  END IF;
END
$$;
