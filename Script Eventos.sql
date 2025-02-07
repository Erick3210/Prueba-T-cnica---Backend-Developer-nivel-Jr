show databases;

use mysql;

CREATE DATABASE gestion_eventos;

USE gestion_eventos;

CREATE TABLE evento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Clave primaria con auto-incremento
    nombre VARCHAR(255) NOT NULL,          -- Nombre del evento, máximo 255 caracteres
    fecha_inicio VARCHAR(10) NOT NULL,     -- Fecha de inicio en formato 'YYYY-MM-DD'
    fecha_fin VARCHAR(10) NOT NULL,        -- Fecha de fin en formato 'YYYY-MM-DD'
    boletos_disponibles INT NOT NULL,      -- Boletos disponibles, sin restricciones de rango
    boletos_vendidos INT DEFAULT 0,        -- Inicializa el número de boletos vendidos en 0
    boletos_canjeados INT DEFAULT 0        -- Inicializa el número de boletos canjeados en 0
);

select * from evento;


SELECT fecha_inicio, fecha_fin FROM evento WHERE id = 1;

UPDATE evento SET fecha_inicio = '2024-12-01' WHERE id = 1;

DESCRIBE evento;
ALTER TABLE evento
MODIFY COLUMN fecha_inicio TEXT NULL,
MODIFY COLUMN fecha_fin TEXT NULL,
MODIFY COLUMN boletos_disponibles INT NULL;


ALTER TABLE evento
MODIFY COLUMN boletos_vendidos INT NULL,
MODIFY COLUMN boletos_canjeados INT NULL;


