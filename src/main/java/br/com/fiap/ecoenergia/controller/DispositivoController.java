package br.com.fiap.ecoenergia.controller;

import br.com.fiap.ecoenergia.dto.DispositivoRequest;
import br.com.fiap.ecoenergia.dto.DispositivoResponse;
import br.com.fiap.ecoenergia.service.DispositivoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Endpoints do cadastro de dispositivos
@RestController
public class DispositivoController {

    private final DispositivoService dispositivoService;

    public DispositivoController(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    // Cadastra um novo dispositivo
    @PostMapping("/dispositivos")
    public ResponseEntity<DispositivoResponse> criar(@RequestBody @Valid DispositivoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dispositivoService.criar(request));
    }

    // Lista todos os dispositivos
    @GetMapping("/dispositivos")
    public ResponseEntity<List<DispositivoResponse>> listar() {
        return ResponseEntity.ok(dispositivoService.listar());
    }

    // Busca um dispositivo pelo código
    @GetMapping("/dispositivos/{id}")
    public ResponseEntity<DispositivoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(dispositivoService.buscar(id));
    }

    // Lista os dispositivos de uma unidade
    @GetMapping("/unidades/{id}/dispositivos")
    public ResponseEntity<List<DispositivoResponse>> listarPorUnidade(@PathVariable Long id) {
        return ResponseEntity.ok(dispositivoService.listarPorUnidade(id));
    }

    // Atualiza um dispositivo existente
    @PutMapping("/dispositivos/{id}")
    public ResponseEntity<DispositivoResponse> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid DispositivoRequest request) {
        return ResponseEntity.ok(dispositivoService.atualizar(id, request));
    }
}