SELECT 
    v.fecha,
    ts.nombre
FROM venta v
INNER JOIN detalle_venta dv ON dv.id_venta = v.id
INNER JOIN tipo_servicio ts ON ts.id = dv.id_item
WHERE 
    dv.id_tipo_item = 1 
    AND ts.is_especial = 1
    AND YEAR(v.fecha) = 2025  -- Filtrar por año
    AND MONTH(v.fecha) = 1 -- Filtrar por mes
ORDER BY v.fecha;
