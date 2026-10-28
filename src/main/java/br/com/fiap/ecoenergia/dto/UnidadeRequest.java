package br.com.fiap.ecoenergia.dto;

import jakarta.validation.constraints.*;

// Dados recebidos para cadastrar ou atualizar uma unidade
public record UnidadeRequest(
        @NotBlank
        @Size(max = 100)
        String nmUnidade,

        @NotBlank
        @Size(min = 2, max = 2)
        String sgEstado,

        @NotBlank
        @Size(max = 60)
        String nmCidade,

        @NotBlank
        @Size(max = 50)
        String dsSegmento
) {}