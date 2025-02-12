package GMC.carwash_system.repository.entidades;


import GMC.carwash_system.model.entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT COUNT(v) FROM Venta v " +
            "WHERE v.cliente.id = :idCliente " +
            "AND MONTH(v.fecha) = :mes " +
            "AND YEAR(v.fecha) = :anio")
    Integer contarVisitasPorClienteYMes(@Param("idCliente") Long idCliente,
                                        @Param("mes") Integer mes,
                                        @Param("anio") Integer anio);




    @Query(value = """
    SELECT 
        ts.nombre AS nombreServicio, 
        SUM(dv.cantidad) AS totalCantidad, 
        SUM(dv.precio_unitario) AS totalRecaudado,
        CAST(v.fecha AS DATE) AS fecha  -- Cambié a CAST para SQL Server
    FROM venta v
    INNER JOIN detalle_venta dv 
        ON dv.id_venta = v.id
    INNER JOIN detalle_ingreso_vehiculo div
        ON div.id = dv.id_entrada_vehiculo
    INNER JOIN tipo_servicio ts
        ON ts.id = dv.id_item
    WHERE div.realizado = 1
      AND dv.id_tipo_item = 1
    GROUP BY ts.nombre, CAST(v.fecha AS DATE)  -- Cambié a CAST también aquí
    ORDER BY totalCantidad DESC
""", nativeQuery = true)
    List<Object[]> obtenerServiciosPorDia();


    @Query(value = """
    SELECT 
        p.nombre AS nombreProducto, 
        SUM(dv.cantidad) AS totalCantidad, 
        SUM(dv.precio_unitario * dv.cantidad) AS totalRecaudado,
        CAST(v.fecha AS DATE) AS fecha  -- Cambié a CAST para SQL Server
    FROM venta v
    INNER JOIN detalle_venta dv 
        ON dv.id_venta = v.id
    INNER JOIN detalle_ingreso_vehiculo div
        ON div.id = dv.id_entrada_vehiculo
    INNER JOIN producto p
        ON p.id = dv.id_item
    WHERE div.realizado = 1
      AND dv.id_tipo_item = 2
    GROUP BY p.nombre, CAST(v.fecha AS DATE)  -- Cambié a CAST también aquí
    ORDER BY totalCantidad DESC
""", nativeQuery = true)
    List<Object[]> obtenerProductosPorDia();


    @Query(value = """
    SELECT 
        ventasUnicas.tipoVehiculo AS tipoVehiculo, 
        COUNT(*) AS cantidad,
        CONVERT(VARCHAR, ventasUnicas.fecha, 23) AS fecha  -- Convertir la fecha a formato 'YYYY-MM-DD'
    FROM (
        SELECT DISTINCT 
            v.id AS idVenta, 
            tv.nombre AS tipoVehiculo,
            v.fecha AS fecha  -- Seleccionar la fecha en la subconsulta
        FROM venta v
        INNER JOIN detalle_venta dv 
            ON dv.id_venta = v.id
        INNER JOIN detalle_ingreso_vehiculo div
            ON div.id = dv.id_entrada_vehiculo
        INNER JOIN vehiculo veh
            ON div.id_vehiculo = veh.id
        LEFT JOIN tipo_vehiculo tv
            ON veh.id_tipo_vehiculo = tv.id
        WHERE div.realizado = 1
    ) AS ventasUnicas
    GROUP BY ventasUnicas.tipoVehiculo, CONVERT(VARCHAR, ventasUnicas.fecha, 23)  -- Convertir la fecha a formato adecuado
    ORDER BY cantidad DESC
""", nativeQuery = true)
    List<Object[]> obtenerTiposVehiculosPorDia();


    @Query(value = """
            SELECT
                mp.nombre AS metodoPago,
                SUM(v.total) AS total,
            	CAST(v.fecha AS DATE) AS fecha
            FROM venta v
            INNER JOIN metodo_pago mp
            	ON mp.id = v.id_metodo_pago
            GROUP BY mp.nombre, CAST(v.fecha AS DATE)
            ORDER BY total DESC
""", nativeQuery = true)
    List<Object[]> obtenerMetodosPagoPorDia();





















    //GENERAL:
    @Query(value = """
            SELECT 
                ts.nombre AS nombreServicio, 
                SUM(dv.cantidad) AS totalCantidad, 
                SUM(dv.precio_unitario) AS totalRecaudado
            FROM venta v
            INNER JOIN detalle_venta dv 
                ON dv.id_venta = v.id
            INNER JOIN detalle_ingreso_vehiculo div
                ON div.id = dv.id_entrada_vehiculo
            INNER JOIN tipo_servicio ts
                ON ts.id = dv.id_item
            WHERE div.realizado = 1
              AND dv.id_tipo_item = 1
            GROUP BY ts.nombre
            ORDER BY totalCantidad DESC
            """, nativeQuery = true)
    List<Object[]> obtenerServiciosRaw();

    @Query(value = """
            SELECT 
                p.nombre AS nombreProducto, 
                SUM(dv.cantidad) AS totalCantidad, 
                SUM(dv.precio_unitario) AS totalRecaudado
            FROM venta v
            INNER JOIN detalle_venta dv 
                ON dv.id_venta = v.id
            INNER JOIN detalle_ingreso_vehiculo div
                ON div.id = dv.id_entrada_vehiculo
            INNER JOIN producto p
                ON p.id = dv.id_item
            WHERE div.realizado = 1
              AND dv.id_tipo_item = 2
            GROUP BY p.nombre
            ORDER BY totalCantidad DESC
            """, nativeQuery = true)
    List<Object[]> obtenerProductosRaw();

    @Query(value = """
    SELECT 
        ventasUnicas.tipoVehiculo AS tipoVehiculo, 
        COUNT(*) AS cantidad
    FROM (
        SELECT DISTINCT 
            v.id AS idVenta, 
            tv.nombre AS tipoVehiculo
        FROM venta v
        INNER JOIN detalle_venta dv 
            ON dv.id_venta = v.id
        INNER JOIN detalle_ingreso_vehiculo div
            ON div.id = dv.id_entrada_vehiculo
        INNER JOIN vehiculo veh
            ON div.id_vehiculo = veh.id
        INNER JOIN tipo_vehiculo tv
            ON veh.id_tipo_vehiculo = tv.id
        WHERE div.realizado = 1
    ) AS ventasUnicas
    GROUP BY ventasUnicas.tipoVehiculo
    ORDER BY cantidad DESC
    """, nativeQuery = true)
    List<Object[]> obtenerTiposVehiculosRaw();


    @Query(value = """
            SELECT
                mp.nombre AS metodoPago,
                SUM(v.total) AS total
            FROM venta v
            INNER JOIN metodo_pago mp
            	ON mp.id = v.id_metodo_pago
            GROUP BY mp.nombre
            ORDER BY total DESC
    """, nativeQuery = true)
    List<Object[]> obtenerMetodosPagoRaw();


}

