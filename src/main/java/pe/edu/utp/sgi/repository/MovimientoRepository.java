package pe.edu.utp.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sgi.entity.Movimiento;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

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
