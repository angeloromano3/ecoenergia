
-- ============================================
-- V4 - INSERCAO DOS DADOS DE TESTE
-- Projeto EcoEnergia - FIAP
-- ============================================

-- Os consumos sao inseridos antes das metas
-- para preservar os acumulados originais.
-- As triggers do V3 ja estarao ativas.

-- ============================================
-- 1. UNIDADES (6 REGISTROS)
-- ============================================

INSERT INTO tb_esg_unidade (cd_unidade, nm_unidade, sg_estado, nm_cidade, ds_segmento, st_unidade, dt_cadastro)
VALUES (1, 'Sede Matriz Paulista', 'SP', 'São Paulo', 'Escritório Corporativo', 'A', TO_DATE('2024-01-10', 'YYYY-MM-DD'));

INSERT INTO tb_esg_unidade (cd_unidade, nm_unidade, sg_estado, nm_cidade, ds_segmento, st_unidade, dt_cadastro)
VALUES (2, 'Centro de Distribuição Cajamar', 'SP', 'Cajamar', 'Logística e Armazenagem', 'A', TO_DATE('2024-01-15', 'YYYY-MM-DD'));

INSERT INTO tb_esg_unidade (cd_unidade, nm_unidade, sg_estado, nm_cidade, ds_segmento, st_unidade, dt_cadastro)
VALUES (3, 'Complexo Industrial Betim', 'MG', 'Betim', 'Manufatura Pesada', 'A', TO_DATE('2024-02-01', 'YYYY-MM-DD'));

INSERT INTO tb_esg_unidade (cd_unidade, nm_unidade, sg_estado, nm_cidade, ds_segmento, st_unidade, dt_cadastro)
VALUES (4, 'Polo Tecnológico Campinas', 'SP', 'Campinas', 'Pesquisa e Datacenter', 'A', TO_DATE('2024-02-20', 'YYYY-MM-DD'));

INSERT INTO tb_esg_unidade (cd_unidade, nm_unidade, sg_estado, nm_cidade, ds_segmento, st_unidade, dt_cadastro)
VALUES (5, 'Filial Comercial Curitiba', 'PR', 'Curitiba', 'Vendas e Showroom', 'A', TO_DATE('2024-03-05', 'YYYY-MM-DD'));

INSERT INTO tb_esg_unidade (cd_unidade, nm_unidade, sg_estado, nm_cidade, ds_segmento, st_unidade, dt_cadastro)
VALUES (6, 'Fábrica de Componentes Joinville', 'SC', 'Joinville', 'Manufatura Metalmecânica', 'A', TO_DATE('2024-03-12', 'YYYY-MM-DD'));


-- ============================================
-- 2. DISPOSITIVOS (6 REGISTROS)
-- ============================================

INSERT INTO tb_esg_dispositivo (cd_dispositivo, cd_unidade, nm_dispositivo, ds_tipo_dispositivo, vl_limite_kwh_hora, st_dispositivo, dt_instalacao)
VALUES (101, 1, 'Medidor Geral Sede - Smart Meter A1', 'Medidor Trifásico Smart', 120.00, 'O', TO_DATE('2024-01-12', 'YYYY-MM-DD'));

INSERT INTO tb_esg_dispositivo (cd_dispositivo, cd_unidade, nm_dispositivo, ds_tipo_dispositivo, vl_limite_kwh_hora, st_dispositivo, dt_instalacao)
VALUES (102, 1, 'HVAC Central de Climatização Sede', 'Sistema Ar Central', 85.00, 'O', TO_DATE('2024-01-15', 'YYYY-MM-DD'));

INSERT INTO tb_esg_dispositivo (cd_dispositivo, cd_unidade, nm_dispositivo, ds_tipo_dispositivo, vl_limite_kwh_hora, st_dispositivo, dt_instalacao)
VALUES (201, 2, 'Câmaras Frigoríficas CD Cajamar', 'Refrigeração Industrial', 210.00, 'O', TO_DATE('2024-01-20', 'YYYY-MM-DD'));

INSERT INTO tb_esg_dispositivo (cd_dispositivo, cd_unidade, nm_dispositivo, ds_tipo_dispositivo, vl_limite_kwh_hora, st_dispositivo, dt_instalacao)
VALUES (301, 3, 'Linha de Prensas Automáticas Betim', 'Maquinário de Estamparia', 450.00, 'O', TO_DATE('2024-02-10', 'YYYY-MM-DD'));

INSERT INTO tb_esg_dispositivo (cd_dispositivo, cd_unidade, nm_dispositivo, ds_tipo_dispositivo, vl_limite_kwh_hora, st_dispositivo, dt_instalacao)
VALUES (401, 4, 'Servidores e Racks - Datacenter Campinas', 'Infraestrutura de TI', 160.00, 'O', TO_DATE('2024-02-25', 'YYYY-MM-DD'));

INSERT INTO tb_esg_dispositivo (cd_dispositivo, cd_unidade, nm_dispositivo, ds_tipo_dispositivo, vl_limite_kwh_hora, st_dispositivo, dt_instalacao)
VALUES (501, 5, 'Iluminação LED e Climatização Curitiba', 'Iluminação Comercial', 40.00, 'O', TO_DATE('2024-03-08', 'YYYY-MM-DD'));


-- ============================================
-- 3. CONSUMOS DIARIOS (6 REGISTROS)
-- ============================================

INSERT INTO tb_esg_consumo_diario (cd_consumo, cd_dispositivo, dt_medicao, vl_kwh_consumido, vl_custo_estimado, st_processado)
VALUES (1001, 101, TO_DATE('2026-10-01', 'YYYY-MM-DD'), 78.50, 58.87, 'S');

