SELECT 
    CONVERT(VARCHAR, F.Fecha, 23) AS fecha,
    DATENAME(WEEKDAY, F.Fecha) AS subDia,
    DAY(F.Fecha) AS dia,
    COALESCE(SUM(va.SubTotal), 0) AS Ingresos,
    COALESCE(SUM(e.monto), 0) AS Egresos,
    COALESCE(SUM(va.SubTotal), 0) - COALESCE(SUM(e.monto), 0) AS Balance
FROM 
    (SELECT DATEADD(DAY, value - 1, DATEFROMPARTS(2025, 2, 1)) AS Fecha 
     FROM GENERATE_SERIES(1, DAY(EOMONTH(DATEFROMPARTS(2025, 2, 1))))) AS F
LEFT JOIN venta v ON F.Fecha = v.fecha
LEFT JOIN (SELECT id_venta, SUM(subtotal) AS SubTotal 
           FROM detalle_venta 
           WHERE id_tipo_item = 1 
           GROUP BY id_venta) va 
    ON va.id_venta = v.id
LEFT JOIN egreso e ON F.Fecha = e.fecha
GROUP BY F.Fecha
ORDER BY F.Fecha;
