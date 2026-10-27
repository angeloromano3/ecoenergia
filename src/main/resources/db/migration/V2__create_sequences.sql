
-- ============================================
-- V2 - CRIACAO DAS SEQUENCES
-- Projeto EcoEnergia - FIAP
-- ============================================

-- 1. SEQUENCE DE UNIDADES
-- Nova sequence para os cadastros realizados pela API

CREATE SEQUENCE sq_esg_unidade
    START WITH 10000
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;


-- 2. SEQUENCE DE DISPOSITIVOS
-- Nova sequence para os equipamentos cadastrados

CREATE SEQUENCE sq_esg_dispositivo
    START WITH 10000
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;


-- 3. SEQUENCE DE METAS DE ENERGIA
-- Nova sequence para as metas mensais

CREATE SEQUENCE sq_esg_meta
    START WITH 10000
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;


-- 4. SEQUENCE DE CONSUMO DIARIO
-- Nova sequence para os registros de consumo

CREATE SEQUENCE sq_esg_consumo
    START WITH 10000
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;


-- 5. SEQUENCE DE ALERTAS
-- Sequence preservada do SQL original do grupo
-- Utilizada pelas triggers de automacao ESG

CREATE SEQUENCE sq_esg_alerta
    START WITH 100
    INCREMENT BY 1
    NOCACHE;
