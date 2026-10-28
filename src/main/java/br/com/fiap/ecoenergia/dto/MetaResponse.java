package br.com.fiap.ecoenergia.dto;

import java.math.BigDecimal;

// Dados devolvidos ao consultar uma meta de energia
public record MetaResponse(
        Long cdMeta,
        Long cdUnidade,
        Integer nrMes,
        Integer nrAno,
        BigDecimal vlMetaMaxKwh,
        BigDecimal vlKwhAcumulado,
        String stMeta
) {}