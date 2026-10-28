package br.com.fiap.ecoenergia.repository;

import br.com.fiap.ecoenergia.entity.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {

    // Lista os dispositivos de uma unidade
    List<Dispositivo> findByUnidadeCdUnidade(Long cdUnidade);
}