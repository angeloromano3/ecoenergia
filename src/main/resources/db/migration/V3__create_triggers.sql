
-- ============================================
-- V3 - TRIGGERS DE AUTOMACAO ECOENERGIA
-- Projeto FIAP - Oracle / Flyway
-- ============================================

-- TRIGGER 1: DETECCAO DE PICOS DE CONSUMO

BEGIN
EXECUTE IMMEDIATE q'~
CREATE OR REPLACE TRIGGER trg_esg_verifica_pico_consumo
AFTER INSERT ON tb_esg_consumo_diario
FOR EACH ROW
DECLARE
    v_limite_kwh_hora   tb_esg_dispositivo.vl_limite_kwh_hora%TYPE;
    v_cd_unidade        tb_esg_dispositivo.cd_unidade%TYPE;
    v_nm_dispositivo    tb_esg_dispositivo.nm_dispositivo%TYPE;
    v_novo_alerta_id    NUMBER;
BEGIN
    SELECT cd_unidade, vl_limite_kwh_hora, nm_dispositivo
      INTO v_cd_unidade, v_limite_kwh_hora, v_nm_dispositivo
      FROM tb_esg_dispositivo
     WHERE cd_dispositivo = :NEW.cd_dispositivo;

    IF :NEW.vl_kwh_consumido > v_limite_kwh_hora THEN
        v_novo_alerta_id := sq_esg_alerta.NEXTVAL;

        INSERT INTO tb_esg_alerta_incidente (
            cd_alerta,
            cd_unidade,
            cd_dispositivo,
            dt_alerta,
            tp_severidade,
            ds_motivo,
            st_resolucao
        ) VALUES (
            v_novo_alerta_id,
            v_cd_unidade,
            :NEW.cd_dispositivo,
            SYSDATE,
            CASE
                WHEN :NEW.vl_kwh_consumido >
                     (v_limite_kwh_hora * 1.30)
                THEN 'CRITICA'
                ELSE 'ALTA'
            END,
            'Pico anômalo detectado em [' ||
            v_nm_dispositivo ||
            ']. Consumo medido: ' ||
            TO_CHAR(:NEW.vl_kwh_consumido, '999990.99') ||
            ' kWh superior ao limite seguro de ' ||
            TO_CHAR(v_limite_kwh_hora, '999990.99') ||
            ' kWh.',
            'P'
        );

        DBMS_OUTPUT.PUT_LINE(
            '>>> ALERTA ESG GERADO: Sobrecarga no dispositivo ' ||
            :NEW.cd_dispositivo
        );
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE(
            'Aviso: Dispositivo de telemetria não localizado.'
        );

    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(
            'Erro na execução da Trigger trg_esg_verifica_pico_consumo: ' ||
            SQLERRM
        );
END;
~';
END;
/

-- ============================================
-- TRIGGER 2: ATUALIZACAO DAS METAS MENSAIS
-- ============================================

BEGIN
EXECUTE IMMEDIATE q'~
CREATE OR REPLACE TRIGGER trg_esg_atualiza_meta_mensal
AFTER INSERT ON tb_esg_consumo_diario
FOR EACH ROW
DECLARE
    v_cd_unidade        tb_esg_dispositivo.cd_unidade%TYPE;
    v_mes_medicao       NUMBER(2);
    v_ano_medicao       NUMBER(4);
    v_meta_max          tb_esg_meta_energia.vl_meta_max_kwh%TYPE;
    v_novo_acumulado    tb_esg_meta_energia.vl_kwh_acumulado%TYPE;
    v_st_meta_atual     tb_esg_meta_energia.st_meta%TYPE;
    v_novo_alerta_id    NUMBER;
