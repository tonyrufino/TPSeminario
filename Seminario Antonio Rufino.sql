create database TaskManager;
use TaskManager;

CREATE TABLE GRUPO (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE USUARIO (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    grupo_id INT,
    FOREIGN KEY (grupo_id) REFERENCES GRUPO(id)
);

CREATE TABLE PROYECTO (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    vencimiento DATE NOT NULL,
    completo BOOLEAN
);

CREATE TABLE TAREA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT NOT NULL,
    prioridad VARCHAR(20),
    completa BOOLEAN,
    vencimiento DATE NOT NULL,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (proyecto_id) REFERENCES PROYECTO(id)
);

CREATE TABLE USUARIO_TAREA (
    usuario_id INT NOT NULL,
    tarea_id INT NOT NULL,
    PRIMARY KEY (usuario_id, tarea_id),
    FOREIGN KEY (usuario_id) REFERENCES USUARIO(id),
    FOREIGN KEY (tarea_id) REFERENCES TAREA(id)
);

-- poblar tablas
INSERT INTO `taskmanager`.`grupo` (`nombre`) VALUES ('RRHH');

INSERT INTO `taskmanager`.`proyecto` (`nombre`, `descripcion`, `vencimiento`, `completo`) VALUES ('Generico', 'Generar un proyecto generico para la correción del TP', '2025-10-10', '0');

INSERT INTO `taskmanager`.`tarea` (`descripcion`, `prioridad`, `completa`, `vencimiento`, `proyecto_id`) VALUES ('Testear Funcionalidad de la base de datos', 'Alta', '0', '2025-10-10', '1');

INSERT INTO `taskmanager`.`usuario` (`nombre`, `rol`, `grupo_id`) VALUES ('CarlosRamirez', 'Usuario', '1');

INSERT INTO `taskmanager`.`usuario_tarea` (`usuario_id`, `tarea_id`) VALUES ('1', '1');


-- consulta 1
SELECT t.descripcion, g.nombre
FROM TAREA t
JOIN GRUPO g ON t.grupo_id = g.id
WHERE g.nombre = 'RRHH';

-- consulta 2
SELECT t.descripcion, t.estado, t.vencimiento
FROM TAREA t
JOIN USUARIO_TAREA ut ON t.id = ut.tarea_id
JOIN USUARIO u ON ut.usuario_id = u.id
WHERE u.nombre = 'CarlosRamirez';



