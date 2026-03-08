# 🚀 GUÍA RÁPIDA DE INICIO - AutosColombia

## ✅ Estructura Creada Exitosamente

Se ha creado la estructura completa del proyecto AutosColombia con:

### 📂 Archivos Principales
- ✅ pom.xml (configuración de Maven)
- ✅ application.properties (configuración de la aplicación)
- ✅ README.md (documentación completa)

### 🗂️ Paquetes Java
- ✅ **domain** (8 clases): Entidades JPA (Administrador, Cliente, Vehiculo, etc.)
- ✅ **repository** (5 interfaces): Repositorios Spring Data JPA
- ✅ **security** (2 clases): Configuración de seguridad y autenticación
- ✅ **service** (1 clase): Lógica de negocio del parqueadero
- ✅ **web.controller** (2 clases): Controladores MVC
- ✅ **web.dto** (3 clases): Objetos de transferencia de datos
- ✅ **config** (1 clase): Inicializador de datos

### 🎨 Templates HTML (6 archivos)
- ✅ login.html
- ✅ menu.html
- ✅ ingreso.html
- ✅ salida.html
- ✅ buscar.html
- ✅ parqueados.html

### 📁 Recursos Estáticos
- ✅ style.css (estilos CSS)

---

## 🔧 PASOS PARA EJECUTAR EL PROYECTO

### 1️⃣ Descargar Dependencias
```powershell
cd C:\Users\LADY\IdeaProjects\AutosColombia
mvn clean install
```

### 2️⃣ Ejecutar la Aplicación

**Opción A: Desde la terminal**
```powershell
mvn spring-boot:run
```

**Opción B: Desde IntelliJ IDEA**
1. Abrir el proyecto en IntelliJ
2. Esperar a que Maven descargue las dependencias
3. Buscar la clase `AutosColombiaApplication.java`
4. Click derecho → Run 'AutosColombiaApplication'

### 3️⃣ Acceder a la Aplicación

1. Abrir navegador en: **http://localhost:8080**
2. Usar las credenciales por defecto:
   - **Usuario:** `admin`
   - **Contraseña:** `admin123`

---

## 🗄️ BASE DE DATOS

Por defecto, el proyecto está configurado con **H2 Database** (en memoria).

### Acceder a la Consola H2:
- URL: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:autoscolombia`
- Usuario: `sa`
- Password: (dejar vacío)

### Cambiar a MySQL (Opcional):

1. Editar `src/main/resources/application.properties`
2. Comentar las líneas de H2
3. Descomentar las líneas de MySQL:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/autoscolombia
   spring.datasource.username=root
   spring.datasource.password=tu_password
   ```
4. Crear la base de datos:
   ```sql
   CREATE DATABASE autoscolombia;
   ```

---

## 📊 DATOS DE EJEMPLO

Al iniciar la aplicación, se crean automáticamente:

### 👤 Administrador
- Usuario: `admin`
- Contraseña: `admin123`

### 🚗 Vehículos de Prueba
- **ABC123** - Toyota Corolla 2020 (Carro)
- **XYZ789** - Yamaha FZ 2021 (Moto)

### 👥 Clientes de Prueba
- **Juan Carlos Pérez** - CC: 1234567890
- **María Fernanda López** - CC: 9876543210

---

## 🎯 FUNCIONALIDADES DISPONIBLES

### 1. Registrar Ingreso
- Ingresar placa del vehículo
- Seleccionar método de cobro
- Agregar observaciones

### 2. Registrar Salida
- Ingresar placa del vehículo
- Cálculo automático del cobro
- Basado en tiempo y tipo de vehículo

### 3. Buscar
- Por placa de vehículo
- Por cédula de cliente

### 4. Ver Parqueados
- Lista de vehículos actualmente en el parqueadero
- Tiempo de permanencia
- Información del cliente

---

## 💰 TARIFAS CONFIGURADAS

| Tipo de Vehículo | Tarifa por Hora |
|-----------------|----------------|
| Carro/Camioneta | $3,000 |
| Moto | $2,000 |
| Bicicleta | $1,000 |
| Mensualidad | Sin cobro adicional |

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Error: "Puerto 8080 en uso"
```properties
# Cambiar en application.properties
server.port=8081
```

### Error: "Cannot resolve springframework"
```powershell
# Recargar dependencias de Maven
mvn clean install -U
```

### Error al compilar
```powershell
# Verificar versión de Java (debe ser 17+)
java -version

# Limpiar y recompilar
mvn clean compile
```

---

## 📝 PRÓXIMOS PASOS

1. ✅ **Probar el sistema** con los datos de ejemplo
2. ⭐ **Personalizar** las tarifas en `ParqueaderoService.java`
3. 🎨 **Modificar** los estilos CSS según preferencias
4. 📊 **Agregar** más funcionalidades (reportes, estadísticas, etc.)
5. 🗄️ **Configurar** base de datos productiva (MySQL/PostgreSQL)

---

## 📞 SOPORTE

Si tienes problemas:
1. Revisar los logs en la consola
2. Verificar que todas las dependencias se descargaron
3. Consultar el README.md para más detalles

---

**¡El proyecto está listo para usar! 🎉**

Desarrollado con Spring Boot 3.2.0 + Java 17

