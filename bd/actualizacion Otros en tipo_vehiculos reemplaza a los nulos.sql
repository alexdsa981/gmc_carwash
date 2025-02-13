-- 1. Insertar el tipo de vehículo "Otros" si no existe
IF NOT EXISTS (SELECT 1 FROM [gmc_carwash].[dbo].[tipo_vehiculo] WHERE nombre = 'Otros')
BEGIN
    INSERT INTO [gmc_carwash].[dbo].[tipo_vehiculo] ([nombre], [is_active])
    VALUES ('Otros', 1);  -- Asumiendo que 'is_active' debe ser 1 para activos
END

-- 2. Obtener el ID del tipo de vehículo "Otros"
DECLARE @IdOtros INT;
SELECT @IdOtros = id FROM [gmc_carwash].[dbo].[tipo_vehiculo] WHERE nombre = 'Otros';

-- 3. Actualizar los registros nulos en la tabla vehiculo
UPDATE [gmc_carwash].[dbo].[vehiculo]
SET id_tipo_vehiculo = @IdOtros
WHERE id_tipo_vehiculo IS NULL;