BEGIN
    SELECT cd_unidade
      INTO v_cd_unidade
      FROM tb_esg_dispositivo
     WHERE cd_dispositivo = :NEW.cd_dispositivo;

    v_mes_medicao := EXTRACT(MONTH FROM :NEW.dt_medicao);
    v_ano_medicao := EXTRACT(YEAR FROM :NEW.dt_medicao);

    UPDATE tb_esg_meta_energia
       SET vl_kwh_acumulado =
           vl_kwh_acumulado + :NEW.vl_kwh_consumido
     WHERE cd_unidade = v_cd_unidade
       AND nr_mes = v_mes_medicao
       AND nr_ano = v_ano_medicao;

    IF SQL%ROWCOUNT > 0 THEN

        SELECT vl_meta_max_kwh,
               vl_kwh_acumulado,
               st_meta
          INTO v_meta_max,
               v_novo_acumulado,
               v_st_meta_atual
          FROM tb_esg_meta_energia
         WHERE cd_unidade = v_cd_unidade
           AND nr_mes = v_mes_medicao
           AND nr_ano = v_ano_medicao;

        IF v_novo_acumulado > v_meta_max THEN

            UPDATE tb_esg_meta_energia
               SET st_meta = 'EXCEDIDA'
             WHERE cd_unidade = v_cd_unidade
               AND nr_mes = v_mes_medicao
               AND nr_ano = v_ano_medicao;

            v_novo_alerta_id := sq_esg_alerta.NEXTVAL;

            INSERT INTO tb_esg_alerta_incidente (
                cd_alerta,
                cd_unidade,
                cd_dispositivo,
                dt_alerta,
                tp_severidade,
                ds_motivo,
                st_resolucao
            ) VALUES (
                v_novo_alerta_id,
                v_cd_unidade,
                NULL,
                SYSDATE,
                'ALTA',
                'Teto de consumo ESG EXCEDIDO para a Unidade ' ||
                v_cd_unidade ||
                ' no mês ' ||
                v_mes_medicao || '/' || v_ano_medicao ||
                '. Consumo atual: ' ||
                TO_CHAR(v_novo_acumulado, '999990.99') ||
                ' kWh de ' ||
                TO_CHAR(v_meta_max, '999990.99') ||
                ' kWh previstos.',
                'P'
            );

            DBMS_OUTPUT.PUT_LINE(
                '>>> META ESG EXCEDIDA: Unidade ' ||
                v_cd_unidade
            );

        ELSIF v_novo_acumulado >= (v_meta_max * 0.85)
          AND v_st_meta_atual = 'EM_ANDAMENTO' THEN

            v_novo_alerta_id := sq_esg_alerta.NEXTVAL;

            INSERT INTO tb_esg_alerta_incidente (
                cd_alerta,
                cd_unidade,
                cd_dispositivo,
                dt_alerta,
                tp_severidade,
                ds_motivo,
                st_resolucao
            ) VALUES (
                v_novo_alerta_id,
                v_cd_unidade,
                NULL,
                SYSDATE,
                'MEDIA',
                'Alerta Preventivo: Unidade ' ||
                v_cd_unidade ||
                ' atingiu ' ||
                ROUND(
                    (v_novo_acumulado / v_meta_max) * 100,
                    1
                ) ||
                '% da sua cota sustentável do mês ' ||
                v_mes_medicao || '/' ||
                v_ano_medicao || '.',
                'P'
            );

            DBMS_OUTPUT.PUT_LINE(
                '>>> ALERTA PREVENTIVO ESG: Unidade ' ||
                v_cd_unidade ||
                ' atingiu 85% da meta.'
            );

        END IF;
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        NULL;

    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(
            'Erro na execução da Trigger trg_esg_atualiza_meta_mensal: ' ||
            SQLERRM
        );
END;
~';
END;
/

-- ============================================
-- TRIGGER 3: VALIDACAO DOS SENSORES
-- ============================================

