package br.com.fiap.ecoenergia.service;

import br.com.fiap.ecoenergia.dto.MetaRequest;
import br.com.fiap.ecoenergia.dto.MetaResponse;
import br.com.fiap.ecoenergia.entity.MetaEnergia;
import br.com.fiap.ecoenergia.entity.Unidade;
import br.com.fiap.ecoenergia.repository.MetaEnergiaRepository;
import br.com.fiap.ecoenergia.repository.UnidadeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Regras de negócio das metas mensais de energia
@Service
public class MetaService {

    private final MetaEnergiaRepository metaRepository;
    private final UnidadeRepository unidadeRepository;

    public MetaService(MetaEnergiaRepository metaRepository,
                       UnidadeRepository unidadeRepository) {
        this.metaRepository = metaRepository;
        this.unidadeRepository = unidadeRepository;
    }

    // Cadastra uma nova meta (acumulado e situação são preenchidos pela entidade)
    public MetaResponse criar(MetaRequest request) {
        Unidade unidade = buscarUnidade(request.cdUnidade());

        // Só pode existir uma meta por unidade, mês e ano
        if (metaRepository.existsByUnidadeCdUnidadeAndNrMesAndNrAno(
                request.cdUnidade(), request.nrMes(), request.nrAno())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Já existe meta para esta unidade neste mês e ano");
        }

        MetaEnergia meta = new MetaEnergia();
        meta.setUnidade(unidade);
        meta.setNrMes(request.nrMes());
        meta.setNrAno(request.nrAno());
        meta.setVlMetaMaxKwh(request.vlMetaMaxKwh());
        return toResponse(metaRepository.save(meta));
    }

    // Lista as metas de uma unidade
    public List<MetaResponse> listarPorUnidade(Long cdUnidade) {
        buscarUnidade(cdUnidade);
        return metaRepository.findByUnidadeCdUnidade(cdUnidade).stream()
                .map(this::toResponse)
                .toList();
    }

    // Atualiza o valor máximo da meta (unidade, mês e ano não mudam)
    public MetaResponse atualizar(Long id, MetaRequest request) {
        MetaEnergia meta = buscarEntidade(id);
        meta.setVlMetaMaxKwh(request.vlMetaMaxKwh());
        return toResponse(metaRepository.save(meta));
    }

    // Busca a meta ou lança 404 se não existir
    private MetaEnergia buscarEntidade(Long id) {
        return metaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Meta não encontrada"));
    }

    // Busca a unidade ou lança 404 se não existir
    private Unidade buscarUnidade(Long cdUnidade) {
        return unidadeRepository.findById(cdUnidade)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Unidade não encontrada"));
    }

    // Converte a entidade no response devolvido pela API
    private MetaResponse toResponse(MetaEnergia meta) {
        return new MetaResponse(
                meta.getCdMeta(),
                meta.getUnidade().getCdUnidade(),
                meta.getNrMes(),
                meta.getNrAno(),
                meta.getVlMetaMaxKwh(),
                meta.getVlKwhAcumulado(),
                meta.getStMeta()
        );
    }
}
