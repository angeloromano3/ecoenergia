package br.com.fiap.ecoenergia.dto;

import java.time.LocalDateTime;

public record AlertaResponse(
        Long cdAlerta,
        Long cdUnidade,
        Long cdDispositivo,
        LocalDateTime dataAlerta,
        String tipoSeveridade,
        String descricaoMotivo,
        String statusResolucao
) {}
