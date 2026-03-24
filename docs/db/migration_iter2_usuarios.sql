-- ==========================================
-- AutosColombia – Migración Iteración 2
-- Gestión de Usuarios (Administrador + Empleado)
-- Aplica sobre base existente de Iteración 1
-- MySQL 8.x
-- ==========================================

USE parqueadero_db;

-- =============================================================
-- 1) Ampliar tabla administrador con campos de perfil
-- =============================================================
ALTER TABLE administrador
  ADD COLUMN nombre_completo VARCHAR(120) NULL AFTER contrasena_hash,
  ADD COLUMN documento        VARCHAR(20)  NULL AFTER nombre_completo,
  ADD COLUMN email            VARCHAR(120) NULL AFTER documento,
  ADD COLUMN telefono         VARCHAR(30)  NULL AFTER email,
  ADD UNIQUE KEY uq_administrador_documento (documento),
  ADD UNIQUE KEY uq_administrador_email (email);

-- =============================================================
-- 2) Crear tabla empleado (si no existe)
-- =============================================================
CREATE TABLE IF NOT EXISTS empleado (
  id_empleado      BIGINT        AUTO_INCREMENT PRIMARY KEY,
  nombre_completo  VARCHAR(120)  NOT NULL,
  documento        VARCHAR(20)   NOT NULL,
  email            VARCHAR(120)  NOT NULL,
  telefono         VARCHAR(30)   NOT NULL,
  contrasena_hash  VARCHAR(255)  NOT NULL,

  -- RF-U06: control de acceso
  intentos_fallidos INT       NOT NULL DEFAULT 0,
  bloqueo_hasta     DATETIME  NULL,
  ultimo_login      DATETIME  NULL,

  UNIQUE KEY uq_empleado_documento (documento),
  UNIQUE KEY uq_empleado_email     (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================================
-- 3) Actualizar administrador demo con los nuevos campos
--    (ajusta el nombre_usuario si es diferente en tu instancia)
-- =============================================================
UPDATE administrador
   SET nombre_completo = 'Laura Administradora',
       documento        = '1000000000',
       email            = 'admin@autoscolombia.com',
       telefono         = '3009876543'
 WHERE nombre_usuario = 'Laura'
   AND documento IS NULL;

-- =============================================================
-- FIN DE LA MIGRACIÓN
-- =============================================================
