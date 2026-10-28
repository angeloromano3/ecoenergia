
package br.com.fiap.ecoenergia;

import br.com.fiap.ecoenergia.entity.ConsumoDiario;
import br.com.fiap.ecoenergia.entity.Dispositivo;
import br.com.fiap.ecoenergia.repository.*;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@EnabledIfEnvironmentVariable(named = "DB_URL", matches = ".+")
@EnabledIfEnvironmentVariable(named = "DB_USERNAME", matches = ".+")
@EnabledIfEnvironmentVariable(named = "DB_PASSWORD", matches = ".+")
class RepositoryGravacaoTest {


    @Autowired
    private ConsumoDiarioRepository consumoRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private MetaEnergiaRepository metaRepository;

    @Autowired
    private AlertaIncidenteRepository alertaRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void deveSalvarConsumoEAcionarTriggers() {

        Dispositivo dispositivo = dispositivoRepository
                .findById(101L)
                .orElseThrow();

        BigDecimal acumuladoAntes = metaRepository
                .findById(1L)
                .orElseThrow()
                .getVlKwhAcumulado();

        long alertasAntes = alertaRepository.count();

        ConsumoDiario consumo = new ConsumoDiario();
        consumo.setDispositivo(dispositivo);
        consumo.setDtMedicao(
                LocalDateTime.of(2026, 10, 27, 12, 0)
        );
        consumo.setVlKwhConsumido(
                new BigDecimal("175.00")
        );
        consumo.setVlCustoEstimado(
                new BigDecimal("120.00")
        );
        consumo.setStProcessado("S");

        ConsumoDiario salvo =
                consumoRepository.saveAndFlush(consumo);

        assertNotNull(salvo.getCdConsumo());

        // Atualiza a leitura para enxergar
        // as mudanças feitas pelas triggers.
        entityManager.clear();

        assertTrue(
                consumoRepository
                        .findById(salvo.getCdConsumo())
                        .isPresent()
        );

        long alertasDepois = alertaRepository.count();

        BigDecimal acumuladoDepois = metaRepository
                .findById(1L)
                .orElseThrow()
                .getVlKwhAcumulado();

        assertEquals(
                alertasAntes + 1,
                alertasDepois
        );

        assertEquals(
                0,
                acumuladoAntes
                        .add(new BigDecimal("175.00"))
                        .compareTo(acumuladoDepois)
        );

        System.out.println(
                "Consumo gravado pelo JPA: OK!"
        );
        System.out.println(
                "Alerta gerado pela trigger: OK!"
        );
        System.out.println(
                "Meta mensal atualizada: OK!"
        );
        System.out.println(
                "Teste transacional: rollback automatico."
        );
    }
}
