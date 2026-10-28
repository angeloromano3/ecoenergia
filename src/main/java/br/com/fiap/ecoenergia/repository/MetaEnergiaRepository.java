
package br.com.fiap.ecoenergia.repository;

import br.com.fiap.ecoenergia.entity.MetaEnergia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetaEnergiaRepository
        extends JpaRepository<MetaEnergia, Long> {
}
