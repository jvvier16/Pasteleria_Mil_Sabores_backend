INSERT INTO CATEGORIA (CATEGORIA_ID, NOMBRE, DESCRIPCION) 
SELECT 1, 'Tortas', 'Deliciosas tortas para toda ocasión' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM CATEGORIA WHERE CATEGORIA_ID = 1 OR NOMBRE = 'Tortas');

INSERT INTO CATEGORIA (CATEGORIA_ID, NOMBRE, DESCRIPCION) 
SELECT 2, 'Postres', 'Postres individuales y porciones' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM CATEGORIA WHERE CATEGORIA_ID = 2 OR NOMBRE = 'Postres');

INSERT INTO CATEGORIA (CATEGORIA_ID, NOMBRE, DESCRIPCION) 
SELECT 3, 'Sin Azúcar', 'Opciones saludables sin azúcar añadida' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM CATEGORIA WHERE CATEGORIA_ID = 3 OR NOMBRE = 'Sin Azúcar');

INSERT INTO CATEGORIA (CATEGORIA_ID, NOMBRE, DESCRIPCION) 
SELECT 4, 'Otros', 'Variedad de productos de pastelería' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM CATEGORIA WHERE CATEGORIA_ID = 4 OR NOMBRE = 'Otros');

INSERT INTO CATEGORIA (CATEGORIA_ID, NOMBRE, DESCRIPCION) 
SELECT 5, 'Sin Gluten', 'Productos aptos para celíacos' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM CATEGORIA WHERE CATEGORIA_ID = 5 OR NOMBRE = 'Sin Gluten');

INSERT INTO CATEGORIA (CATEGORIA_ID, NOMBRE, DESCRIPCION) 
SELECT 6, 'Veganas', 'Productos 100% veganos' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM CATEGORIA WHERE CATEGORIA_ID = 6 OR NOMBRE = 'Veganas');

INSERT INTO CATEGORIA (CATEGORIA_ID, NOMBRE, DESCRIPCION) 
SELECT 7, 'Especiales', 'Tortas para ocasiones especiales' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM CATEGORIA WHERE CATEGORIA_ID = 7 OR NOMBRE = 'Especiales');

COMMIT;

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Ana', 'García', 'ana.garcia@gmail.com', 'AnaGarcia1234', TO_DATE('1990-05-14', 'YYYY-MM-DD'), 'Ñuñoa, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Luis', 'Martínez', 'luis.martinez@gmail.com', 'LuisMartinez1', TO_DATE('1987-09-02', 'YYYY-MM-DD'), 'La Florida, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Sofía', 'López', 'sofia.lopez@gmail.com', 'SofiaLopez12', TO_DATE('1996-11-21', 'YYYY-MM-DD'), 'Providencia, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Carlos', 'Ruiz', 'carlos.ruiz@gmail.com', 'CarlosRuiz34', TO_DATE('1979-04-08', 'YYYY-MM-DD'), 'Maipú, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Valentina', 'Torres', 'valentina.torres@gmail.com', 'ValenTorres56', TO_DATE('1984-12-30', 'YYYY-MM-DD'), 'Las Condes, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Javier', 'Rojas', 'javier.rojas@gmail.com', 'JavierRojas13', TO_DATE('2005-11-14', 'YYYY-MM-DD'), 'Puente Alto, Santiago', NULL, 'tester', '../assets/img/segunda.jpeg', 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('admin', 'tienda', 'admin@gmail.com', 'JavierRojas12', TO_DATE('1990-01-01', 'YYYY-MM-DD'), 'Santiago, Chile', NULL, 'admin', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('aracelly', 'zenteno', 'aracelly.zenteno@gmail.com', 'Aracellyzen90', TO_DATE('2006-03-23', 'YYYY-MM-DD'), 'Puente Alto, Santiago', NULL, 'vendedor', '../assets/img/primera.jpeg', 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Matías', 'Jara', 'matias.jara@gmail.com', 'MatiasJara22', TO_DATE('2005-04-08', 'YYYY-MM-DD'), 'Puente Alto, Santiago', NULL, 'admin', '../assets/img/tecera.jpeg', 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Fernanda', 'Castillo', 'fernanda.castillo@gmail.com', 'FerCastillo45', TO_DATE('1978-02-17', 'YYYY-MM-DD'), 'Providencia, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Ricardo', 'Morales', 'ricardo.morales@gmail.com', 'RicaMora80', TO_DATE('1960-06-29', 'YYYY-MM-DD'), 'Maipú, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Daniela', 'Vega', 'daniela.vega@gmail.com', 'DaniVega19', TO_DATE('2003-03-15', 'YYYY-MM-DD'), 'Puente Alto, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Tomás', 'Aguilera', 'tomas.aguilera@gmail.com', 'TomasAgui07', TO_DATE('1985-09-01', 'YYYY-MM-DD'), 'Las Condes, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Beatriz', 'Fuentes', 'beatriz.fuentes@gmail.com', 'BeaFuent55', TO_DATE('1970-07-21', 'YYYY-MM-DD'), 'Recoleta, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Gabriel', 'Navarro', 'gabriel.navarro@gmail.com', 'GabiNava44', TO_DATE('1980-12-09', 'YYYY-MM-DD'), 'San Miguel, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Francisca', 'Silva', 'francisca.silva@gmail.com', 'FranSilva67', TO_DATE('1957-08-14', 'YYYY-MM-DD'), 'La Reina, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Ignacio', 'Vergara', 'ignacio.vergara@gmail.com', 'IgnacioVer03', TO_DATE('1999-10-30', 'YYYY-MM-DD'), 'Quilicura, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Paula', 'Mendoza', 'paula.mendoza@gmail.com', 'PaulaMend12', TO_DATE('1992-01-22', 'YYYY-MM-DD'), 'Independencia, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Héctor', 'Carrasco', 'hector.carrasco@gmail.com', 'HectorCar50', TO_DATE('1974-05-27', 'YYYY-MM-DD'), 'Peñalolén, Santiago', NULL, 'user', NULL, 1);

