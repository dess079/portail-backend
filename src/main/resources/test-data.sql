-- Script d'insertion de données test pour la table TB_APP_APPLICATIONS
-- À exécuter dans la base de données db_apps

-- Supprimer les données existantes si nécessaire (optionnel)
-- TRUNCATE TABLE TB_APP_APPLICATIONS CASCADE;

-- Insertion de données test pour les applications
INSERT INTO TB_APP_APPLICATIONS (
    code,
    name,
    description,
    url,
    status,
    order_index,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
) VALUES
    (
        'PORTAIL',
        'Portail de Gestion',
        'Interface de gestion centralisée pour toutes les applications de l''écosystème',
        'http://localhost:5173',
        'ACTIVE',
        1,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    ),
    (
        'LOTTOQUEBEC',
        'Lotto Québec Backend',
        'API backend pour la gestion des tirages et prédictions de loterie',
        'http://localhost:8090',
        'ACTIVE',
        2,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    ),
    (
        'LOTTOSYLVAIN-UI',
        'Lotto Sylvain Interface',
        'Interface utilisateur pour l''analyse des tirages de loterie',
        'http://localhost:3000',
        'ACTIVE',
        3,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    ),
    (
        'SED-BACKEND',
        'SED Backend',
        'API backend pour le système d''échange de documents avec AI',
        'http://localhost:8082',
        'ACTIVE',
        4,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    ),
    (
        'SED-FRONTEND',
        'SED Frontend',
        'Interface utilisateur pour le système d''échange de documents',
        'http://localhost:3002',
        'ACTIVE',
        5,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    ),
    (
        'DIRECTAI-BACKEND',
        'DirectAI Backend',
        'API backend pour l''orchestration d''agents IA',
        'http://localhost:8083',
        'ACTIVE',
        6,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    ),
    (
        'DIRECTAI-ADMIN-UI',
        'DirectAI Admin Interface',
        'Interface d''administration pour les agents IA',
        'http://localhost:3003',
        'ACTIVE',
        7,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    );

-- Insertion de données test pour les thèmes UI
INSERT INTO TB_UI_THEME_TYPES (
    code,
    name,
    description,
    status,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
) VALUES
    (
        'MUI',
        'Material-UI',
        'Thèmes basés sur Material Design',
        'ACTIVE',
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    );

INSERT INTO TB_UI_THEMES (
    code,
    name,
    theme_type_id,
    theme_definition,
    status,
    order_index,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
) VALUES
    (
        'DEFAULT',
        'Thème par défaut',
        1,
        '{
            "palette": {
                "mode": "light",
                "primary": {
                    "main": "#1976d2"
                },
                "secondary": {
                    "main": "#dc004e"
                }
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
        1,
        '{
            "palette": {
                "mode": "dark",
                "primary": {
                    "main": "#90caf9"
                },
                "secondary": {
                    "main": "#f48fb1"
                },
                "background": {
                    "default": "#121212",
                    "paper": "#1e1e1e"
                }
            }
        }'::jsonb,
        'ACTIVE',
        2,
        'system',
        CURRENT_TIMESTAMP,
        'system',
        CURRENT_TIMESTAMP
    );

INSERT INTO TB_UI_THEME_TRANSLATIONS (
    theme_id,
    language_code,
    name
) VALUES
    (1, 'fr', 'Thème par défaut'),
    (1, 'en', 'Default Theme'),
    (2, 'fr', 'Thème sombre'),
    (2, 'en', 'Dark Theme');