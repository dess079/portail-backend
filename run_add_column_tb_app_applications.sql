-- Add column app_type_code to tb_app_applications and related FK/index
ALTER TABLE tb_app_applications
  ADD COLUMN IF NOT EXISTS app_type_code SMALLINT DEFAULT 3;

UPDATE tb_app_applications
SET app_type_code = 3
WHERE app_type_code IS NULL;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tb_app_applications_type') THEN
    EXECUTE 'ALTER TABLE tb_app_applications ADD CONSTRAINT fk_tb_app_applications_type FOREIGN KEY (app_type_code) REFERENCES tb_app_application_type(code)';
  END IF;
END
$$;

CREATE INDEX IF NOT EXISTS idx_tb_app_applications_app_type ON tb_app_applications(app_type_code);
