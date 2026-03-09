# 🚀 Guía de Instalación Completa - AutosColombia

## 📋 Requisitos Previos

### Windows 10/11 con PowerShell

---

## 🔧 Paso 1: Instalar JDK 17

Abre **PowerShell como Administrador** y ejecuta:

```powershell
winget install -e --id EclipseAdoptium.Temurin.17.JDK
```

**Verifica la instalación:**
```powershell
java -version
javac -version
```

Deberías ver algo como:
```
openjdk version "17.0.x"
```

---

## 📦 Paso 2: Instalar Apache Maven

En PowerShell:

```powershell
winget install -e --id Apache.Maven
```

**Cierra y vuelve a abrir PowerShell**, luego verifica:

```powershell
mvn -version
```

Deberías ver:
```
Apache Maven 3.x.x
```

---

## 🗄️ Paso 3: Instalar y Configurar MySQL

### Opción A: Instalar MySQL Server

```powershell
winget install -e --id Oracle.MySQL
```

### Opción B: Usar XAMPP (más fácil)

1. Descarga XAMPP desde: https://www.apachefriends.org/
2. Instala y ejecuta **MySQL** desde el panel de control
3. Por defecto usa:
   - Puerto: `3306`
   - Usuario: `root`
   - Password: (vacío o el que configures)

### Crear Base de Datos

Abre MySQL Workbench o phpMyAdmin y ejecuta:

```sql
CREATE DATABASE parqueadero_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## ⚙️ Paso 4: Configurar el Proyecto

### 4.1 Navega a la carpeta del proyecto:

```powershell
cd C:\Users\LADY\IdeaProjects\AutosColombia
```

### 4.2 Edita `src/main/resources/application.properties`

**Para MySQL:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/parqueadero_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD_AQUI
spring.jpa.hibernate.ddl-auto=update
```

**Para H2 (desarrollo rápido sin MySQL):**
```properties
spring.datasource.url=jdbc:h2:mem:autosdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

---

## 🏗️ Paso 5: Compilar el Proyecto

```powershell
mvn clean install
```

Si todo está bien, verás:
```
[INFO] BUILD SUCCESS
```

---

## ▶️ Paso 6: Ejecutar la Aplicación

```powershell
mvn spring-boot:run
```

Espera a ver:
```
Started AutosColombiaApplication in X.XXX seconds
```

---

## 🌐 Paso 7: Acceder a la Aplicación

Abre tu navegador en:
```
http://localhost:8080
```

### Credenciales por defecto:
- **Usuario:** `admin`
- **Contraseña:** `admin123` (revisa `DataInitializer.java` para confirmar)

---

## 🎨 ¿Qué Mejoras Visuales Verás?

### ✨ Pantalla de Login
- Diseño centrado tipo "card" con sombras
- Fondo con gradiente morado elegante
- Alertas estilizadas de error/éxito
- Animación de entrada suave

### 🏠 Menú Principal
- Grid de 4 tarjetas interactivas con iconos
- Efecto hover: elevación y cambio de sombra
- Botón de cerrar sesión visible
- Animación secuencial de entrada

### 📥 Registro de Ingreso
- Formulario organizado en secciones
- Validación en tiempo real:
  - Placa → Mayúsculas automáticas
  - VIN → Validación de 17 caracteres
  - Cédula/Teléfono → Solo números
- Diseño responsive para móvil

### 📤 Registro de Salida
- Diseño simplificado y enfocado
- Confirmación antes de procesar
- Feedback visual inmediato

### 🔍 Buscar Registro
- Resultados en tarjeta con gradiente
- Información organizada en grid
- Estados visuales claros

### 🅿️ Vehículos Parqueados
- Tabla profesional con hover effects
- Búsqueda en tiempo real (filtro)
- Badges de colores para estados
- Scroll personalizado
- Responsive con scroll horizontal en móvil

---

## 🎯 Funcionalidades JavaScript Activas

1. **Auto-cierre de alertas** (5 segundos)
2. **Validación de placas** (mayúsculas automáticas)
3. **Validación de VIN** (17 caracteres con feedback visual)
4. **Solo números** en cédula y teléfono
5. **Confirmaciones** antes de cerrar sesión o procesar salida
6. **Búsqueda en tabla** en tiempo real
7. **Animaciones** de entrada en menú

---

## 🛠️ Solución de Problemas

### Error: "mvn no se reconoce"
**Solución:** Cierra y vuelve a abrir PowerShell después de instalar Maven.

### Error: "Cannot resolve table"
**Solución:** Son advertencias del IDE, no errores reales. Ignóralas.

### Error: "Communications link failure"
**Solución:** 
1. Verifica que MySQL esté corriendo
2. Confirma usuario/password en `application.properties`
3. O usa H2 en memoria (más fácil para probar)

### Error: "Port 8080 already in use"
**Solución:** Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

### La página se ve sin estilos
**Solución:**
1. Limpia y reconstruye: `mvn clean install`
2. Verifica que `src/main/resources/static/css/style.css` existe
3. Limpia caché del navegador (Ctrl + Shift + Delete)

---

## 📱 Responsive Design

La aplicación se adapta automáticamente a:
- 📱 **Móviles** (< 768px): Layout de 1 columna
- 📲 **Tablets** (768px - 1024px): Layout de 2 columnas
- 💻 **Desktop** (> 1024px): Layout completo

---

## 🎨 Paleta de Colores

```css
Primary:    #667eea  (Morado principal)
Secondary:  #764ba2  (Morado oscuro)
Success:    #28a745  (Verde)
Danger:     #dc3545  (Rojo)
Warning:    #ffc107  (Amarillo)
Info:       #17a2b8  (Azul)
```

---

## 📚 Estructura de Archivos Actualizada

```
src/main/resources/
├── application.properties     ← Configuración BD
├── static/
│   ├── css/
│   │   └── style.css         ← 300+ líneas de estilos modernos ✨
│   └── js/
│       └── main.js           ← 185 líneas de interactividad ⚡
└── templates/
    ├── login.html            ← Modernizado ✅
    ├── menu.html             ← Grid de tarjetas ✅
    ├── ingreso.html          ← Formulario mejorado ✅
    ├── salida.html           ← Simplificado ✅
    ├── buscar.html           ← Resultados en card ✅
    └── parqueados.html       ← Tabla profesional + búsqueda ✅
```

---

## 🚀 Comandos Rápidos

```powershell
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run

# Ejecutar en modo debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

# Crear JAR ejecutable
mvn clean package
java -jar target/autos-colombia-1.0.0.jar

# Ver logs en tiempo real (en otra terminal)
Get-Content -Path "logs/spring-boot.log" -Wait
```

---

## 📞 Soporte

Si encuentras errores:

1. **Revisa los logs** en la terminal donde ejecutaste `mvn spring-boot:run`
2. **Busca la línea "Caused by:"** que indica la causa raíz
3. **Verifica:**
   - ✅ JDK 17 instalado
   - ✅ Maven instalado
   - ✅ MySQL corriendo (o usa H2)
   - ✅ Puerto 8080 disponible

---

## 🎉 ¡Listo!

Tu aplicación **AutosColombia** ahora tiene:
- ✨ Interfaz moderna y profesional
- 🎨 Diseño responsive
- ⚡ Validaciones en tiempo real
- 🔍 Búsqueda instantánea
- 📊 Tablas profesionales
- 🎭 Animaciones suaves

**¡Disfruta tu nuevo sistema de parqueadero! 🚗🏍️**

