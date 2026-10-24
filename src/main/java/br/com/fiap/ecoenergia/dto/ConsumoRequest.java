package br.com.fiap.ecoenergia.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumoRequest(
        @NotNull
        Long cdDispositivo,

        @NotNull
        @PastOrPresent
        LocalDate dataMedicao,

        @NotNull
        @PositiveOrZero
        BigDecimal kwhConsumido
) {}