package pe.edu.utp.sgi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_sucursal", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"producto_id", "sucursal_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockSucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @Builder.Default
    @Column(nullable = false)
    private Integer cantidad = 0;
}
