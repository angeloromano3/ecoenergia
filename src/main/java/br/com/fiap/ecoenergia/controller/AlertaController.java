package br.com.fiap.ecoenergia.controller;

import br.com.fiap.ecoenergia.dto.AlertaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controller responsável pelos endpoints de alertas.
// Permite consultar os alertas gerados automaticamente pelas triggers do banco e marcá-los como resolvidos.
@RestController
@RequestMapping("/alertas")
public class AlertaController {

    //Lista todos os alertas gerados pelo sistema.
    @GetMapping
    public ResponseEntity<List<AlertaResponse>> listarAlertas() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    //Busca um alerta pelo código.
    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponse> buscarAlerta(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Atualiza o status do alerta para resolvido,
    // indicando que o gestor já tratou o problema.
    // Os alertas são criados automaticamente pelas triggers do banco de dados.
    @PatchMapping("/{id}/resolver")
    public ResponseEntity<AlertaResponse> resolverAlerta(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
