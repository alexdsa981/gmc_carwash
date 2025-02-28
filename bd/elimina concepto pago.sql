-- 1. Buscar el nombre de la clave foránea en 'sueldos' que referencia 'concepto_pago'
DECLARE @fk_name NVARCHAR(255);

SELECT @fk_name = name 
FROM sys.foreign_keys 
WHERE parent_object_id = OBJECT_ID('sueldos') 
AND referenced_object_id = OBJECT_ID('concepto_pago');

-- 2. Si existe la clave foránea, eliminarla dinámicamente
IF @fk_name IS NOT NULL
BEGIN
    EXEC('ALTER TABLE sueldos DROP CONSTRAINT ' + @fk_name);
END

-- 3. Eliminar la columna id_concepto_pago si existe
IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
           WHERE TABLE_NAME = 'sueldos' AND COLUMN_NAME = 'id_concepto_pago')
BEGIN
    ALTER TABLE sueldos DROP COLUMN id_concepto_pago;
END

-- 4. Eliminar la tabla concepto_pago si existe
IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'concepto_pago')
BEGIN
    DROP TABLE concepto_pago;
END
