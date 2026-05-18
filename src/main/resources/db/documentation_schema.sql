-- =============================================================================
-- Documentation Schema — Composant de documentation centralisée
-- Convention: TB_DOC_{NAME}  |  ddl-auto: none  |  No Flyway
-- =============================================================================

-- Table principale : une documentation par écran/application
CREATE TABLE IF NOT EXISTS TB_DOC_DOCUMENTATION (
    doc_id             SERIAL PRIMARY KEY,
    screen_key         TEXT NOT NULL,
    application_code   TEXT NOT NULL,
    title_fr           TEXT NOT NULL,
    title_en           TEXT NOT NULL,
    subtitle_fr        TEXT,
    subtitle_en        TEXT,
    icon_name          TEXT,          -- nom MUI icon ex: 'Storage', 'Info'
    version            INTEGER NOT NULL DEFAULT 1,
    active             BOOLEAN NOT NULL DEFAULT true,
    created_by         TEXT NOT NULL DEFAULT 'system',
    created_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   TEXT,
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_doc_screen_app UNIQUE (screen_key, application_code)
);

-- Sections ordonnées par document
CREATE TABLE IF NOT EXISTS TB_DOC_SECTION (
    section_id         SERIAL PRIMARY KEY,
    doc_id             INTEGER NOT NULL REFERENCES TB_DOC_DOCUMENTATION(doc_id) ON DELETE CASCADE,
    section_key        TEXT NOT NULL,
    heading_level      INTEGER NOT NULL DEFAULT 2 CHECK (heading_level IN (2, 3)),
    order_index        INTEGER NOT NULL DEFAULT 0,
    title_fr           TEXT NOT NULL,
    title_en           TEXT NOT NULL,
    content_fr         TEXT NOT NULL DEFAULT '',
    content_en         TEXT NOT NULL DEFAULT '',
    active             BOOLEAN NOT NULL DEFAULT true,
    created_by         TEXT NOT NULL DEFAULT 'system',
    created_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   TEXT,
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_section_key_doc UNIQUE (doc_id, section_key)
);

-- Audit log des modifications
CREATE TABLE IF NOT EXISTS TB_DOC_AUDIT_LOG (
    audit_id           SERIAL PRIMARY KEY,
    doc_id             INTEGER NOT NULL,
    section_id         INTEGER,
    action             TEXT NOT NULL CHECK (action IN ('CREATE','UPDATE','DELETE','REORDER')),
    changed_by         TEXT NOT NULL,
    changed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    old_content_fr     TEXT,
    old_content_en     TEXT,
    new_content_fr     TEXT,
    new_content_en     TEXT,
    extra_data         TEXT           -- JSON libre pour métadonnées supplémentaires
);

-- Index de performance
CREATE INDEX IF NOT EXISTS idx_doc_app_screen ON TB_DOC_DOCUMENTATION (application_code, screen_key);
CREATE INDEX IF NOT EXISTS idx_section_doc_order ON TB_DOC_SECTION (doc_id, order_index);
CREATE INDEX IF NOT EXISTS idx_audit_doc ON TB_DOC_AUDIT_LOG (doc_id);
CREATE INDEX IF NOT EXISTS idx_audit_section ON TB_DOC_AUDIT_LOG (section_id);
