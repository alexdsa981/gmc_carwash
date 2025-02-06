UPDATE tipo_servicio
SET is_active = 1
WHERE is_active IS NULL;
