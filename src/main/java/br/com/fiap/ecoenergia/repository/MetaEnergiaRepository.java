package br.com.fiap.ecoenergia.repository;

import br.com.fiap.ecoenergia.entity.MetaEnergia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetaEnergiaRepository
        extends JpaRepository<MetaEnergia, Long> {

    // Lista as metas de uma unidade
    List<MetaEnergia> findByUnidadeCdUnidade(Long cdUnidade);

    // Verifica se já existe meta para a unidade no mês e ano informados
    boolean existsByUnidadeCdUnidadeAndNrMesAndNrAno(Long cdUnidade, Integer nrMes, Integer nrAno);

    // Busca a meta da unidade no mês e ano informados
    Optional<MetaEnergia> findByUnidadeCdUnidadeAndNrMesAndNrAno(Long cdUnidade, Integer nrMes, Integer nrAno);
}