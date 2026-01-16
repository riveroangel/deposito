-- Insertar datos iniciales si la tabla está vacía
INSERT INTO deposito (codigo, nombre, direccion, ciudad, responsable, telefono, observaciones, activo, es_principal, capacidad_maxima, fecha_creacion, fecha_actualizacion)
SELECT 'DEP-001', 'Depósito Central', 'Av. Principal 123', 'Lima', 'Juan Pérez', '987654321', 'Depósito principal del sistema', true, true, 10000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM deposito WHERE codigo = 'DEP-001');

INSERT INTO deposito (codigo, nombre, direccion, ciudad, responsable, telefono, observaciones, activo, es_principal, capacidad_maxima, fecha_creacion, fecha_actualizacion)
SELECT 'DEP-002', 'Almacén Norte', 'Calle Norte 456', 'Trujillo', 'María López', '987654322', 'Almacén para productos del norte', true, false, 5000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM deposito WHERE codigo = 'DEP-002');

INSERT INTO deposito (codigo, nombre, direccion, ciudad, responsable, telefono, observaciones, activo, es_principal, capacidad_maxima, fecha_creacion, fecha_actualizacion)
SELECT 'DEP-003', 'Bodega Sur', 'Jr. Sur 789', 'Arequipa', 'Carlos Gómez', '987654323', 'Bodega para productos del sur', true, false, 3000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM deposito WHERE codigo = 'DEP-003');

INSERT INTO deposito (codigo, nombre, direccion, ciudad, responsable, telefono, observaciones, activo, es_principal, capacidad_maxima, fecha_creacion, fecha_actualizacion)
SELECT 'DEP-004', 'Centro Distribución Este', 'Av. Este 101', 'Ica', 'Ana Torres', '987654324', 'Centro de distribución regional', true, false, 8000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM deposito WHERE codigo = 'DEP-004');

INSERT INTO deposito (codigo, nombre, direccion, ciudad, responsable, telefono, observaciones, activo, es_principal, capacidad_maxima, fecha_creacion, fecha_actualizacion)
SELECT 'DEP-005', 'Almacén Temporal', 'Calle Temporal 202', 'Lima', 'Pedro Ruiz', '987654325', 'Almacén para productos temporales', false, false, 2000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM deposito WHERE codigo = 'DEP-005');