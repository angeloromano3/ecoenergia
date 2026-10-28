package br.com.fiap.ecoenergia.service;

import br.com.fiap.ecoenergia.dto.UnidadeRequest;
import br.com.fiap.ecoenergia.dto.UnidadeResponse;
import br.com.fiap.ecoenergia.entity.Unidade;
import br.com.fiap.ecoenergia.repository.UnidadeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Regras de negócio do cadastro de unidades
@Service
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;

    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    // Cadastra uma nova unidade (situação e data de cadastro são preenchidas pela entidade)
    public UnidadeResponse criar(UnidadeRequest request) {
        Unidade unidade = new Unidade();
        preencher(unidade, request);
        return toResponse(unidadeRepository.save(unidade));
    }

    // Lista todas as unidades
    public List<UnidadeResponse> listar() {
        return unidadeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca uma unidade pelo código
    public UnidadeResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    // Atualiza os dados de uma unidade existente
    public UnidadeResponse atualizar(Long id, UnidadeRequest request) {
        Unidade unidade = buscarEntidade(id);
        preencher(unidade, request);
        return toResponse(unidadeRepository.save(unidade));
    }

    // Exclui uma unidade pelo código
    public void excluir(Long id) {
        unidadeRepository.delete(buscarEntidade(id));
    }

    // Busca a entidade ou lança 404 se não existir
    private Unidade buscarEntidade(Long id) {
        return unidadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Unidade não encontrada"));
    }

    // Copia os dados do request para a entidade
    private void preencher(Unidade unidade, UnidadeRequest request) {
        unidade.setNmUnidade(request.nmUnidade());
        unidade.setSgEstado(request.sgEstado());
        unidade.setNmCidade(request.nmCidade());
        unidade.setDsSegmento(request.dsSegmento());
    }

    // Converte a entidade no response devolvido pela API
    private UnidadeResponse toResponse(Unidade unidade) {
        return new UnidadeResponse(
                unidade.getCdUnidade(),
                unidade.getNmUnidade(),
                unidade.getSgEstado(),
                unidade.getNmCidade(),
                unidade.getDsSegmento(),
                unidade.getStUnidade(),
                unidade.getDtCadastro().toLocalDate()
        );
    }
}