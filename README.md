# Sistema de Gestión de Inventarios (SGI)

**API REST** para la gestión de inventarios de una cadena de minimarkets peruanos con 10 sucursales.  
Proyecto académico — **Universidad Tecnológica del Perú (UTP)** — Desarrollo Web Integrado.

---

## 📋 Descripción

SGI permite administrar productos, categorías, sucursales, stock por sucursal y usuarios con control de acceso basado en roles (RBAC). El sistema soporta cuatro roles: **ADMIN**, **GERENTE**, **JEFE_ALMACEN** y **VENDEDOR**, cada uno con permisos diferenciados.

---

## ⚙️ Requisitos

| Herramienta | Versión mínima |
|-------------|---------------|
| Java (JDK)  | 17            |
| MySQL       | 8.0           |
| Maven       | 3.8+          |

---

## 🚀 Configuración y Ejecución

### 1. Crear la base de datos

La aplicación crea la base de datos automáticamente gracias al parámetro `createDatabaseIfNotExist=true`, pero si lo prefiere puede crearla manualmente:

```sql
CREATE DATABASE IF NOT EXISTS sgi_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configurar la contraseña de MySQL

Edite `src/main/resources/application.properties` o use una variable de entorno:

```bash
# Opción A: Variable de entorno
set DB_PASSWORD=tu_contraseña

# Opción B: Editar application.properties directamente
spring.datasource.password=tu_contraseña
```

### 3. Compilar y ejecutar

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

La aplicación se levantará en **http://localhost:8080**.

### 4. Acceder a Swagger UI

Abra en el navegador:

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Credenciales de Administrador

| Campo       | Valor              |
|-------------|--------------------|
| Correo      | `admin@sgi.com`    |
| Contraseña  | `Admin1234`        |

---

## 📡 Endpoints de la API

### Autenticación

| Método | Ruta                  | Roles       | Descripción                     |
|--------|-----------------------|-------------|---------------------------------|
| POST   | `/api/auth/login`     | Público     | Iniciar sesión y obtener JWT    |
| POST   | `/api/auth/register`  | ADMIN       | Registrar un nuevo usuario      |

### Usuarios

| Método | Ruta                        | Roles           | Descripción                     |
|--------|-----------------------------|-----------------|---------------------------------|
| GET    | `/api/usuarios`             | ADMIN, GERENTE  | Listar todos los usuarios       |
| GET    | `/api/usuarios/{id}`        | ADMIN, GERENTE  | Obtener usuario por ID          |
| PUT    | `/api/usuarios/{id}`        | ADMIN           | Actualizar usuario              |
| DELETE | `/api/usuarios/{id}`        | ADMIN           | Desactivar usuario              |
| GET    | `/api/usuarios/me`          | Autenticado     | Obtener perfil propio           |

### Productos

| Método | Ruta                        | Roles                              | Descripción                      |
|--------|-----------------------------|-------------------------------------|----------------------------------|
| GET    | `/api/productos`            | Autenticado                        | Listar todos los productos       |
| GET    | `/api/productos/{id}`       | Autenticado                        | Obtener producto por ID          |
| POST   | `/api/productos`            | ADMIN, GERENTE, JEFE_ALMACEN       | Crear producto                   |
| PUT    | `/api/productos/{id}`       | ADMIN, GERENTE, JEFE_ALMACEN       | Actualizar producto              |
| DELETE | `/api/productos/{id}`       | ADMIN, GERENTE                     | Desactivar producto              |

### Categorías

| Método | Ruta                        | Roles                              | Descripción                      |
|--------|-----------------------------|-------------------------------------|----------------------------------|
| GET    | `/api/categorias`           | Autenticado                        | Listar todas las categorías      |
| GET    | `/api/categorias/{id}`      | Autenticado                        | Obtener categoría por ID         |
| POST   | `/api/categorias`           | ADMIN, GERENTE                     | Crear categoría                  |
| PUT    | `/api/categorias/{id}`      | ADMIN, GERENTE                     | Actualizar categoría             |
| DELETE | `/api/categorias/{id}`      | ADMIN                              | Eliminar categoría               |

### Sucursales

| Método | Ruta                        | Roles                              | Descripción                      |
|--------|-----------------------------|-------------------------------------|----------------------------------|
| GET    | `/api/sucursales`           | Autenticado                        | Listar todas las sucursales      |
| GET    | `/api/sucursales/{id}`      | Autenticado                        | Obtener sucursal por ID          |
| POST   | `/api/sucursales`           | ADMIN                              | Crear sucursal                   |
| PUT    | `/api/sucursales/{id}`      | ADMIN                              | Actualizar sucursal              |
| DELETE | `/api/sucursales/{id}`      | ADMIN                              | Desactivar sucursal              |

### Stock por Sucursal

| Método | Ruta                                          | Roles                                    | Descripción                              |
|--------|-----------------------------------------------|------------------------------------------|------------------------------------------|
| GET    | `/api/stock`                                  | Autenticado                              | Listar todo el stock                     |
| GET    | `/api/stock/sucursal/{sucursalId}`            | Autenticado                              | Stock por sucursal                       |
| GET    | `/api/stock/producto/{productoId}`            | Autenticado                              | Stock de un producto en todas las sedes  |
| POST   | `/api/stock`                                  | ADMIN, GERENTE, JEFE_ALMACEN             | Crear entrada de stock                   |
| PUT    | `/api/stock/{id}`                             | ADMIN, GERENTE, JEFE_ALMACEN             | Actualizar cantidad de stock             |
| DELETE | `/api/stock/{id}`                             | ADMIN, GERENTE                           | Eliminar entrada de stock                |

### Reportes / Dashboard

| Método | Ruta                                          | Roles              | Descripción                              |
|--------|-----------------------------------------------|---------------------|------------------------------------------|
| GET    | `/api/reportes/stock-bajo`                    | ADMIN, GERENTE      | Productos con stock bajo mínimo          |
| GET    | `/api/reportes/resumen`                       | ADMIN, GERENTE      | Resumen general del inventario           |

---

## 📁 Estructura del Proyecto

```
sgi-backend/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/pe/edu/utp/sgi/
        │   ├── SgiApplication.java
        │   ├── config/
        │   │   ├── CorsConfig.java
        │   │   ├── OpenApiConfig.java
        │   │   └── SecurityConfig.java
        │   ├── security/
        │   │   ├── JwtAuthenticationFilter.java
        │   │   ├── JwtTokenProvider.java
        │   │   └── CustomUserDetailsService.java
        │   ├── model/
        │   │   ├── Rol.java
        │   │   ├── Usuario.java
        │   │   ├── Categoria.java
        │   │   ├── Producto.java
        │   │   ├── Sucursal.java
        │   │   └── StockSucursal.java
        │   ├── repository/
        │   │   ├── RolRepository.java
        │   │   ├── UsuarioRepository.java
        │   │   ├── CategoriaRepository.java
        │   │   ├── ProductoRepository.java
        │   │   ├── SucursalRepository.java
        │   │   └── StockSucursalRepository.java
        │   ├── dto/
        │   │   ├── request/
        │   │   └── response/
        │   ├── service/
        │   │   ├── AuthService.java
        │   │   ├── UsuarioService.java
        │   │   ├── ProductoService.java
        │   │   ├── CategoriaService.java
        │   │   ├── SucursalService.java
        │   │   └── StockSucursalService.java
        │   ├── controller/
        │   │   ├── AuthController.java
        │   │   ├── UsuarioController.java
        │   │   ├── ProductoController.java
        │   │   ├── CategoriaController.java
        │   │   ├── SucursalController.java
        │   │   ├── StockSucursalController.java
        │   │   └── ReporteController.java
        │   └── exception/
        │       ├── GlobalExceptionHandler.java
        │       └── ResourceNotFoundException.java
        └── resources/
            ├── application.properties
            └── data.sql
