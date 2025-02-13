DECLARE @Year INT = 2025;
DECLARE @Month INT = 2;

WITH Fechas AS (
    SELECT DATEADD(DAY, value - 1, DATEFROMPARTS(@Year, @Month, 1)) AS Fecha
    FROM GENERATE_SERIES(1, DAY(EOMONTH(DATEFROMPARTS(@Year, @Month, 1))))
),
VentasAgrupadas AS (
    SELECT id_venta, SUM(subtotal) AS SubTotal
    FROM detalle_venta
    WHERE id_tipo_item = 1  -- Filtrar solo los subtotales con id_tipo_item = 1
    GROUP BY id_venta
),
IngresoVehiculos AS (
    SELECT DISTINCT id_cliente, id_vehiculo
    FROM detalle_ingreso_vehiculo
)
SELECT
    F.Fecha,
    DAY(F.Fecha) AS Dia,
    DATENAME(WEEKDAY, F.Fecha) AS NombreDia,

    COALESCE(SUM(va.SubTotal), 0) AS SubTotal, -- Sumar solo los subtotales con id_tipo_item = 1

    COUNT(DISTINCT v.id) AS Cantidad, -- Total de visitas en el día (sin duplicados)

    COUNT(DISTINCT CASE WHEN ve.id_tipo_vehiculo NOT IN (7, 8) THEN v.id END) AS Autos,  
    COUNT(DISTINCT CASE WHEN ve.id_tipo_vehiculo = 7 THEN v.id END) AS MotosL, 
    COUNT(DISTINCT CASE WHEN ve.id_tipo_vehiculo = 8 THEN v.id END) AS MotosT  

FROM Fechas F
LEFT JOIN venta v ON F.Fecha = v.fecha
LEFT JOIN IngresoVehiculos div ON v.id_cliente = div.id_cliente
LEFT JOIN vehiculo ve ON ve.id = div.id_vehiculo
LEFT JOIN VentasAgrupadas va ON va.id_venta = v.id

GROUP BY F.Fecha
ORDER BY F.Fecha;
