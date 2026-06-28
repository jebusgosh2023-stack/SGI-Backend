package pe.edu.utp.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sgi.entity.Movimiento;

import java.time.LocalDateTime;
import java.util.List;

import pe.edu.utp.sgi.entity.TipoMovimiento;
import org.springframework.data.domain.Pageable;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    long countByTipo(TipoMovimiento tipo);
    long countByTipoAndSucursalOrigenId(TipoMovimiento tipo, Long sucursalOrigenId);
    long countByTipoAndSucursalDestinoId(TipoMovimiento tipo, Long sucursalDestinoId);

    @Query("""
        SELECT p.nombre, SUM(m.cantidad)
        FROM Movimiento m
        JOIN m.producto p
        WHERE m.tipo IN :tipos
        GROUP BY p.nombre
        ORDER BY SUM(m.cantidad) DESC
    """)
    List<Object[]> findTopProductosMasMovidosGlobal(@Param("tipos") List<TipoMovimiento> tipos, Pageable pageable);

    @Query("""
        SELECT p.nombre, SUM(m.cantidad)
        FROM Movimiento m
        JOIN m.producto p
        WHERE m.tipo IN :tipos AND (m.sucursalOrigen.id = :sucursalId OR m.sucursalDestino.id = :sucursalId)
        GROUP BY p.nombre
        ORDER BY SUM(m.cantidad) DESC
    """)
    List<Object[]> findTopProductosMasMovidosPorSucursal(@Param("tipos") List<TipoMovimiento> tipos, @Param("sucursalId") Long sucursalId, Pageable pageable);

    @Query("""
        SELECT m FROM Movimiento m
        WHERE (m.sucursalOrigen.id = :sucursalId
               OR m.sucursalDestino.id = :sucursalId)
          AND m.fecha BETWEEN :desde AND :hasta
        ORDER BY m.fecha DESC
    """)
    List<Movimiento> findMovimientosPorSucursalYFecha(
        @Param("sucursalId") Long sucursalId,
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta);

    @Query("""
        SELECT c.nombre, SUM(m.cantidad)
        FROM Movimiento m
        JOIN m.producto p
        JOIN p.categoria c
        WHERE m.tipo = pe.edu.utp.sgi.entity.TipoMovimiento.SALIDA
          AND m.fecha BETWEEN :desde AND :hasta
        GROUP BY c.nombre
        ORDER BY SUM(m.cantidad) DESC
    """)
    List<Object[]> totalVendidoPorCategoria(
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta);
}
