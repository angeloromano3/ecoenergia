package br.com.fiap.ecoenergia.service;

import br.com.fiap.ecoenergia.dto.IndicadoresResponse;
import br.com.fiap.ecoenergia.entity.MetaEnergia;
import br.com.fiap.ecoenergia.repository.AlertaIncidenteRepository;
import br.com.fiap.ecoenergia.repository.MetaEnergiaRepository;
import br.com.fiap.ecoenergia.repository.UnidadeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

// Cálculo dos indicadores de consumo de uma unidade
@Service
public class IndicadorService {

    private final UnidadeRepository unidadeRepository;
    private final MetaEnergiaRepository metaRepository;
    private final AlertaIncidenteRepository alertaRepository;

    public IndicadorService(UnidadeRepository unidadeRepository,
                            MetaEnergiaRepository metaRepository,
                            AlertaIncidenteRepository alertaRepository) {
        this.unidadeRepository = unidadeRepository;
        this.metaRepository = metaRepository;
        this.alertaRepository = alertaRepository;
    }

    // Calcula os indicadores do mês informado (se mês/ano não vierem, usa o mês e o ano atuais)
    public IndicadoresResponse calcular(Long cdUnidade, Integer mes, Integer ano) {
        if (mes != null && (mes < 1 || mes > 12)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O mês deve estar entre 1 e 12");
        }

        LocalDate hoje = LocalDate.now();
        int nrMes = (mes != null) ? mes : hoje.getMonthValue();
        int nrAno = (ano != null) ? ano : hoje.getYear();

        if (!unidadeRepository.existsById(cdUnidade)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unidade não encontrada");
        }

        MetaEnergia meta = metaRepository
                .findByUnidadeCdUnidadeAndNrMesAndNrAno(cdUnidade, nrMes, nrAno)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Meta não encontrada para o mês informado"));

        // Período do mês: do dia 1 às 00:00 até o último dia às 23:59:59
        YearMonth periodo = YearMonth.of(nrAno, nrMes);
        LocalDateTime inicio = periodo.atDay(1).atStartOfDay();
        LocalDateTime fim = periodo.atEndOfMonth().atTime(23, 59, 59);

        long qtdAlertas = alertaRepository
                .countByUnidadeCdUnidadeAndDtAlertaBetween(cdUnidade, inicio, fim);

        return new IndicadoresResponse(
                cdUnidade,
                nrMes,
                nrAno,
                meta.getVlMetaMaxKwh(),
                meta.getVlKwhAcumulado(),
                calcularPercentual(meta.getVlKwhAcumulado(), meta.getVlMetaMaxKwh()),
                qtdAlertas
        );
    }

    // Percentual atingido da meta, protegido contra divisão por zero
    private BigDecimal calcularPercentual(BigDecimal acumulado, BigDecimal metaMax) {
        if (metaMax.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return acumulado
                .multiply(BigDecimal.valueOf(100))
                .divide(metaMax, 2, RoundingMode.HALF_UP);
    }
}