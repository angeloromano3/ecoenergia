
package br.com.fiap.ecoenergia.repository;

import br.com.fiap.ecoenergia.entity.ConsumoDiario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumoDiarioRepository
        extends JpaRepository<ConsumoDiario, Long> {
}
