
package br.com.fiap.ecoenergia.repository;

import br.com.fiap.ecoenergia.entity.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispositivoRepository
        extends JpaRepository<Dispositivo, Long> {
}
