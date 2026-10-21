package br.com.fiap.ecoenergia.controller;

import br.com.fiap.ecoenergia.dto.ConsumoRequest;
import br.com.fiap.ecoenergia.dto.ConsumoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controller responsável pelos endpoints de consumo de energia.
//Permite registrar, consultar, atualizar e excluir as medições diárias dos dispositivos.
@RestController
@RequestMapping("/consumos")
public class ConsumoController {

    //RegistraConsumo uma nova medição de consumo de energia de um dispositivo. O custo estimado é calculado pelo sistema.
    @PostMapping
    public ResponseEntity<ConsumoResponse> registrarConsumo(@RequestBody @Valid ConsumoRequest request) {

        //TODO aqui vai chamar o service, quando as entidades estiverem prontas
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Lista todas as medições de consumo de energia, em kWh, registradas.
    @GetMapping
    public ResponseEntity<List<ConsumoResponse>> listarConsumos() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Busca uma medição de consumo de energia em kWh, pelo código.
    @GetMapping("/{id}")
    public ResponseEntity<ConsumoResponse> buscarConsumo(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Atualiza os dados de uma medição de consumo existente.
    @PutMapping("/{id}")
    public ResponseEntity<ConsumoResponse> atualizarConsumo(
            @PathVariable Long id,
            @RequestBody @Valid ConsumoRequest request) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    // Exclui uma medição de consumo pelo código.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirConsumo(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}