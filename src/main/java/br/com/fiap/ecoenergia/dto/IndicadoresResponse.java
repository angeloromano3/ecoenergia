package br.com.fiap.ecoenergia.dto;

import java.math.BigDecimal;

// Indicadores de consumo de uma unidade em um mês: consumo x meta, percentual e alertas
public record IndicadoresResponse(
        Long cdUnidade,
        Integer nrMes,
        Integer nrAno,
        BigDecimal vlMetaMaxKwh,
        BigDecimal vlKwhAcumulado,
        BigDecimal percentualAtingido,
        Long qtdAlertas
) {}