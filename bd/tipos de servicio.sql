-- Insertar servicios básicos
INSERT INTO [dbo].[tipo_servicio] (nombre, descripcion, is_especial) VALUES
('Lavado Basico', 'Lavado básico exterior', 0),
('Lavado Basico + Cera', 'Lavado básico con aplicación de cera', 0),
('Lavado Basico + Cera Carnauba', 'Lavado básico con cera de carnauba', 0);

-- Insertar servicios especiales
INSERT INTO [dbo].[tipo_servicio] (nombre, descripcion, is_especial) VALUES
('Lavado Salon', 'Lavado completo del salón del vehículo', 1),
('Lavado Asientos', 'Lavado y desinfección de asientos', 1),
('Lavado Cinturones', 'Limpieza y desinfección de cinturones de seguridad', 1),
('Lavado de Techo', 'Limpieza del techo interior del vehículo', 1),
('Lavado de Pisos', 'Limpieza profunda de los pisos del vehículo', 1),
('Tratamiento de Cueros', 'Cuidado y tratamiento de superficies de cuero', 1),
('Tratamiento de Faros', 'Restauración y protección de faros', 1),
('Descontaminacion de Pinturas', 'Descontaminación y limpieza de la pintura', 1),
('Lavado de Motor', 'Limpieza y desengrase del motor', 1),
('Lavado de Alfombra', 'Limpieza profunda de alfombras', 1),
('Lavado de Chasis', 'Lavado y protección del chasis del vehículo', 1);