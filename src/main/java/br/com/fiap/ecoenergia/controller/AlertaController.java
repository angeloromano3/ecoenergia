package br.com.fiap.ecoenergia.controller;

import br.com.fiap.ecoenergia.dto.AlertaResponse;
import br.com.fiap.ecoenergia.service.AlertaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controller responsável pelos endpoints de alertas.
// Permite consultar os alertas gerados automaticamente pelas triggers do banco e marcá-los como resolvidos.
@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    // O Spring entrega o service pronto por este construtor
    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    //Lista todos os alertas gerados pelo sistema.
    @GetMapping
    public ResponseEntity<List<AlertaResponse>> listarAlertas() {
        return ResponseEntity.status(HttpStatus.OK).body(alertaService.listar());
    }

    //Busca um alerta pelo código.
    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponse> buscarAlerta(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(alertaService.buscar(id));
    }

    // Atualiza o status do alerta para resolvido,
    // indicando que o gestor já tratou o problema.
    // Os alertas são criados automaticamente pelas triggers do banco de dados.
    @PatchMapping("/{id}/resolver")
    public ResponseEntity<AlertaResponse> resolverAlerta(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(alertaService.resolver(id));
    }
}