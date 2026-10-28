package br.com.fiap.ecoenergia.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

// Dados recebidos para cadastrar ou atualizar uma meta de energia
public record MetaRequest(
        @NotNull
        Long cdUnidade,

        @NotNull
        @Min(1)
        @Max(12)
        Integer nrMes,

        @NotNull
        @Min(2000)
        @Max(2100)
        Integer nrAno,

        @NotNull
        @Positive
        @Digits(integer = 10, fraction = 2)
        BigDecimal vlMetaMaxKwh
) {}