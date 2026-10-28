
-- V1 - Criacao das tabelas do projeto EcoEnergia
-- Baseado no script original de PL/SQL do grupo

-- ============================================
-- 1. TABELA DE UNIDADES
-- ============================================

CREATE TABLE tb_esg_unidade (
                                cd_unidade      NUMBER(6) NOT NULL,
                                nm_unidade      VARCHAR2(100) NOT NULL,
                                sg_estado       CHAR(2) NOT NULL,
                                nm_cidade       VARCHAR2(60) NOT NULL,
                                ds_segmento     VARCHAR2(50) NOT NULL,
                                st_unidade      CHAR(1) DEFAULT 'A' NOT NULL,
                                dt_cadastro     DATE DEFAULT SYSDATE NOT NULL
);

ALTER TABLE tb_esg_unidade
    ADD CONSTRAINT pk_tb_esg_unidade
        PRIMARY KEY (cd_unidade);

ALTER TABLE tb_esg_unidade
    ADD CONSTRAINT ck_tb_esg_unidade_st
        CHECK (st_unidade IN ('A', 'I'));

COMMENT ON TABLE tb_esg_unidade IS
'Registra as filiais, fábricas e escritórios monitorados pela governança ESG.';

COMMENT ON COLUMN tb_esg_unidade.cd_unidade IS
'Código identificador único da unidade.';

COMMENT ON COLUMN tb_esg_unidade.nm_unidade IS
'Nome descritivo da unidade ou prédio corporativo.';

COMMENT ON COLUMN tb_esg_unidade.st_unidade IS
'Status da unidade: A para Ativo, I para Inativo.';


-- ============================================
-- 2. TABELA DE DISPOSITIVOS
-- ============================================

CREATE TABLE tb_esg_dispositivo (
                                    cd_dispositivo       NUMBER(8) NOT NULL,
                                    cd_unidade           NUMBER(6) NOT NULL,
                                    nm_dispositivo       VARCHAR2(80) NOT NULL,
                                    ds_tipo_dispositivo  VARCHAR2(40) NOT NULL,
                                    vl_limite_kwh_hora   NUMBER(10,2) NOT NULL,
                                    st_dispositivo       CHAR(1) DEFAULT 'O' NOT NULL,
                                    dt_instalacao        DATE NOT NULL
);

ALTER TABLE tb_esg_dispositivo
    ADD CONSTRAINT pk_tb_esg_dispositivo
        PRIMARY KEY (cd_dispositivo);

ALTER TABLE tb_esg_dispositivo
    ADD CONSTRAINT fk_dispositivo_unidade
        FOREIGN KEY (cd_unidade)
            REFERENCES tb_esg_unidade (cd_unidade);

ALTER TABLE tb_esg_dispositivo
    ADD CONSTRAINT ck_tb_esg_disp_st
        CHECK (st_dispositivo IN ('O', 'M', 'D'));

ALTER TABLE tb_esg_dispositivo
    ADD CONSTRAINT ck_tb_esg_disp_limite
        CHECK (vl_limite_kwh_hora > 0);

COMMENT ON TABLE tb_esg_dispositivo IS
'Dispositivos e medidores inteligentes instalados para telemetria energética.';

COMMENT ON COLUMN tb_esg_dispositivo.vl_limite_kwh_hora IS
'Consumo máximo seguro/esperado por hora em kWh antes de caracterizar sobrecarga.';

COMMENT ON COLUMN tb_esg_dispositivo.st_dispositivo IS
'Status: O=Operacional, M=Manutenção, D=Desligado.';


-- ============================================
-- 3. TABELA DE METAS DE ENERGIA
-- ============================================

CREATE TABLE tb_esg_meta_energia (
                                     cd_meta              NUMBER(8) NOT NULL,
                                     cd_unidade           NUMBER(6) NOT NULL,
                                     nr_mes               NUMBER(2) NOT NULL,
                                     nr_ano               NUMBER(4) NOT NULL,
                                     vl_meta_max_kwh      NUMBER(12,2) NOT NULL,
                                     vl_kwh_acumulado     NUMBER(12,2) DEFAULT 0 NOT NULL,
                                     st_meta              VARCHAR2(20) DEFAULT 'EM_ANDAMENTO' NOT NULL
);

