package br.com.fiap.ecoenergia.service;

import br.com.fiap.ecoenergia.dto.AlertaResponse;
import br.com.fiap.ecoenergia.entity.AlertaIncidente;
import br.com.fiap.ecoenergia.repository.AlertaIncidenteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Service responsável pelas regras de negócio dos alertas.
// Os alertas são criados pelas triggers do banco; aqui só consultamos e resolvemos.
@Service
public class AlertaService {

    private final AlertaIncidenteRepository alertaRepository;

    // O Spring entrega o repositório pronto por este construtor
    public AlertaService(AlertaIncidenteRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    // Lista todos os alertas gerados pelo sistema
    public List<AlertaResponse> listar() {
        return alertaRepository.
                findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca um alerta pelo código; se não existir, devolve 404
    public AlertaResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    // Marca o alerta como resolvido (R = Resolvido)
    public AlertaResponse resolver(Long id) {
        AlertaIncidente alerta = buscarEntidade(id);
        alerta.setStResolucao("R");
        return toResponse(alertaRepository.save(alerta));
    }

    // Busca a entidade ou lança 404
    private AlertaIncidente buscarEntidade(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta não encontrado"));
    }

    // Converte a entidade em AlertaResponse
    private AlertaResponse toResponse(AlertaIncidente alerta) {
        // Alertas de meta não têm dispositivo, então o código pode ser nulo
        Long cdDispositivo = alerta.getDispositivo() != null
                ? alerta.getDispositivo().getCdDispositivo()
                : null;

        return new AlertaResponse(
                alerta.getCdAlerta(),
                alerta.getUnidade().getCdUnidade(),
                cdDispositivo,
                alerta.getDtAlerta(),
                alerta.getTpSeveridade(),
                alerta.getDsMotivo(),
                alerta.getStResolucao()
        );
    }
}
