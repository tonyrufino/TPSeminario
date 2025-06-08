CREATE DATABASE `taskmanager` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

CREATE TABLE `grupo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `proyecto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `vencimiento` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `tarea` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` text NOT NULL,
  `prioridad` varchar(20) DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `vencimiento` date NOT NULL,
  `idProyecto` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tarea_ibfk_1` (`idProyecto`),
  CONSTRAINT `tarea_ibfk_1` FOREIGN KEY (`idProyecto`) REFERENCES `proyecto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `grupo_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `grupo_id` (`grupo_id`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`grupo_id`) REFERENCES `grupo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `usuario_grupo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) NOT NULL,
  `idGrupo` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idUsuario` (`idUsuario`),
  KEY `idGrupo` (`idGrupo`),
  CONSTRAINT `usuario_grupo_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`id`),
  CONSTRAINT `usuario_grupo_ibfk_2` FOREIGN KEY (`idGrupo`) REFERENCES `grupo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `usuario_tarea` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) NOT NULL,
  `idTarea` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idUsuario` (`idUsuario`),
  KEY `idTarea` (`idTarea`),
  CONSTRAINT `usuario_tarea_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`id`),
  CONSTRAINT `usuario_tarea_ibfk_2` FOREIGN KEY (`idTarea`) REFERENCES `tarea` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