ALTER TABLE tb_esg_meta_energia
    ADD CONSTRAINT pk_tb_esg_meta_energia
        PRIMARY KEY (cd_meta);

ALTER TABLE tb_esg_meta_energia
    ADD CONSTRAINT un_meta_unidade_mes_ano
        UNIQUE (cd_unidade, nr_mes, nr_ano);

ALTER TABLE tb_esg_meta_energia
    ADD CONSTRAINT fk_meta_unidade
        FOREIGN KEY (cd_unidade)
            REFERENCES tb_esg_unidade (cd_unidade);

ALTER TABLE tb_esg_meta_energia
    ADD CONSTRAINT ck_tb_esg_meta_mes
        CHECK (nr_mes BETWEEN 1 AND 12);

ALTER TABLE tb_esg_meta_energia
    ADD CONSTRAINT ck_tb_esg_meta_st
        CHECK (st_meta IN ('EM_ANDAMENTO', 'CUMPRIDA', 'EXCEDIDA'));

COMMENT ON TABLE tb_esg_meta_energia IS
'Metas mensais de teto de consumo sustentável alinhadas ao plano ESG.';


-- ============================================
-- 4. TABELA DE CONSUMO DIARIO
-- ============================================

CREATE TABLE tb_esg_consumo_diario (
                                       cd_consumo           NUMBER(10) NOT NULL,
                                       cd_dispositivo       NUMBER(8) NOT NULL,
                                       dt_medicao           DATE NOT NULL,
                                       vl_kwh_consumido     NUMBER(10,2) NOT NULL,
                                       vl_custo_estimado    NUMBER(10,2) NOT NULL,
                                       st_processado        CHAR(1) DEFAULT 'S' NOT NULL
);

ALTER TABLE tb_esg_consumo_diario
    ADD CONSTRAINT pk_tb_esg_consumo_diario
        PRIMARY KEY (cd_consumo);

ALTER TABLE tb_esg_consumo_diario
    ADD CONSTRAINT fk_consumo_dispositivo
        FOREIGN KEY (cd_dispositivo)
            REFERENCES tb_esg_dispositivo (cd_dispositivo);

ALTER TABLE tb_esg_consumo_diario
    ADD CONSTRAINT ck_tb_esg_consumo_proc
        CHECK (st_processado IN ('S', 'N'));

COMMENT ON TABLE tb_esg_consumo_diario IS
'Telemetria diária consolidada registrada pelos medidores de consumo.';


-- ============================================
-- 5. TABELA DE ALERTAS E INCIDENTES
-- ============================================

CREATE TABLE tb_esg_alerta_incidente (
                                         cd_alerta            NUMBER(10) NOT NULL,
                                         cd_unidade           NUMBER(6) NOT NULL,
                                         cd_dispositivo       NUMBER(8),
                                         dt_alerta            DATE DEFAULT SYSDATE NOT NULL,
                                         tp_severidade        VARCHAR2(15) NOT NULL,
                                         ds_motivo            VARCHAR2(400) NOT NULL,
                                         st_resolucao         CHAR(1) DEFAULT 'P' NOT NULL
);

ALTER TABLE tb_esg_alerta_incidente
    ADD CONSTRAINT pk_tb_esg_alerta
        PRIMARY KEY (cd_alerta);

ALTER TABLE tb_esg_alerta_incidente
    ADD CONSTRAINT fk_alerta_unidade
        FOREIGN KEY (cd_unidade)
            REFERENCES tb_esg_unidade (cd_unidade);

ALTER TABLE tb_esg_alerta_incidente
    ADD CONSTRAINT fk_alerta_dispositivo
        FOREIGN KEY (cd_dispositivo)
            REFERENCES tb_esg_dispositivo (cd_dispositivo);

ALTER TABLE tb_esg_alerta_incidente
    ADD CONSTRAINT ck_tb_esg_alerta_sev
        CHECK (tp_severidade IN ('BAIXA', 'MEDIA', 'ALTA', 'CRITICA'));

ALTER TABLE tb_esg_alerta_incidente
    ADD CONSTRAINT ck_tb_esg_alerta_res
        CHECK (st_resolucao IN ('P', 'R', 'C'));

COMMENT ON TABLE tb_esg_alerta_incidente IS
'Incidentes ambientais e desvios de consumo gravados automaticamente pelas automações PL/SQL.';
