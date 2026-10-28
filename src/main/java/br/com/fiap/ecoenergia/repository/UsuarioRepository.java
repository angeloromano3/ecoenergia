
package br.com.fiap.ecoenergia.repository;

import br.com.fiap.ecoenergia.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository
        extends JpaRepository<Usuario, String> {
}
