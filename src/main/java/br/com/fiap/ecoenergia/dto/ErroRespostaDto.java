package br.com.fiap.ecoenergia.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroRespostaDto(
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem,
    Map<String, String> camposInvalidos
) {
    public ErroRespostaDto(int status, String erro, String mensagem) {
        this(LocalDateTime.now(), status, erro, mensagem, null);
    }

    public ErroRespostaDto(int status, String erro, String mensagem, Map<String, String> camposInvalidos) {
        this(LocalDateTime.now(), status, erro, mensagem, camposInvalidos);
    }
}
