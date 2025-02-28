DECLARE @constraintName NVARCHAR(200);

-- Buscar el nombre de la restricción de clave foránea asociada a la columna id_colaborador
SELECT @constraintName = fk.name
FROM sys.foreign_keys fk
INNER JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
INNER JOIN sys.columns c ON fkc.parent_object_id = c.object_id AND fkc.parent_column_id = c.column_id
WHERE fk.parent_object_id = OBJECT_ID('[gmc_carwash].[dbo].[detalle_venta]')
AND c.name = 'id_colaborador';

-- Si se encuentra una restricción, eliminarla
IF @constraintName IS NOT NULL
BEGIN
    EXEC('ALTER TABLE [gmc_carwash].[dbo].[detalle_venta] DROP CONSTRAINT ' + @constraintName);
END

-- Ahora eliminar la columna
ALTER TABLE [gmc_carwash].[dbo].[detalle_venta]  
DROP COLUMN [id_colaborador];
