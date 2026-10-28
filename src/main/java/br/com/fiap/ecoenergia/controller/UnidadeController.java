package br.com.fiap.ecoenergia.controller;

import br.com.fiap.ecoenergia.dto.UnidadeRequest;
import br.com.fiap.ecoenergia.dto.UnidadeResponse;
import br.com.fiap.ecoenergia.service.UnidadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Endpoints do cadastro de unidades
@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    // Cadastra uma nova unidade
    @PostMapping
    public ResponseEntity<UnidadeResponse> criar(@RequestBody @Valid UnidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeService.criar(request));
    }

    // Lista todas as unidades
    @GetMapping
    public ResponseEntity<List<UnidadeResponse>> listar() {
        return ResponseEntity.ok(unidadeService.listar());
    }

    // Busca uma unidade pelo código
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(unidadeService.buscar(id));
    }

    // Atualiza uma unidade existente
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponse> atualizar(@PathVariable Long id,
                                                     @RequestBody @Valid UnidadeRequest request) {
        return ResponseEntity.ok(unidadeService.atualizar(id, request));
    }

    // Exclui uma unidade
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        unidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}