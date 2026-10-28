package br.com.fiap.ecoenergia.controller;

import br.com.fiap.ecoenergia.dto.IndicadoresResponse;
import br.com.fiap.ecoenergia.service.IndicadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Endpoint dos indicadores de consumo de uma unidade
@RestController
public class IndicadorController {

    private final IndicadorService indicadorService;

    public IndicadorController(IndicadorService indicadorService) {
        this.indicadorService = indicadorService;
    }

    // Consulta os indicadores do mês (se mês e ano não vierem, usa o mês e o ano atuais)
    @GetMapping("/unidades/{id}/indicadores")
    public ResponseEntity<IndicadoresResponse> consultar(
            @PathVariable Long id,
            @RequestParam(name = "mes", required = false) Integer mes,
            @RequestParam(name = "ano", required = false) Integer ano) {
        return ResponseEntity.ok(indicadorService.calcular(id, mes, ano));
    }
}