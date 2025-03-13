CREATE TABLE detalle_metodo_pago (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_detalle_metodo BIGINT NOT NULL,
    id_venta BIGINT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_metodo_pago FOREIGN KEY (id_detalle_metodo) REFERENCES metodo_pago(id),
    CONSTRAINT fk_detalle_metodo_pago_venta FOREIGN KEY (id_venta) REFERENCES venta(id)
);

INSERT INTO detalle_metodo_pago (id_venta, id_detalle_metodo, monto)
SELECT v.id AS id_venta, v.id_metodo_pago AS id_detalle_metodo, v.total AS monto
FROM venta v;





DECLARE @constraint_name NVARCHAR(200);

-- Obtener el nombre de la restricción de clave foránea asociada a la columna id_metodo_pago
SELECT @constraint_name = fk.name
FROM sys.foreign_keys fk
JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
JOIN sys.columns c ON fkc.parent_object_id = c.object_id AND fkc.parent_column_id = c.column_id
WHERE c.object_id = OBJECT_ID('venta') AND c.name = 'id_metodo_pago';

-- Si existe una restricción, eliminarla
IF @constraint_name IS NOT NULL
BEGIN
    EXEC('ALTER TABLE venta DROP CONSTRAINT ' + @constraint_name);
END

-- Eliminar la columna id_metodo_pago
ALTER TABLE venta DROP COLUMN id_metodo_pago;
