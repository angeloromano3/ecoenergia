package br.com.fiap.ecoenergia.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Dados recebidos para cadastrar ou atualizar um dispositivo
public record DispositivoRequest(
        @NotNull
        Long cdUnidade,

        @NotBlank
        @Size(max = 80)
        String nmDispositivo,

        @NotBlank
        @Size(max = 40)
        String dsTipoDispositivo,

        @NotNull
        @Positive
        BigDecimal vlLimiteKwhHora,

        @Pattern(regexp = "[OMD]")
        String stDispositivo,

        @NotNull
        LocalDate dtInstalacao
) {}