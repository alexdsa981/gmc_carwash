-- Insertar servicios b�sicos
INSERT INTO [dbo].[tipo_servicio] (nombre, descripcion, is_especial) VALUES
('Lavado Basico', 'Lavado b�sico exterior', 0),
('Lavado Basico + Cera', 'Lavado b�sico con aplicaci�n de cera', 0),
('Lavado Basico + Cera Carnauba', 'Lavado b�sico con cera de carnauba', 0);

-- Insertar servicios especiales
INSERT INTO [dbo].[tipo_servicio] (nombre, descripcion, is_especial) VALUES
('Lavado Salon', 'Lavado completo del sal�n del veh�culo', 1),
('Lavado Asientos', 'Lavado y desinfecci�n de asientos', 1),
('Lavado Cinturones', 'Limpieza y desinfecci�n de cinturones de seguridad', 1),
('Lavado de Techo', 'Limpieza del techo interior del veh�culo', 1),
('Lavado de Pisos', 'Limpieza profunda de los pisos del veh�culo', 1),
('Tratamiento de Cueros', 'Cuidado y tratamiento de superficies de cuero', 1),
('Tratamiento de Faros', 'Restauraci�n y protecci�n de faros', 1),
('Descontaminacion de Pinturas', 'Descontaminaci�n y limpieza de la pintura', 1),
('Lavado de Motor', 'Limpieza y desengrase del motor', 1),
('Lavado de Alfombra', 'Limpieza profunda de alfombras', 1),
('Lavado de Chasis', 'Lavado y protecci�n del chasis del veh�culo', 1);