
package br.com.fiap.ecoenergia.repository;

import br.com.fiap.ecoenergia.entity.AlertaIncidente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaIncidenteRepository
        extends JpaRepository<AlertaIncidente, Long> {
}
