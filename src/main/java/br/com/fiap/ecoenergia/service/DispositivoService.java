package br.com.fiap.ecoenergia.service;

import br.com.fiap.ecoenergia.dto.DispositivoRequest;
import br.com.fiap.ecoenergia.dto.DispositivoResponse;
import br.com.fiap.ecoenergia.entity.Dispositivo;
import br.com.fiap.ecoenergia.entity.Unidade;
import br.com.fiap.ecoenergia.repository.DispositivoRepository;
import br.com.fiap.ecoenergia.repository.UnidadeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Regras de negócio do cadastro de dispositivos
@Service
public class DispositivoService {

    private final DispositivoRepository dispositivoRepository;
    private final UnidadeRepository unidadeRepository;

    public DispositivoService(DispositivoRepository dispositivoRepository,
                              UnidadeRepository unidadeRepository) {
        this.dispositivoRepository = dispositivoRepository;
        this.unidadeRepository = unidadeRepository;
    }

    // Cadastra um novo dispositivo
    public DispositivoResponse criar(DispositivoRequest request) {
        Dispositivo dispositivo = new Dispositivo();
        preencher(dispositivo, request);
        return toResponse(dispositivoRepository.save(dispositivo));
    }

    // Lista todos os dispositivos
    public List<DispositivoResponse> listar() {
        return dispositivoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca um dispositivo pelo código
    public DispositivoResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    // Lista os dispositivos de uma unidade
    public List<DispositivoResponse> listarPorUnidade(Long cdUnidade) {
        buscarUnidade(cdUnidade);
        return dispositivoRepository.findByUnidadeCdUnidade(cdUnidade).stream()
                .map(this::toResponse)
                .toList();
    }

    // Atualiza os dados de um dispositivo existente
    public DispositivoResponse atualizar(Long id, DispositivoRequest request) {
        Dispositivo dispositivo = buscarEntidade(id);
        preencher(dispositivo, request);
        return toResponse(dispositivoRepository.save(dispositivo));
    }

    // Busca a entidade ou lança 404 se não existir
    private Dispositivo buscarEntidade(Long id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dispositivo não encontrado"));
    }

    // Busca a unidade ou lança 404 se não existir
    private Unidade buscarUnidade(Long cdUnidade) {
        return unidadeRepository.findById(cdUnidade)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Unidade não encontrada"));
    }

    // Copia os dados do request para a entidade
    private void preencher(Dispositivo dispositivo, DispositivoRequest request) {
        dispositivo.setUnidade(buscarUnidade(request.cdUnidade()));
        dispositivo.setNmDispositivo(request.nmDispositivo());
        dispositivo.setDsTipoDispositivo(request.dsTipoDispositivo());
        dispositivo.setVlLimiteKwhHora(request.vlLimiteKwhHora());
        dispositivo.setDtInstalacao(request.dtInstalacao().atStartOfDay());

        // Se a situação não vier, mantém a atual (padrão "O" em um dispositivo novo)
        if (request.stDispositivo() != null) {
            dispositivo.setStDispositivo(request.stDispositivo());
        }
    }

    // Converte a entidade no response devolvido pela API
    private DispositivoResponse toResponse(Dispositivo dispositivo) {
        return new DispositivoResponse(
                dispositivo.getCdDispositivo(),
                dispositivo.getUnidade().getCdUnidade(),
                dispositivo.getNmDispositivo(),
                dispositivo.getDsTipoDispositivo(),
                dispositivo.getVlLimiteKwhHora(),
                dispositivo.getStDispositivo(),
                dispositivo.getDtInstalacao().toLocalDate()
        );
    }
}