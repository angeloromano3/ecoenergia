package br.com.fiap.ecoenergia.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumoResponse(
        Long cdConsumo,
        Long cdDispositivo,
        LocalDate dataMedicao,
        BigDecimal kwhConsumido,
        BigDecimal custoEstimado,
        String processado
) {}