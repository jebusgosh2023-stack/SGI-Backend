package pe.edu.utp.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sgi.entity.Producto;
import pe.edu.utp.sgi.entity.StockSucursal;
import pe.edu.utp.sgi.entity.Sucursal;

import java.util.Optional;

@Repository
public interface StockSucursalRepository extends JpaRepository<StockSucursal, Long> {
    Optional<StockSucursal> findByProductoAndSucursal(Producto producto, Sucursal sucursal);
}