INSERT INTO USUARIO (NOMBRE, APELLIDO, CORREO, CONTRASENA, FECHA_NACIMIENTO, DIRECCION, TELEFONO, ROLE, IMAGEN, ACTIVO) 
VALUES ('Natalia', 'Ramos', 'natalia.ramos@gmail.com', 'NataRamos08', TO_DATE('1986-09-19', 'YYYY-MM-DD'), 'Santiago Centro, Santiago', NULL, 'user', NULL, 1);

COMMIT;

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (1, 'Torta de chocolate cuadrada', 45000, 10, '../assets/img/Torta Cuadrada de Chocolate.webp', 1, 'Deliciosa torta de chocolate con capas de ganache y un toque de avellanas.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (2, 'Torta Cuadrada de Frutas', 50000, 10, '../assets/img/Torta Cuadrada de Frutas.jpeg', 1, 'Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (3, 'torta circular de vainilla', 40000, 10, '../assets/img/torta circular de vainilla.jpeg', 1, 'Bizcocho de vainilla clásico relleno con crema pastelera y glaseado dulce.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (4, 'Torta Circular de manjar', 45000, 10, '../assets/img/Torta Circular de Manjar.jpeg', 1, 'Torta tradicional chilena con manjar y nueces, un deleite para los amantes de los sabores clásicos.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (5, 'Torta Circular de Frutas', 50000, 10, '../assets/img/Torta Circular de Frutas.jpeg', 1, 'Postre individual cremoso y suave, hecho con chocolate de alta calidad.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (6, 'Tiramisú Clásico', 30000, 10, '../assets/img/Tiramisú Clásico.jpeg', 2, 'Un postre italiano con capas de café, mascarpone y cacao.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (7, 'Torta Sin Azúcar de Naranja', 35000, 10, '../assets/img/Torta Sin Azúcar de Naranja.jpeg', 3, 'Torta ligera y deliciosa, endulzada naturalmente, ideal para quienes buscan opciones saludables.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (8, 'Cheesecake Sin Azúcar', 32000, 10, '../assets/img/Cheesecake Sin Azúcar.jpeg', 3, 'Un cheesecake suave y cremoso, endulzado naturalmente, perfecto para celebraciones.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (9, 'Empanada de Manzana', 1500, 10, '../assets/img/Empanada de Manzana.jpeg', 4, 'Pastelería tradicional rellena de manzanas especiadas, perfecta para desayuno o merienda.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (10, 'Tarta de Santiago', 2000, 10, '../assets/img/Tarta de Santiago.jpeg', 4, 'Tradicional tarta española hecha con almendras, azúcar y huevos.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (11, 'Brownie Sin Gluten', 2500, 10, '../assets/img/Brownie Sin Gluten.jpeg', 5, 'Brownie rico y denso, perfecto para quienes evitan el gluten sin perder sabor.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (12, 'Pan Sin Gluten', 3000, 10, '../assets/img/Pan Sin Gluten.jpeg', 5, 'Suave y esponjoso, ideal para sándwiches o acompañar cualquier comida.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (13, 'Torta Vegana de Chocolate', 40000, 10, '../assets/img/Torta Vegana de Chocolate.jpeg', 6, 'Torta húmeda y deliciosa, hecha sin productos de origen animal.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (14, 'Galletas Veganas de Avena', 1200, 10, '../assets/img/Galletas Veganas de Avena.jpeg', 6, 'Crujientes y sabrosas, una excelente opción para un snack saludable y vegano.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (15, 'Torta Especial de Cumpleaños', 55000, 10, '../assets/img/Torta Especial de Cumpleaños.jpeg', 7, 'Diseñada para celebraciones, personalizable con decoraciones y mensajes únicos.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (16, 'Torta Especial de Boda', 60000, 10, '../assets/img/Torta Especial de Boda.jpeg', 7, 'Elegante y deliciosa, pensada para ser el centro de atención en cualquier boda.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (17, 'Torta Tres Leches Tradicional', 38000, 10, '../assets/img/Torta Tres Leches Tradicional.jpeg', 1, 'Bizcocho húmedo bañado en mezcla de tres leches y cubierto con merengue suave.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (18, 'Torta Red Velvet', 42000, 10, '../assets/img/Torta Red Velvet.jpeg', 1, 'Bizcocho rojo aterciopelado con capas de crema de queso suavemente dulce.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (19, 'Pie de Limón', 18000, 10, '../assets/img/Pie de Limón.jpeg', 2, 'Base crujiente con relleno cítrico de limón y cobertura de merengue tostado.');

INSERT INTO PRODUCTO (PRODUCTO_ID, NOMBRE, PRECIO, STOCK, IMAGEN, CATEGORIA_CATEGORIA_ID, DESCRIPCION) 
VALUES (20, 'Roll de Canela', 1500, 10, '../assets/img/Roll de Canela.jpeg', 2, 'Roll suave y esponjoso con relleno de canela y glaseado dulce encima.');

COMMIT;

INSERT INTO BOLETA (FECHA, CLIENTE_USER_ID, TOTAL, ESTADO) 
SELECT TIMESTAMP '2025-11-08 10:00:00', USER_ID, 48000, 'pendiente'
FROM USUARIO WHERE CORREO = 'ana.garcia@gmail.com';

INSERT INTO BOLETA (FECHA, CLIENTE_USER_ID, TOTAL, ESTADO) 
SELECT TIMESTAMP '2025-11-08 09:30:00', USER_ID, 80000, 'procesado'
FROM USUARIO WHERE CORREO = 'luis.martinez@gmail.com';

INSERT INTO BOLETA (FECHA, CLIENTE_USER_ID, TOTAL, ESTADO) 
SELECT TIMESTAMP '2025-11-07 15:45:00', USER_ID, 30000, 'enviado'
FROM USUARIO WHERE CORREO = 'sofia.lopez@gmail.com';

INSERT INTO BOLETA (FECHA, CLIENTE_USER_ID, TOTAL, ESTADO) 
SELECT TIMESTAMP '2025-11-06 14:20:00', USER_ID, 45000, 'procesado'
FROM USUARIO WHERE CORREO = 'carlos.ruiz@gmail.com';

INSERT INTO BOLETA (FECHA, CLIENTE_USER_ID, TOTAL, ESTADO) 
SELECT TIMESTAMP '2025-11-05 11:15:00', USER_ID, 50000, 'enviado'
FROM USUARIO WHERE CORREO = 'valentina.torres@gmail.com';

INSERT INTO BOLETA (FECHA, CLIENTE_USER_ID, TOTAL, ESTADO) 
SELECT TIMESTAMP '2025-11-04 16:30:00', USER_ID, 30000, 'pendiente'
FROM USUARIO WHERE CORREO = 'ana.garcia@gmail.com';

INSERT INTO BOLETA (FECHA, CLIENTE_USER_ID, TOTAL, ESTADO) 
SELECT TIMESTAMP '2025-11-03 13:00:00', USER_ID, 35000, 'procesado'
FROM USUARIO WHERE CORREO = 'javier.rojas@gmail.com';

INSERT INTO BOLETA (FECHA, CLIENTE_USER_ID, TOTAL, ESTADO) 
SELECT TIMESTAMP '2025-11-02 10:00:00', USER_ID, 55000, 'enviado'
FROM USUARIO WHERE CORREO = 'admin@gmail.com';

COMMIT;

INSERT INTO DETALLE_BOLETA (PRODUCTO_PRODUCTO_ID, CANTIDAD, BOLETA_BOLETA_ID) 
SELECT 1, 1, BOLETA_ID
FROM BOLETA b
JOIN USUARIO u ON b.CLIENTE_USER_ID = u.USER_ID
WHERE u.CORREO = 'ana.garcia@gmail.com' AND b.FECHA = TIMESTAMP '2025-11-08 10:00:00' AND ROWNUM = 1;

INSERT INTO DETALLE_BOLETA (PRODUCTO_PRODUCTO_ID, CANTIDAD, BOLETA_BOLETA_ID) 
SELECT 9, 2, BOLETA_ID
FROM BOLETA b
JOIN USUARIO u ON b.CLIENTE_USER_ID = u.USER_ID
WHERE u.CORREO = 'ana.garcia@gmail.com' AND b.FECHA = TIMESTAMP '2025-11-08 10:00:00' AND ROWNUM = 1;

INSERT INTO DETALLE_BOLETA (PRODUCTO_PRODUCTO_ID, CANTIDAD, BOLETA_BOLETA_ID) 
SELECT 3, 2, BOLETA_ID
FROM BOLETA b
JOIN USUARIO u ON b.CLIENTE_USER_ID = u.USER_ID
WHERE u.CORREO = 'luis.martinez@gmail.com' AND b.FECHA = TIMESTAMP '2025-11-08 09:30:00' AND ROWNUM = 1;

INSERT INTO DETALLE_BOLETA (PRODUCTO_PRODUCTO_ID, CANTIDAD, BOLETA_BOLETA_ID) 
SELECT 6, 1, BOLETA_ID
FROM BOLETA b
JOIN USUARIO u ON b.CLIENTE_USER_ID = u.USER_ID
WHERE u.CORREO = 'sofia.lopez@gmail.com' AND b.FECHA = TIMESTAMP '2025-11-07 15:45:00' AND ROWNUM = 1;

INSERT INTO DETALLE_BOLETA (PRODUCTO_PRODUCTO_ID, CANTIDAD, BOLETA_BOLETA_ID) 
SELECT 4, 1, BOLETA_ID
FROM BOLETA b
JOIN USUARIO u ON b.CLIENTE_USER_ID = u.USER_ID
WHERE u.CORREO = 'carlos.ruiz@gmail.com' AND b.FECHA = TIMESTAMP '2025-11-06 14:20:00' AND ROWNUM = 1;

INSERT INTO DETALLE_BOLETA (PRODUCTO_PRODUCTO_ID, CANTIDAD, BOLETA_BOLETA_ID) 
SELECT 5, 1, BOLETA_ID
FROM BOLETA b
JOIN USUARIO u ON b.CLIENTE_USER_ID = u.USER_ID
WHERE u.CORREO = 'valentina.torres@gmail.com' AND b.FECHA = TIMESTAMP '2025-11-05 11:15:00' AND ROWNUM = 1;

INSERT INTO DETALLE_BOLETA (PRODUCTO_PRODUCTO_ID, CANTIDAD, BOLETA_BOLETA_ID) 
SELECT 6, 1, BOLETA_ID
FROM BOLETA b
JOIN USUARIO u ON b.CLIENTE_USER_ID = u.USER_ID
WHERE u.CORREO = 'ana.garcia@gmail.com' AND b.FECHA = TIMESTAMP '2025-11-04 16:30:00' AND ROWNUM = 1;

INSERT INTO DETALLE_BOLETA (PRODUCTO_PRODUCTO_ID, CANTIDAD, BOLETA_BOLETA_ID) 
SELECT 7, 1, BOLETA_ID
FROM BOLETA b
JOIN USUARIO u ON b.CLIENTE_USER_ID = u.USER_ID
WHERE u.CORREO = 'javier.rojas@gmail.com' AND b.FECHA = TIMESTAMP '2025-11-03 13:00:00' AND ROWNUM = 1;

INSERT INTO DETALLE_BOLETA (PRODUCTO_PRODUCTO_ID, CANTIDAD, BOLETA_BOLETA_ID) 
SELECT 15, 1, BOLETA_ID
FROM BOLETA b
JOIN USUARIO u ON b.CLIENTE_USER_ID = u.USER_ID
WHERE u.CORREO = 'admin@gmail.com' AND b.FECHA = TIMESTAMP '2025-11-02 10:00:00' AND ROWNUM = 1;

COMMIT;
