
  INSERT INTO historial_visitas_cliente (fecha, hora, id_cliente)
SELECT fecha, hora, id_cliente 
FROM venta;
