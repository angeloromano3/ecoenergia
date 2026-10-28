package br.com.fiap.ecoenergia.controller;

import br.com.fiap.ecoenergia.dto.MetaRequest;
import br.com.fiap.ecoenergia.dto.MetaResponse;
import br.com.fiap.ecoenergia.service.MetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Endpoints das metas mensais de energia
@RestController
public class MetaController {

    private final MetaService metaService;

    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    // Cadastra uma nova meta
    @PostMapping("/metas")
    public ResponseEntity<MetaResponse> criar(@RequestBody @Valid MetaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metaService.criar(request));
    }

    // Lista as metas de uma unidade
    @GetMapping("/unidades/{id}/metas")
    public ResponseEntity<List<MetaResponse>> listarPorUnidade(@PathVariable Long id) {
        return ResponseEntity.ok(metaService.listarPorUnidade(id));
    }

    // Atualiza o valor máximo de uma meta existente
    @PutMapping("/metas/{id}")
    public ResponseEntity<MetaResponse> atualizar(@PathVariable Long id,
                                                  @RequestBody @Valid MetaRequest request) {
        return ResponseEntity.ok(metaService.atualizar(id, request));
    }
}