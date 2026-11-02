package br.com.fiap.ecoenergia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ConsumoCadastroDto(
    @NotNull(message = "O ID do dispositivo é obrigatório.")
    Long dispositivoId,

    @NotNull(message = "O valor consumido é obrigatório.")
    @Positive(message = "O consumo deve ser um valor positivo.")
    BigDecimal valorConsumo,

    @NotBlank(message = "A unidade de medida não pode estar em branco.")
    @Size(max = 10, message = "A unidade deve ter no máximo 10 caracteres.")
    String unidadeMedida
) {}
