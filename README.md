# 🚗 AutosColombia - Sistema de Gestión de Parqueadero

Sistema web para la gestión integral de un parqueadero, desarrollado con Spring Boot, JPA/Hibernate y Thymeleaf.

## 📋 Características

- ✅ Autenticación y autorización de administradores
- ✅ Registro de ingreso y salida de vehículos
- ✅ Cálculo automático de tarifas según tipo de vehículo y tiempo
- ✅ Gestión de clientes y vehículos
- ✅ Búsqueda de vehículos por placa y clientes por cédula
- ✅ Visualización de vehículos actualmente parqueados
- ✅ Múltiples métodos de cobro (por hora, mensualidad, día completo)
- ✅ Interfaz web responsive con Thymeleaf

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Boot DevTools
- **Thymeleaf** - Motor de plantillas
- **Lombok** - Reducción de código boilerplate
- **H2 Database** - Base de datos en memoria para desarrollo
- **MySQL/PostgreSQL** - Opciones para producción
- **Maven** - Gestión de dependencias

## 📁 Estructura del Proyecto

```
AutosColombia/
├── src/
│   ├── main/
│   │   ├── java/com/autoscolombia/
│   │   │   ├── AutosColombiaApplication.java
│   │   │   ├── config/
│   │   │   │   └── DataInitializer.java
│   │   │   ├── domain/
│   │   │   │   ├── Administrador.java
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Vehiculo.java
│   │   │   │   ├── RegistroParqueo.java
│   │   │   │   ├── Mensualidad.java
│   │   │   │   ├── TipoVehiculo.java
│   │   │   │   ├── MetodoCobro.java
│   │   │   │   └── LugarActual.java
│   │   │   ├── repository/
│   │   │   │   ├── AdministradorRepository.java
│   │   │   │   ├── ClienteRepository.java
│   │   │   │   ├── VehiculoRepository.java
│   │   │   │   ├── RegistroParqueoRepository.java
│   │   │   │   └── MensualidadRepository.java
│   │   │   ├── security/
│   │   │   │   ├── AdminUserDetailsService.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── service/
│   │   │   │   └── ParqueaderoService.java
│   │   │   └── web/
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   └── ParqueaderoController.java
│   │   │       └── dto/
│   │   │           ├── IngresoForm.java
│   │   │           ├── SalidaForm.java
│   │   │           └── BuscarForm.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/
│   │       │   ├── login.html
│   │       │   ├── menu.html
│   │       │   ├── ingreso.html
│   │       │   ├── salida.html
│   │       │   ├── buscar.html
│   │       │   └── parqueados.html
│   │       └── static/
│   │           └── css/
│   │               └── style.css
│   └── test/
│       └── java/
└── pom.xml
```

## 🚀 Instalación y Ejecución

### Prerrequisitos

- JDK 17 o superior
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VSCode)

### Pasos

1. **Clonar o descargar el proyecto**

2. **Configurar la base de datos**

   Editar `src/main/resources/application.properties`:

   - Para desarrollo con H2 (por defecto):
     ```properties
     spring.datasource.url=jdbc:h2:mem:autoscolombia
     spring.h2.console.enabled=true
     ```

   - Para MySQL:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/autoscolombia
     spring.datasource.username=root
     spring.datasource.password=tu_password
     ```

3. **Compilar el proyecto**

   ```bash
   mvn clean install
   ```

4. **Ejecutar la aplicación**

   ```bash
   mvn spring-boot:run
   ```

   O desde tu IDE, ejecutar la clase `AutosColombiaApplication.java`

5. **Acceder a la aplicación**

   Abrir el navegador en: `http://localhost:8080`

   **Credenciales por defecto:**
   - Usuario: `admin`
   - Contraseña: `admin123`

## 💾 Base de Datos

### Modelo de Datos

- **Administrador**: Usuarios del sistema con acceso al parqueadero
- **Cliente**: Propietarios de vehículos
- **Vehiculo**: Información de los vehículos registrados
- **RegistroParqueo**: Histórico de ingresos y salidas
- **Mensualidad**: Pagos mensuales de clientes

### Datos de Ejemplo

Al iniciar la aplicación por primera vez, se crean automáticamente:
- 1 administrador (admin/admin123)
- 2 clientes de ejemplo
- 2 vehículos de ejemplo (ABC123, XYZ789)

## 💰 Tarifas Configuradas

- **Carro/Camioneta/Camión**: $3,000 por hora
- **Moto**: $2,000 por hora
- **Bicicleta**: $1,000 por hora
- **Mensualidad**: Sin cobro adicional por estacionamiento

## 🔐 Seguridad

- Autenticación mediante Spring Security
- Contraseñas encriptadas con BCrypt
- Sesiones seguras
- Protección CSRF
- Acceso restringido a recursos

## 📱 Funcionalidades Principales

### 1. Login
- Autenticación de administradores
- Mensajes de error y confirmación

### 2. Menú Principal
- Dashboard con acceso rápido a todas las funciones
- Información del usuario logueado

### 3. Registro de Ingreso
- Formulario para registrar entrada de vehículos
- Selección de método de cobro
- Campos de observaciones

### 4. Registro de Salida
- Cálculo automático del valor a cobrar
- Basado en tiempo y tipo de vehículo
- Actualización del estado del vehículo

### 5. Búsqueda
- Búsqueda por placa de vehículo
- Búsqueda por cédula de cliente
- Visualización de información detallada

### 6. Vehículos Parqueados
- Lista de vehículos actualmente en el parqueadero
- Tiempo de permanencia en tiempo real
- Estadísticas del parqueadero

## 🔧 Configuración Adicional

### Cambiar Puerto del Servidor

En `application.properties`:
```properties
server.port=8081
```

### Habilitar Consola H2

Para acceder a la consola de H2 Database:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:autoscolombia`
- Usuario: `sa`
- Password: (dejar vacío)

## 📝 Notas

- El sistema calcula cobros con un mínimo de 1 hora
- Los vehículos con mensualidad no tienen cobro adicional
- Se mantiene un histórico completo de todos los registros
- Las contraseñas se almacenan encriptadas

## 🤝 Contribuciones

Este es un proyecto educativo/empresarial. Para contribuir o reportar problemas, contactar con el equipo de desarrollo.

## 📄 Licencia

Proyecto privado - Todos los derechos reservados © 2026 AutosColombia

## 👥 Contacto

Para soporte o consultas sobre el sistema, contactar con el administrador del sistema.

---

**Desarrollado con ❤️ usando Spring Boot**

