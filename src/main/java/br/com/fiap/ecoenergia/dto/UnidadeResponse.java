package br.com.fiap.ecoenergia.dto;

import java.time.LocalDate;

// Dados devolvidos ao consultar uma unidade
public record UnidadeResponse(
        Long cdUnidade,
        String nmUnidade,
        String sgEstado,
        String nmCidade,
        String dsSegmento,
        String stUnidade,
        LocalDate dtCadastro
) {}