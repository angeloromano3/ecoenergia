package br.com.fiap.ecoenergia.service;

import br.com.fiap.ecoenergia.dto.ConsumoRequest;
import br.com.fiap.ecoenergia.dto.ConsumoResponse;
import br.com.fiap.ecoenergia.entity.ConsumoDiario;
import br.com.fiap.ecoenergia.entity.Dispositivo;
import br.com.fiap.ecoenergia.repository.ConsumoDiarioRepository;
import br.com.fiap.ecoenergia.repository.DispositivoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

// Service responsável pelas regras de negócio dos consumos de energia.
@Service
public class ConsumoService {

    private final ConsumoDiarioRepository consumoRepository;
    private final DispositivoRepository dispositivoRepository;

    // Tarifa lida do application.properties (R$ por kWh)
    @Value("${ecoenergia.tarifa-kwh}")
    private BigDecimal tarifaKwh;

    // O Spring entrega os repositórios prontos por este construtor.
    public ConsumoService(ConsumoDiarioRepository consumoRepository,
                          DispositivoRepository dispositivoRepository) {
        this.consumoRepository = consumoRepository;
        this.dispositivoRepository = dispositivoRepository;
    }

    // Registra uma nova medição de consumo
    public ConsumoResponse registrar(ConsumoRequest request) {
        ConsumoDiario consumo = new ConsumoDiario();
        preencherDados(consumo, request);
        return toResponse(consumoRepository.save(consumo));
    }

    // Lista todas as medições de consumo
    public List<ConsumoResponse> listar() {
        return consumoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca uma medição pelo código; se não existir, devolve 404
    public ConsumoResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    // Atualiza os dados de uma medição existente
    public ConsumoResponse atualizar(Long id, ConsumoRequest request) {
        ConsumoDiario consumo = buscarEntidade(id);
        preencherDados(consumo, request);
        return toResponse(consumoRepository.save(consumo));
    }

    // Exclui uma medição pelo código
    public void excluir(Long id) {
        consumoRepository.delete(buscarEntidade(id));
    }

    // Copia os dados do request para a entidade e calcula o custo
    private void preencherDados(ConsumoDiario consumo, ConsumoRequest request) {
        // Busca o dispositivo pelo código; se não existir, devolve 404
        Dispositivo dispositivo = dispositivoRepository.findById(request.cdDispositivo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dispositivo não encontrado"));

        consumo.setDispositivo(dispositivo);
        // Converte a data do request para data com hora (início do dia)
        consumo.setDtMedicao(request.dataMedicao().atStartOfDay());
        consumo.setVlKwhConsumido(request.kwhConsumido());
        // Custo = kWh consumidos x tarifa, arredondado para 2 casas (centavos)
        consumo.setVlCustoEstimado(
                request.kwhConsumido().multiply(tarifaKwh).setScale(2, RoundingMode.HALF_UP)
        );
    }

    // Busca a entidade ou lança 404, reaproveitado pelos métodos acima
    private ConsumoDiario buscarEntidade(Long id) {
        return consumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consumo não encontrado"));
    }

    // Converte a entidade em ConsumoResponse
    private ConsumoResponse toResponse(ConsumoDiario consumo) {
        return new ConsumoResponse(
                consumo.getCdConsumo(),
                consumo.getDispositivo().getCdDispositivo(),
                consumo.getDtMedicao().toLocalDate(),
                consumo.getVlKwhConsumido(),
                consumo.getVlCustoEstimado(),
                consumo.getStProcessado()
        );
    }
}