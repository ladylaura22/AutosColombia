-- =====================================================
-- Iteración 3: Gestión de Pagos — AutosColombia
-- =====================================================

-- Tabla de tarifas del parqueadero
CREATE TABLE IF NOT EXISTS tarifa (
    id_tarifa         INT AUTO_INCREMENT PRIMARY KEY,
    tarifa_hora_carro DECIMAL(12, 2) NOT NULL,
    tarifa_hora_moto  DECIMAL(12, 2) NOT NULL,
    tarifa_mensual    DECIMAL(12, 2) NOT NULL,
    activo            BOOLEAN        NOT NULL DEFAULT TRUE,
    creado_en         DATETIME,
    modificado_en     DATETIME
);

-- Tabla de pagos
CREATE TABLE IF NOT EXISTS pago (
    id_pago           INT AUTO_INCREMENT PRIMARY KEY,
    tipo_pago         VARCHAR(20)    NOT NULL,  -- HORAS | MENSUALIDAD
    metodo_pago       VARCHAR(20)    NOT NULL,  -- EFECTIVO | TARJETA | TRANSFERENCIA | QR | OTRO
    referencia        VARCHAR(100),
    valor             DECIMAL(12, 2) NOT NULL,
    estado            VARCHAR(10)    NOT NULL DEFAULT 'PAGADO',  -- PAGADO | ANULADO
    id_registro       INT            NULL,      -- FK -> registro_parqueo (pago por horas)
    id_mensualidad    INT            NULL,      -- FK -> mensualidad (pago mensual)
    creado_por        VARCHAR(80),
    creado_en         DATETIME,
    anulado_por       VARCHAR(80),
    anulado_en        DATETIME,
    motivo_anulacion  VARCHAR(500),
    CONSTRAINT fk_pago_registro    FOREIGN KEY (id_registro)    REFERENCES registro_parqueo(id_registro),
    CONSTRAINT fk_pago_mensualidad FOREIGN KEY (id_mensualidad) REFERENCES mensualidad(id_mensualidad)
);

-- Tarifa inicial por defecto
INSERT INTO tarifa (tarifa_hora_carro, tarifa_hora_moto, tarifa_mensual, activo, creado_en, modificado_en)
SELECT 10000.00, 5000.00, 70000.00, TRUE, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM tarifa WHERE activo = TRUE);
