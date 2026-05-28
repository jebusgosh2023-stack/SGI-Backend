-- =====================================================
-- SGI - Sistema de Gestión de Inventarios
-- Datos iniciales (INSERT IGNORE para idempotencia)
-- Nombres de tablas y columnas según entidades JPA
-- =====================================================

-- -------------------------------------------------
-- ROLES
-- -------------------------------------------------
INSERT IGNORE INTO roles (id, nombre) VALUES (1, 'ADMIN');
INSERT IGNORE INTO roles (id, nombre) VALUES (2, 'GERENTE');
INSERT IGNORE INTO roles (id, nombre) VALUES (3, 'JEFE_ALMACEN');
INSERT IGNORE INTO roles (id, nombre) VALUES (4, 'VENDEDOR');

-- -------------------------------------------------
-- SUCURSALES (10 distritos peruanos)
-- -------------------------------------------------
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (1,  'Minimarket SGI Miraflores',            'Av. Larco 1234',                       'Miraflores',               true);
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (2,  'Minimarket SGI Los Olivos',            'Av. Carlos Izaguirre 567',             'Los Olivos',               true);
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (3,  'Minimarket SGI San Isidro',            'Calle Las Begonias 890',               'San Isidro',               true);
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (4,  'Minimarket SGI Trujillo Centro',       'Jr. Pizarro 456',                      'Trujillo Centro',          true);
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (5,  'Minimarket SGI Arequipa Centro',       'Calle Mercaderes 321',                 'Arequipa Centro',          true);
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (6,  'Minimarket SGI San Juan de Lurigancho','Av. Próceres de la Independencia 1500', 'San Juan de Lurigancho',   true);
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (7,  'Minimarket SGI Iquitos Centro',        'Jr. Próspero 789',                     'Iquitos Centro',           true);
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (8,  'Minimarket SGI Cusco Centro',          'Av. El Sol 234',                       'Cusco Centro',             true);
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (9,  'Minimarket SGI Huancayo Centro',       'Calle Real 567',                       'Huancayo Centro',          true);
INSERT IGNORE INTO sucursales (id, nombre, direccion, distrito, activa) VALUES (10, 'Minimarket SGI Chorrillos',            'Av. Defensores del Morro 1100',        'Chorrillos',               true);

-- -------------------------------------------------
-- CATEGORÍAS
-- -------------------------------------------------
INSERT IGNORE INTO categorias (id, nombre, descripcion, activa) VALUES (1, 'Bebidas',   'Bebidas gaseosas, jugos, aguas y refrescos',                true);
INSERT IGNORE INTO categorias (id, nombre, descripcion, activa) VALUES (2, 'Abarrotes', 'Arroz, azúcar, aceite, fideos y productos de despensa',     true);
INSERT IGNORE INTO categorias (id, nombre, descripcion, activa) VALUES (3, 'Lácteos',   'Leche, yogurt, queso y derivados lácteos',                  true);
INSERT IGNORE INTO categorias (id, nombre, descripcion, activa) VALUES (4, 'Limpieza',  'Detergentes, jabones, lejía y artículos de limpieza',       true);
INSERT IGNORE INTO categorias (id, nombre, descripcion, activa) VALUES (5, 'Snacks',    'Galletas, papitas, chocolates y bocaditos',                 true);

-- -------------------------------------------------
-- USUARIO ADMINISTRADOR
-- Correo: admin@sgi.com  |  Contraseña: Admin1234
-- BCrypt hash generado para: Admin1234
-- -------------------------------------------------
INSERT IGNORE INTO usuarios (id, nombre, correo, password, activo, fecha_creacion, rol_id, sucursal_id)
VALUES (1, 'Administrador', 'admin@sgi.com',
        '$2a$10$9l8I0KkUSJlNL2/HOLREHefUbLb1HC540oRnuEM36NJzZougRFP.i',
        true, NOW(), 1, NULL);

-- -------------------------------------------------
-- PRODUCTOS (10 productos típicos de minimarket peruano)
-- -------------------------------------------------
INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (1,  'BEB-001', 'Inca Kola 500ml',           'Gaseosa Inca Kola botella personal 500ml',           1.80, 2.50, 'Unidad',    20, 200, true, 1);

INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (2,  'BEB-002', 'Agua San Mateo 625ml',      'Agua mineral San Mateo sin gas 625ml',               0.90, 1.50, 'Unidad',    30, 300, true, 1);

INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (3,  'ABA-001', 'Arroz Costeño 5kg',         'Arroz extra Costeño bolsa de 5 kilogramos',          14.50, 18.90, 'Bolsa',    10, 100, true, 2);

INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (4,  'ABA-002', 'Aceite Primor 1L',          'Aceite vegetal Primor botella de 1 litro',            6.50, 8.90, 'Botella',   15, 150, true, 2);

INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (5,  'LAC-001', 'Leche Gloria 400ml',        'Leche evaporada Gloria lata 400ml',                   3.20, 4.20, 'Lata',     25, 250, true, 3);

INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (6,  'LAC-002', 'Yogurt Laive 1L',           'Yogurt bebible Laive fresa 1 litro',                  4.80, 6.50, 'Botella',  15, 120, true, 3);

INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (7,  'LIM-001', 'Lejía Clorox 1L',          'Lejía Clorox tradicional botella 1 litro',            4.20, 5.90, 'Botella',  12, 120, true, 4);

INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (8,  'LIM-002', 'Detergente Bolivar 2.6kg',  'Detergente en polvo Bolívar floral bolsa 2.6kg',    17.00, 22.50, 'Bolsa',    8,  80,  true, 4);

INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (9,  'SNK-001', 'Galleta Casino 6-pack',     'Galletas Casino sabor chocolate pack 6 unidades',     2.80, 3.80, 'Paquete',  20, 200, true, 5);

INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio_compra, precio_venta, unidad_medida, stock_minimo, stock_maximo, activo, categoria_id)
VALUES (10, 'SNK-002', 'Papitas Lays Clásica 200g', 'Papas fritas Lays sabor clásico bolsa 200g',          5.90, 7.90, 'Bolsa',   18, 180, true, 5);

-- -------------------------------------------------
-- STOCK POR SUCURSAL (10 productos x 10 sucursales = 100 filas)
-- -------------------------------------------------

-- Producto 1: Inca Kola 500ml
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (1,  1, 1,  45);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (2,  1, 2,  60);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (3,  1, 3,  38);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (4,  1, 4,  55);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (5,  1, 5,  42);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (6,  1, 6,  70);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (7,  1, 7,  35);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (8,  1, 8,  48);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (9,  1, 9,  52);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (10, 1, 10, 40);

-- Producto 2: Agua San Mateo 625ml
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (11, 2, 1,  55);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (12, 2, 2,  80);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (13, 2, 3,  65);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (14, 2, 4,  45);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (15, 2, 5,  50);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (16, 2, 6,  90);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (17, 2, 7,  40);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (18, 2, 8,  58);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (19, 2, 9,  62);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (20, 2, 10, 48);

-- Producto 3: Arroz Costeño 5kg
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (21, 3, 1,  25);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (22, 3, 2,  35);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (23, 3, 3,  20);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (24, 3, 4,  30);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (25, 3, 5,  28);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (26, 3, 6,  40);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (27, 3, 7,  18);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (28, 3, 8,  22);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (29, 3, 9,  32);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (30, 3, 10, 27);

-- Producto 4: Aceite Primor 1L
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (31, 4, 1,  30);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (32, 4, 2,  42);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (33, 4, 3,  25);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (34, 4, 4,  38);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (35, 4, 5,  33);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (36, 4, 6,  50);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (37, 4, 7,  22);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (38, 4, 8,  28);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (39, 4, 9,  36);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (40, 4, 10, 31);

-- Producto 5: Leche Gloria 400ml
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (41, 5, 1,  50);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (42, 5, 2,  65);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (43, 5, 3,  40);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (44, 5, 4,  55);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (45, 5, 5,  48);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (46, 5, 6,  75);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (47, 5, 7,  35);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (48, 5, 8,  42);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (49, 5, 9,  58);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (50, 5, 10, 44);

-- Producto 6: Yogurt Laive 1L
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (51, 6, 1,  28);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (52, 6, 2,  35);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (53, 6, 3,  22);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (54, 6, 4,  30);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (55, 6, 5,  26);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (56, 6, 6,  42);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (57, 6, 7,  18);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (58, 6, 8,  24);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (59, 6, 9,  32);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (60, 6, 10, 20);

-- Producto 7: Lejía Clorox 1L
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (61, 7, 1,  20);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (62, 7, 2,  30);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (63, 7, 3,  18);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (64, 7, 4,  25);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (65, 7, 5,  22);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (66, 7, 6,  38);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (67, 7, 7,  15);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (68, 7, 8,  20);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (69, 7, 9,  28);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (70, 7, 10, 24);

-- Producto 8: Detergente Bolivar 2.6kg
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (71, 8, 1,  15);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (72, 8, 2,  22);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (73, 8, 3,  12);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (74, 8, 4,  18);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (75, 8, 5,  16);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (76, 8, 6,  28);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (77, 8, 7,  10);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (78, 8, 8,  14);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (79, 8, 9,  20);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (80, 8, 10, 17);

-- Producto 9: Galleta Casino 6-pack
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (81, 9, 1,  40);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (82, 9, 2,  55);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (83, 9, 3,  35);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (84, 9, 4,  48);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (85, 9, 5,  42);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (86, 9, 6,  65);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (87, 9, 7,  30);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (88, 9, 8,  38);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (89, 9, 9,  50);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (90, 9, 10, 36);

-- Producto 10: Papitas Lays Clásica 200g
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (91,  10, 1,  32);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (92,  10, 2,  45);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (93,  10, 3,  28);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (94,  10, 4,  38);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (95,  10, 5,  35);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (96,  10, 6,  52);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (97,  10, 7,  24);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (98,  10, 8,  30);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (99,  10, 9,  40);
INSERT IGNORE INTO stock_sucursal (id, producto_id, sucursal_id, cantidad) VALUES (100, 10, 10, 29);
