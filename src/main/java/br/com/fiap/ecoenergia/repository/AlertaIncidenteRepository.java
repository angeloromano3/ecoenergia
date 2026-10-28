package br.com.fiap.ecoenergia.repository;

import br.com.fiap.ecoenergia.entity.AlertaIncidente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AlertaIncidenteRepository
        extends JpaRepository<AlertaIncidente, Long> {

    // Conta os alertas de uma unidade dentro de um período
    long countByUnidadeCdUnidadeAndDtAlertaBetween(Long cdUnidade, LocalDateTime inicio, LocalDateTime fim);
}