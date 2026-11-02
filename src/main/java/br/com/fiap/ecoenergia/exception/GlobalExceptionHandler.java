package br.com.fiap.ecoenergia.exception;

import br.com.fiap.ecoenergia.dto.ErroRespostaDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

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

    // Erros lançados pelos services com ResponseStatusException (ex.: 404 "Consumo não encontrado")
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroRespostaDto> tratarResponseStatus(ResponseStatusException ex) {
        HttpStatusCode status = ex.getStatusCode();
        String erro = status.value() == 404 ? "Recurso Não Encontrado" : "Erro na Requisição";
        return ResponseEntity.status(status).body(
            new ErroRespostaDto(status.value(), erro, ex.getReason())
        );
    }

    // JSON mal formatado ou valor do tipo errado (ex.: texto onde deveria ser número)
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroRespostaDto> tratarRequisicaoInvalida(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErroRespostaDto(HttpStatus.BAD_REQUEST.value(), "Requisição Inválida",
                "O corpo ou os parâmetros da requisição estão em formato inválido.")
        );
    }

    // Qualquer erro de banco: primeiro verifica se veio de uma trigger (ORA-2000x)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErroRespostaDto> tratarErrosBanco(DataAccessException ex) {
        ResponseEntity<ErroRespostaDto> erroTrigger = traduzirErroTrigger(ex);
        if (erroTrigger != null) {
            return erroTrigger;
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            new ErroRespostaDto(HttpStatus.CONFLICT.value(), "Conflito de Dados", "Ocorreu uma violação de integridade dos dados no banco.")
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDto> tratarErroInesperado(Exception ex) {
        // Erro de trigger que chegou embrulhado em outra exceção (ex.: no commit da transação)
        ResponseEntity<ErroRespostaDto> erroTrigger = traduzirErroTrigger(ex);
        if (erroTrigger != null) {
            return erroTrigger;
        }

        // Erros do próprio Spring que já têm status (ex.: rota inexistente = 404, método errado = 405)
        if (ex instanceof ErrorResponse errorResponse) {
            HttpStatusCode status = errorResponse.getStatusCode();
            return ResponseEntity.status(status).body(
                new ErroRespostaDto(status.value(), "Erro na Requisição", ex.getMessage())
            );
        }

        // Mensagem genérica: não expõe detalhes internos do servidor
        ErroRespostaDto resposta = new ErroRespostaDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro Interno",
            "Ocorreu um erro inesperado no servidor."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }

    // Procura o código ORA das triggers em toda a cadeia de causas da exceção
    private ResponseEntity<ErroRespostaDto> traduzirErroTrigger(Throwable ex) {
        Throwable atual = ex;
        while (atual != null) {
            String mensagem = atual.getMessage();
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
            atual = atual.getCause();
        }
        return null;
    }
}