```

---

## 📌 Reglas de Negocio

1. **Roles y permisos**: Cada usuario tiene un único rol. Los permisos se validan a nivel de endpoint.
2. **Stock mínimo**: Cada producto define un `stock_minimo`. Cuando el stock en una sucursal cae por debajo de ese valor, el producto aparece en el reporte de stock bajo.
3. **Sucursales**: Los usuarios (excepto ADMIN) están asignados a una sucursal específica.
4. **SKU único**: Cada producto tiene un código SKU que debe ser único en todo el sistema.
5. **Eliminación lógica**: Los productos, usuarios y sucursales se desactivan (campo `activo = false`) en lugar de eliminarse físicamente.
6. **Autenticación JWT**: Todas las peticiones (excepto login) requieren un token JWT válido en el header `Authorization: Bearer <token>`.
7. **Datos iniciales**: Al ejecutar la aplicación por primera vez, se cargan automáticamente roles, sucursales, categorías, productos y un usuario administrador.

---

## 🛠️ Tecnologías

- **Backend**: Java 17, Spring Boot 3.2.5
- **Seguridad**: Spring Security + JWT (jjwt 0.12.5)
- **Base de datos**: MySQL 8.0 + Spring Data JPA / Hibernate
- **Documentación**: SpringDoc OpenAPI 2.5.0 (Swagger UI)
- **Utilidades**: Lombok, Bean Validation

---

## 📄 Licencia

Proyecto académico — Universidad Tecnológica del Perú (UTP) — 2026.