BEGIN
EXECUTE IMMEDIATE q'~
CREATE OR REPLACE TRIGGER trg_esg_valida_sensor_consumo
BEFORE INSERT OR UPDATE ON tb_esg_consumo_diario
FOR EACH ROW
DECLARE
    v_st_dispositivo tb_esg_dispositivo.st_dispositivo%TYPE;
BEGIN
    IF :NEW.vl_kwh_consumido < 0 THEN
        RAISE_APPLICATION_ERROR(
            -20001,
            'Violação de Telemetria ESG: O valor de consumo em kWh não pode ser negativo (' ||
            :NEW.vl_kwh_consumido || ').'
        );
    END IF;

    SELECT st_dispositivo
      INTO v_st_dispositivo
      FROM tb_esg_dispositivo
     WHERE cd_dispositivo = :NEW.cd_dispositivo;

    IF :NEW.vl_kwh_consumido = 0
       AND v_st_dispositivo = 'O' THEN

        UPDATE tb_esg_dispositivo
           SET st_dispositivo = 'M'
         WHERE cd_dispositivo = :NEW.cd_dispositivo;

        DBMS_OUTPUT.PUT_LINE(
            '>>> Sensor ' ||
            :NEW.cd_dispositivo ||
            ' sem telemetria: alterado automaticamente para MANUTENÇÃO (M).'
        );
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(
            -20002,
            'Dispositivo de telemetria ' ||
            :NEW.cd_dispositivo ||
            ' inexistente no cadastro ESG.'
        );
END;
~';
END;
/

-- ============================================
-- TRIGGER 4: GOVERNANCA E AUDITORIA ESG
-- ============================================

BEGIN
EXECUTE IMMEDIATE q'~
CREATE OR REPLACE TRIGGER trg_esg_governanca_auditoria
BEFORE UPDATE ON tb_esg_meta_energia
FOR EACH ROW
DECLARE
    v_mes_atual NUMBER(2);
    v_ano_atual NUMBER(4);
    v_novo_alerta_id NUMBER;
BEGIN
    v_mes_atual := EXTRACT(MONTH FROM SYSDATE);
    v_ano_atual := EXTRACT(YEAR FROM SYSDATE);

    IF (:OLD.nr_ano < v_ano_atual)
       OR (
           :OLD.nr_ano = v_ano_atual
           AND :OLD.nr_mes < v_mes_atual
       ) THEN

        IF :OLD.vl_meta_max_kwh !=
           :NEW.vl_meta_max_kwh THEN

            RAISE_APPLICATION_ERROR(
                -20003,
                'Violação de Governança e Compliance ESG: Não é permitido adulterar metas de meses anteriores encerrados.'
            );
        END IF;
    END IF;

    IF :NEW.vl_meta_max_kwh >
       (:OLD.vl_meta_max_kwh * 1.25) THEN

        v_novo_alerta_id := sq_esg_alerta.NEXTVAL;

        INSERT INTO tb_esg_alerta_incidente (
            cd_alerta,
            cd_unidade,
            cd_dispositivo,
            dt_alerta,
            tp_severidade,
            ds_motivo,
            st_resolucao
        ) VALUES (
            v_novo_alerta_id,
            :NEW.cd_unidade,
            NULL,
            SYSDATE,
            'MEDIA',
            'Auditoria de Governança: Meta da Unidade ' ||
            :NEW.cd_unidade ||
            ' no mês ' ||
            :NEW.nr_mes || '/' || :NEW.nr_ano ||
            ' sofreu aumento atípico de ' ||
            TO_CHAR(
                :OLD.vl_meta_max_kwh,
                '999990.99'
            ) ||
            ' kWh para ' ||
            TO_CHAR(
                :NEW.vl_meta_max_kwh,
                '999990.99'
            ) ||
            ' kWh.',
            'P'
        );

        DBMS_OUTPUT.PUT_LINE(
            '>>> AUDITORIA ESG: Registro de alteração atípica de meta gravado em Alertas.'
        );
    END IF;
END;
~';
END;
/
