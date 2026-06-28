package pe.edu.utp.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sgi.entity.Producto;
import pe.edu.utp.sgi.entity.StockSucursal;
import pe.edu.utp.sgi.entity.Sucursal;

import java.util.Optional;
import java.util.List;

@Repository
public interface StockSucursalRepository extends JpaRepository<StockSucursal, Long> {
    Optional<StockSucursal> findByProductoAndSucursal(Producto producto, Sucursal sucursal);

    List<StockSucursal> findByProductoId(Long productoId);

    @Query("SELECT s FROM StockSucursal s JOIN FETCH s.producto p WHERE s.sucursal.id = :sucursalId AND p.activo = true")
    List<StockSucursal> findBySucursalIdAndProductoActivoTrue(@Param("sucursalId") Long sucursalId);

    @Query("SELECT s FROM StockSucursal s JOIN FETCH s.producto p WHERE s.sucursal.id = :sucursalId AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) AND p.activo = true")
    List<StockSucursal> buscarPorSucursalIdYNombreProducto(@Param("sucursalId") Long sucursalId, @Param("texto") String texto);

    @Query("SELECT s.producto.id, SUM(s.cantidad) FROM StockSucursal s WHERE s.producto.activo = true GROUP BY s.producto.id")
    List<Object[]> sumarStockPorProductoActivo();

    @Query("SELECT s FROM StockSucursal s JOIN FETCH s.producto p JOIN FETCH s.sucursal suc WHERE p.activo = true AND s.cantidad <= p.stockMinimo")
    List<StockSucursal> findProductosBajoStockGlobal();

    @Query("SELECT s FROM StockSucursal s JOIN FETCH s.producto p JOIN FETCH s.sucursal suc WHERE p.activo = true AND s.sucursal.id = :sucursalId AND s.cantidad <= p.stockMinimo")
    List<StockSucursal> findProductosBajoStockPorSucursal(@Param("sucursalId") Long sucursalId);

    @Query("SELECT s.sucursal.nombre, COALESCE(SUM(s.cantidad), 0) FROM StockSucursal s GROUP BY s.sucursal.nombre ORDER BY SUM(s.cantidad) ASC")
    List<Object[]> findSucursalesConMenosStock();

    @Query("SELECT s FROM StockSucursal s JOIN FETCH s.producto p WHERE s.sucursal.id = :sucursalId AND p.activo = true ORDER BY s.cantidad DESC")
    List<StockSucursal> findProductosMasStockPorSucursal(@Param("sucursalId") Long sucursalId, Pageable pageable);

    @Query("SELECT s FROM StockSucursal s JOIN FETCH s.producto p WHERE s.sucursal.id = :sucursalId AND p.activo = true ORDER BY s.cantidad ASC")
    List<StockSucursal> findProductosMenosStockPorSucursal(@Param("sucursalId") Long sucursalId, Pageable pageable);
}