INSERT INTO tb_esg_consumo_diario (cd_consumo, cd_dispositivo, dt_medicao, vl_kwh_consumido, vl_custo_estimado, st_processado)
VALUES (1002, 102, TO_DATE('2026-10-01', 'YYYY-MM-DD'), 52.10, 39.07, 'S');

INSERT INTO tb_esg_consumo_diario (cd_consumo, cd_dispositivo, dt_medicao, vl_kwh_consumido, vl_custo_estimado, st_processado)
VALUES (1003, 201, TO_DATE('2026-10-01', 'YYYY-MM-DD'), 185.00, 138.75, 'S');

INSERT INTO tb_esg_consumo_diario (cd_consumo, cd_dispositivo, dt_medicao, vl_kwh_consumido, vl_custo_estimado, st_processado)
VALUES (1004, 301, TO_DATE('2026-10-01', 'YYYY-MM-DD'), 410.20, 307.65, 'S');

INSERT INTO tb_esg_consumo_diario (cd_consumo, cd_dispositivo, dt_medicao, vl_kwh_consumido, vl_custo_estimado, st_processado)
VALUES (1005, 401, TO_DATE('2026-10-01', 'YYYY-MM-DD'), 145.80, 109.35, 'S');

INSERT INTO tb_esg_consumo_diario (cd_consumo, cd_dispositivo, dt_medicao, vl_kwh_consumido, vl_custo_estimado, st_processado)
VALUES (1006, 501, TO_DATE('2026-10-01', 'YYYY-MM-DD'), 28.40, 21.30, 'S');


-- ============================================
-- 4. METAS MENSAIS (6 REGISTROS)
-- ============================================

INSERT INTO tb_esg_meta_energia (cd_meta, cd_unidade, nr_mes, nr_ano, vl_meta_max_kwh, vl_kwh_acumulado, st_meta)
VALUES (1, 1, 10, 2026, 45000.00, 12500.00, 'EM_ANDAMENTO');

INSERT INTO tb_esg_meta_energia (cd_meta, cd_unidade, nr_mes, nr_ano, vl_meta_max_kwh, vl_kwh_acumulado, st_meta)
VALUES (2, 2, 10, 2026, 75000.00, 24800.00, 'EM_ANDAMENTO');

INSERT INTO tb_esg_meta_energia (cd_meta, cd_unidade, nr_mes, nr_ano, vl_meta_max_kwh, vl_kwh_acumulado, st_meta)
VALUES (3, 3, 10, 2026, 160000.00, 89000.00, 'EM_ANDAMENTO');

INSERT INTO tb_esg_meta_energia (cd_meta, cd_unidade, nr_mes, nr_ano, vl_meta_max_kwh, vl_kwh_acumulado, st_meta)
VALUES (4, 4, 10, 2026, 60000.00, 18500.00, 'EM_ANDAMENTO');

INSERT INTO tb_esg_meta_energia (cd_meta, cd_unidade, nr_mes, nr_ano, vl_meta_max_kwh, vl_kwh_acumulado, st_meta)
VALUES (5, 5, 10, 2026, 15000.00, 4200.00, 'EM_ANDAMENTO');

INSERT INTO tb_esg_meta_energia (cd_meta, cd_unidade, nr_mes, nr_ano, vl_meta_max_kwh, vl_kwh_acumulado, st_meta)
VALUES (6, 1, 9, 2026, 45000.00, 41200.00, 'CUMPRIDA');


-- ============================================
-- 5. ALERTAS HISTORICOS (5 REGISTROS)
-- ============================================

INSERT INTO tb_esg_alerta_incidente (cd_alerta, cd_unidade, cd_dispositivo, dt_alerta, tp_severidade, ds_motivo, st_resolucao)
VALUES (1, 1, 102, TO_DATE('2026-09-15 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'MEDIA', 'Climatização operando acima da média durante horário de almoço.', 'R');

INSERT INTO tb_esg_alerta_incidente (cd_alerta, cd_unidade, cd_dispositivo, dt_alerta, tp_severidade, ds_motivo, st_resolucao)
VALUES (2, 3, 301, TO_DATE('2026-09-22 18:10:00', 'YYYY-MM-DD HH24:MI:SS'), 'CRITICA', 'Pico de consumo registrado 22% acima do teto estipulado para a linha de prensas.', 'R');

INSERT INTO tb_esg_alerta_incidente (cd_alerta, cd_unidade, cd_dispositivo, dt_alerta, tp_severidade, ds_motivo, st_resolucao)
VALUES (3, 2, 201, TO_DATE('2026-09-28 03:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'ALTA', 'Oscilação térmica com sobreconsumo nas câmaras frias no período noturno.', 'R');

INSERT INTO tb_esg_alerta_incidente (cd_alerta, cd_unidade, cd_dispositivo, dt_alerta, tp_severidade, ds_motivo, st_resolucao)
VALUES (4, 4, 401, TO_DATE('2026-10-02 11:20:00', 'YYYY-MM-DD HH24:MI:SS'), 'BAIXA', 'Consumo pontual elevado por migração de cargas de processamento.', 'P');

INSERT INTO tb_esg_alerta_incidente (cd_alerta, cd_unidade, cd_dispositivo, dt_alerta, tp_severidade, ds_motivo, st_resolucao)
VALUES (5, 1, NULL, TO_DATE('2026-10-03 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'MEDIA', 'Unidade atingiu 85% da meta sustentável de energia com 10 dias de antecedência.', 'P');
