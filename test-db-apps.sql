-- Vérifier l'état actuel de la base db_apps
SELECT 
  a.code, 
  a.name, 
  a.status, 
  a.app_type_code,
  e.env_code,
  l.url
FROM TB_APP_APPLICATIONS a
LEFT JOIN TB_APP_ENV_LIAISON l ON a.code = l.app_code
LEFT JOIN TB_ENV_ENVIRONNEMENT e ON l.env_id = e.env_id
WHERE a.app_type_code = 2 -- frontends
ORDER BY a.code, e.env_code;
