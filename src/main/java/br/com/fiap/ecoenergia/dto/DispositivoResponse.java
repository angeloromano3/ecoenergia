package br.com.fiap.ecoenergia.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

// Dados devolvidos ao consultar um dispositivo
public record DispositivoResponse(
        Long cdDispositivo,
        Long cdUnidade,
        String nmDispositivo,
        String dsTipoDispositivo,
        BigDecimal vlLimiteKwhHora,
        String stDispositivo,
        LocalDate dtInstalacao
) {}