-- =====================================================
-- Script para crear las secuencias necesarias en Oracle
-- Ejecutar este script en Oracle SQL Developer o similar
-- ANTES de reiniciar la aplicación Spring Boot
-- =====================================================

-- Secuencia para USUARIO
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE USUARIO_SEQ';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE USUARIO_SEQ
    START WITH 100
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/

-- Secuencia para CATEGORIA
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE CATEGORIA_SEQ';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE CATEGORIA_SEQ
    START WITH 100
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/

-- Secuencia para PRODUCTO
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE PRODUCTO_SEQ';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE PRODUCTO_SEQ
    START WITH 100
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/

-- Secuencia para BOLETA
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE BOLETA_SEQ';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE BOLETA_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/

-- Secuencia para DETALLE_BOLETA
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE DETALLE_BOLETA_SEQ';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE DETALLE_BOLETA_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/

-- =====================================================
-- Verificar que las secuencias fueron creadas
-- =====================================================
SELECT sequence_name, last_number 
FROM user_sequences 
WHERE sequence_name IN ('USUARIO_SEQ', 'CATEGORIA_SEQ', 'PRODUCTO_SEQ', 'BOLETA_SEQ', 'DETALLE_BOLETA_SEQ');

-- =====================================================
-- NOTA: Si ya tienes datos en las tablas, necesitas
-- ajustar el START WITH de cada secuencia al máximo ID + 1
-- Ejemplo para USUARIO:
-- SELECT MAX(USER_ID) FROM USUARIO;
-- Y luego ajustar START WITH al valor obtenido + 1
-- =====================================================

