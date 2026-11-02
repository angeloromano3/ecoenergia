package br.com.fiap.ecoenergia.exception;

import br.com.fiap.ecoenergia.dto.ErroRespostaDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDto> tratarValidacaoCampos(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            erros.put(fe.getField(), fe.getDefaultMessage());
        }

        ErroRespostaDto resposta = new ErroRespostaDto(
            HttpStatus.BAD_REQUEST.value(),
            "Erro de Validação",
            "Existem campos inválidos na requisição.",
            erros
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDto> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroRespostaDto resposta = new ErroRespostaDto(
            HttpStatus.NOT_FOUND.value(),
            "Recurso Não Encontrado",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroRespostaDto> tratarErrosOracle(DataIntegrityViolationException ex) {
        String mensagem = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();

        if (mensagem != null) {
            if (mensagem.contains("ORA-20001")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErroRespostaDto(HttpStatus.BAD_REQUEST.value(), "Regra de Negócio Violada", "O valor do consumo não pode ser negativo.")
                );
            }

            if (mensagem.contains("ORA-20002")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErroRespostaDto(HttpStatus.NOT_FOUND.value(), "Não Encontrado", "Dispositivo informado não existe na base de dados.")
                );
            }

            if (mensagem.contains("ORA-20003")) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                    new ErroRespostaDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Entidade Não Processável", "Não é permitido alterar a meta de um mês que já foi encerrado.")
                );
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            new ErroRespostaDto(HttpStatus.CONFLICT.value(), "Conflito de Dados", "Ocorreu uma violação de integridade dos dados no banco.")
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDto> tratarErroInesperado(Exception ex) {
        ErroRespostaDto resposta = new ErroRespostaDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro Interno",
            "Ocorreu um erro inesperado no servidor: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }
}
