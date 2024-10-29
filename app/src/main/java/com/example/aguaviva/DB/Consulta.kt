package com.example.aguaviva.DB

class Consulta {

    companion object
    {

        var crearTablaUsuario: String =
            """
               CREATE TABLE IF NOT EXISTS `Usuario`
                (
                    `id_usuario` INTEGER NOT NULL,
                    `nombre_usuario` VARCHAR(20) NOT NULL UNIQUE,
                    `contrasena_usuario` VARCHAR(64) NOT NULL, 
                    PRIMARY KEY(`id_usuario`)
                ); 
            """.trimIndent()

        var crearTablaPersona: String =
            """
                CREATE TABLE IF NOT EXISTS `Persona`
                (
                	`id_persona` INTEGER NOT NULL,
                	`cedula_persona` VARCHAR(12) NOT NULL UNIQUE, 
                	`nombre_persona` VARCHAR(20) NOT NULL, 
                	`apellido_persona` VARCHAR(20) NOT NULL,
                	`telefono_persona` VARCHAR(11) NOT NULL,
                	`correo_persona` VARCHAR(255) NOT NULL,
                	`id_usuario` INTEGER NOT NULL,
                	PRIMARY KEY(`id_persona`),
                	FOREIGN KEY(`id_usuario`) REFERENCES Usuario(`id_usuario`)
                );
            """.trimIndent()

        var crearTablaSucursales: String =
            """
                
             CREATE TABLE IF NOT EXISTS `Sucursal`
                (
                    `id_sucursal` INTEGER NOT NULL PRIMARY KEY, 
                    `nombre_sucursal` VARCHAR(20) NOT NULL,
                    `ubicacion_sucursal` VARCHAR(100) NOT NULL,
                    `latitud_sucursal` VARCHAR(11) NOT NULL,
                    `longitud_sucursal` VARCHAR(11) NOT NULL
                );   
                
            """.trimIndent()

        var crearTablaRecarga: String =
            """
               CREATE TABLE IF NOT EXISTS `Recarga`
                (
                    `id_recarga` INTEGER NOT NULL,
                    `litros_recarga` INTEGER NOT NULL,
                    `precio_recarga` REAL NOT NULL, 
                    PRIMARY KEY(`id_recarga`)
                ); 
            """.trimIndent()

        var crearTablaPago: String =
            """
               CREATE TABLE IF NOT EXISTS `Pago`
                (
                    `id_pago` INTEGER NOT NULL,
                    `fecha_pago` DATE NOT NULL,
                    `id_persona` INTEGER NOT NULL,
                    `id_sucursal` INTEGER NOT NULL, 
                    `id_recarga` INTEGER NOT NULL,
                    PRIMARY KEY(`id_pago`),
                    FOREIGN KEY(`id_recarga`) REFERENCES Recarga(`id_recarga`),
                    FOREIGN KEY(`id_sucursal`) REFERENCES Sucursal(`id_sucursal`),
                    FOREIGN KEY(`id_persona`) REFERENCES Persona(`id_persona`)
                );
            """.trimIndent()

        // Consultas de insertar datos en las tablas por defectos

        var insertarTablaSucursales: String =
            """
                
                INSERT INTO `Sucursal` 
                VALUES
                    (NULL, 
                    'La Yaguara',
                    'Av. Garci González da Silva, Zona Industrial de la Yaguara, al lado de Talleres Caracas',
                    '10.480019', '-66.961691'),
                    (NULL, 
                    'Baruta',
                    'Av. Miguelangel, edificio mendieder',
                    '10.4861624', '-66.8727462'),
                    (NULL, 
                    'Sabana Grande',
                    'Diagonal a Centro Residencial Solano, entre Av. Negrin y Av. Fransisco Solano López',
                    '10.494827', '-66.876067'),
                    (NULL, 
                    'Caricuao',
                    'Frente a la estación de metro Zoológico',
                    '10.433043', '-66.972023'),
                    
                    (NULL, 
                    'La Candelaria',
                    'Esquina entre Av. Norte 15 y Av. Urdaneta, diagonal a Casa Bera',
                    '10.505786', '-66.903691');
                
            """.trimIndent()

        var insertarTablaRecarga: String =
            """
                INSERT INTO `Recarga` 
                VALUES
                (NULL, 5, 4.5),
                (NULL, 20, 15),
                (NULL, 30, 25);
            """.trimIndent()

        var insertarUsuarioInicial: String = """
                INSERT INTO `Usuario`
                VALUES
                (
                	NULL,
                	'user',
                	'03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4'
                );
            """.trimIndent()


        var insertarPersonaInicial: String = """
            INSERT INTO `Persona`
            VALUES
            (
            	NULL, 
            	'000000001',
            	'admin',
            	'user',
            	'04XXXXXXXXX', 
            	'correo@ejemplo.com',
            	1
            );
        """.trimIndent()

        fun seleccionarUsuarioNombre(nombreUsuario: String): String
        {
            return """
                SELECT 
                	`nombre_usuario`,
                	`contrasena_usuario`
                FROM `Usuario` 
                WHERE `nombre_usuario` = '${nombreUsuario}';
            """.trimIndent()
        }

        var seleccionarNombreUbicacionSucursales: String =
            """
                SELECT `nombre_sucursal`, `ubicacion_sucursal`
                FROM `Sucursal` 
                WHERE 1;
            """.trimIndent()

        fun seleccionarCoordenadas(id: Int): String
        {

            return """
                
                SELECT `latitud_sucursal`, `longitud_sucursal`
                FROM `Sucursal`
                WHERE `id_sucursal` = ${id};
                
            """.trimIndent()

        }

        fun seleccionardatosPagoId(idUser: Int): String
        {

            return """
                
                SELECT 
                	a.`id_pago`,
                	a.`fecha_pago`,
                	b.`nombre_sucursal`,
                	c.`litros_recarga`,
                	c.`precio_recarga` 
                FROM `Pago` a
                INNER JOIN `Sucursal` b
                	ON b.`id_sucursal` = a.`id_sucursal`
                INNER JOIN `Recarga` c
                	ON c.`id_recarga` = a.`id_recarga`
                INNER JOIN `Persona` d
                	ON d.`id_persona` = a.`id_persona`
                INNER JOIN `Usuario` e
                 	ON e.`id_usuario` = d.`id_usuario`
                WHERE a.`id_persona` = $idUser
				ORDER BY a.`id_pago` DESC; 
                
            """.trimIndent()

        }

        fun seleccionarNumeroPago(idUser: Int): String
        {

            return """
                
                SELECT 
                	max(`id_pago`) 
                AS
                	`NumeroComprobante`
                FROM
                	`Pago`
                WHERE `id_persona` = $idUser;
                
            """.trimIndent()

        }

        fun seleccionarNombreSucursal(idSucursal: Int):String
        {
            return """
                SELECT
                	`nombre_sucursal`
                FROM 
                	`Sucursal`
                WHERE `id_sucursal`= $idSucursal;
            """.trimIndent()
        }

        fun seleccionarLitrosRecarga(idRecarga: Int):String
        {
            return """
                SELECT
                    `litros_recarga`
                FROM 
                    `Recarga`
                WHERE `id_recarga`= $idRecarga;
            """.trimIndent()
        }


        // Consulta para ingresar una fila en la tabla Pago
        fun insertarPago(idPersona: Long, idSucursal: Int, idRecarga: Int): String
        {

           return """
                
                INSERT INTO `Pago`
                VALUES
                (
                	
                	NULL,
                	date(),
                	$idPersona,
                	$idSucursal,
                	$idRecarga

                );
                
            """.trimIndent()


        }



        // Consultas para borrar una tabla:
        fun borrarTablaSucursales(nombreTabla: String): String { return "DROP TABLE IF EXISTS `${nombreTabla}`;" }


    }

}
