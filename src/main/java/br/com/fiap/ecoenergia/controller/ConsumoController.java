package br.com.fiap.ecoenergia.controller;

import br.com.fiap.ecoenergia.dto.ConsumoRequest;
import br.com.fiap.ecoenergia.dto.ConsumoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumos")
public class ConsumoController {

    @PostMapping
    public ResponseEntity<ConsumoResponse> registrar(@RequestBody @Valid ConsumoRequest request) {
        // aqui vai chamar o service, quando as entidades estiverem prontas
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ConsumoResponse>> listarConsumos() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumoResponse> buscarConsumo(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsumoResponse> atualizarConsumo(@PathVariable Long id,
                                                            @RequestBody @Valid ConsumoRequest request) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}