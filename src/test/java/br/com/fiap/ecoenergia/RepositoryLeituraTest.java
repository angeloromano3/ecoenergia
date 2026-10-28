
package br.com.fiap.ecoenergia;

import br.com.fiap.ecoenergia.entity.Dispositivo;
import br.com.fiap.ecoenergia.repository.AlertaIncidenteRepository;
import br.com.fiap.ecoenergia.repository.ConsumoDiarioRepository;
import br.com.fiap.ecoenergia.repository.DispositivoRepository;
import br.com.fiap.ecoenergia.repository.MetaEnergiaRepository;
import br.com.fiap.ecoenergia.repository.UnidadeRepository;
import br.com.fiap.ecoenergia.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@EnabledIfEnvironmentVariable(named = "DB_URL", matches = ".+")
@EnabledIfEnvironmentVariable(named = "DB_USERNAME", matches = ".+")
@EnabledIfEnvironmentVariable(named = "DB_PASSWORD", matches = ".+")
class RepositoryLeituraTest {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private ConsumoDiarioRepository consumoDiarioRepository;

    @Autowired
    private MetaEnergiaRepository metaEnergiaRepository;

    @Autowired
    private AlertaIncidenteRepository alertaIncidenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @Transactional(readOnly = true)
    void deveConsultarDadosDoOracle() {

        long unidades = unidadeRepository.count();
        long dispositivos = dispositivoRepository.count();
        long consumos = consumoDiarioRepository.count();
        long metas = metaEnergiaRepository.count();
        long alertas = alertaIncidenteRepository.count();
        long usuarios = usuarioRepository.count();

        System.out.println("===== TESTE ECOENERGIA =====");
        System.out.println("Unidades: " + unidades);
        System.out.println("Dispositivos: " + dispositivos);
        System.out.println("Consumos: " + consumos);
        System.out.println("Metas: " + metas);
        System.out.println("Alertas: " + alertas);
        System.out.println("Usuarios: " + usuarios);

        assertTrue(unidades > 0);
        assertTrue(dispositivos > 0);
        assertTrue(consumos > 0);
        assertTrue(metas > 0);
        assertTrue(alertas > 0);

        assertTrue(unidadeRepository.findById(1L).isPresent());

        Dispositivo dispositivo = dispositivoRepository
                .findById(101L)
                .orElseThrow();

        assertEquals(
                1L,
                dispositivo.getUnidade().getCdUnidade()
        );

        System.out.println(
                "Repositories e relacionamento JPA: OK!"
        );
    }
}
