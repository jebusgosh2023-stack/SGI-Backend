package pe.edu.utp.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sgi.entity.Producto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsBySku(String sku);

    Optional<Producto> findBySku(String sku);

    List<Producto> findByActivoTrue();

    @Query("""
        SELECT p FROM Producto p
        WHERE p.activo = true
          AND (SELECT COALESCE(SUM(s.cantidad), 0)
               FROM StockSucursal s WHERE s.producto = p) <= p.stockMinimo
    """)
    List<Producto> findProductosBajoStockMinimo();

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) " +
           "LIKE LOWER(CONCAT('%', :texto, '%')) AND p.activo = true")
    List<Producto> buscarPorNombre(@Param("texto") String texto);
}
