-- Agent Activity — schéma
-- À exécuter manuellement sur db_apps (port 5437)
-- Créé le : 2026-05-04

-- ─── Types ENUM ──────────────────────────────────────────────────────────────

CREATE TYPE agent_hit_status AS ENUM ('started', 'completed', 'failed', 'escalated');
CREATE TYPE plan_status       AS ENUM ('running', 'completed', 'blocked');

-- ─── TB_AGT_PLAN ─────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS TB_AGT_PLAN (
    plan_id         VARCHAR(100)  PRIMARY KEY,
    objective       TEXT          NOT NULL,
    total_tasks     INTEGER       NOT NULL DEFAULT 0,
    completed_tasks INTEGER       NOT NULL DEFAULT 0,
    status          plan_status   NOT NULL DEFAULT 'running',
    started_at      TIMESTAMPTZ   NOT NULL DEFAULT now(),
    completed_at    TIMESTAMPTZ
);

COMMENT ON TABLE  TB_AGT_PLAN                IS 'Un enregistrement par plan d''exécution gem-orchestrator.';
COMMENT ON COLUMN TB_AGT_PLAN.plan_id        IS 'ID généré par l''orchestrateur : YYYYMMDD-slug';
COMMENT ON COLUMN TB_AGT_PLAN.objective      IS 'Objectif du plan (texte libre)';
COMMENT ON COLUMN TB_AGT_PLAN.total_tasks    IS 'Nombre total de tâches dans le plan';
COMMENT ON COLUMN TB_AGT_PLAN.completed_tasks IS 'Nombre de tâches complétées';
COMMENT ON COLUMN TB_AGT_PLAN.status         IS 'running | completed | blocked';

-- ─── TB_AGT_HIT ──────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS TB_AGT_HIT (
    id               BIGSERIAL     PRIMARY KEY,
    plan_id          VARCHAR(100)  NOT NULL REFERENCES TB_AGT_PLAN(plan_id),
    task_id          VARCHAR(200)  NOT NULL,
    agent_name       VARCHAR(100)  NOT NULL,
    skill_name       VARCHAR(100),
    status           agent_hit_status NOT NULL,
    description      TEXT,
    wave             INTEGER,
    duration_seconds INTEGER,
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT now()
);

COMMENT ON TABLE  TB_AGT_HIT                  IS 'Un enregistrement par dispatch ou complétion d''agent.';
COMMENT ON COLUMN TB_AGT_HIT.plan_id          IS 'Référence au plan parent';
COMMENT ON COLUMN TB_AGT_HIT.task_id          IS 'ID de la tâche dans le plan (ex. W1-constitution)';
COMMENT ON COLUMN TB_AGT_HIT.agent_name       IS 'Nom de l''agent invoqué (ex. gem-implementer)';
COMMENT ON COLUMN TB_AGT_HIT.skill_name       IS 'Nom du skill si applicable (nullable)';
COMMENT ON COLUMN TB_AGT_HIT.status           IS 'started | completed | failed | escalated';
COMMENT ON COLUMN TB_AGT_HIT.description      IS 'Résumé de ce qui a été fait (summary de l''agent)';
COMMENT ON COLUMN TB_AGT_HIT.wave             IS 'Numéro de vague (nullable)';
COMMENT ON COLUMN TB_AGT_HIT.duration_seconds IS 'Durée en secondes (null si status=started)';

-- ─── Index ───────────────────────────────────────────────────────────────────

CREATE INDEX IF NOT EXISTS idx_agt_hit_plan_id    ON TB_AGT_HIT(plan_id);
CREATE INDEX IF NOT EXISTS idx_agt_hit_agent_name ON TB_AGT_HIT(agent_name);
CREATE INDEX IF NOT EXISTS idx_agt_hit_status     ON TB_AGT_HIT(status);
CREATE INDEX IF NOT EXISTS idx_agt_hit_created_at ON TB_AGT_HIT(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_agt_hit_skill_name ON TB_AGT_HIT(skill_name) WHERE skill_name IS NOT NULL;
