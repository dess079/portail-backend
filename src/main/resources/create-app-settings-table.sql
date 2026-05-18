-- Table générique des paramètres applicatifs (clé-valeur par utilisateur)
CREATE TABLE IF NOT EXISTS TB_APP_SETTINGS (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    setting_key VARCHAR(100) NOT NULL,
    setting_value TEXT NOT NULL,
    setting_type VARCHAR(20) NOT NULL DEFAULT 'STRING',
    description VARCHAR(255),
    created_date TIMESTAMP DEFAULT NOW(),
    last_modified_date TIMESTAMP DEFAULT NOW(),
    UNIQUE(user_id, setting_key)
);

-- Index pour optimiser les recherches par user_id
CREATE INDEX IF NOT EXISTS idx_app_settings_user_id ON TB_APP_SETTINGS(user_id);

-- Index composite pour recherche par clé et utilisateur
CREATE INDEX IF NOT EXISTS idx_app_settings_user_key ON TB_APP_SETTINGS(user_id, setting_key);

-- Paramètre par défaut : mode éco-AI désactivé
INSERT INTO TB_APP_SETTINGS (user_id, setting_key, setting_value, setting_type, description, created_date)
VALUES ('default-user', 'ai-eco-mode', 'false', 'BOOLEAN', 'Décharge les modèles Ollama du GPU après chaque inférence pour réduire la chaleur', NOW())
ON CONFLICT (user_id, setting_key) DO NOTHING;
