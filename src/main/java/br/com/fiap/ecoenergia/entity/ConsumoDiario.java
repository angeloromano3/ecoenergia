
package br.com.fiap.ecoenergia.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ESG_CONSUMO_DIARIO")
public class ConsumoDiario {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "consumo_seq_generator"
    )
    @SequenceGenerator(
            name = "consumo_seq_generator",
            sequenceName = "SQ_ESG_CONSUMO",
            allocationSize = 1
    )
    @Column(
            name = "CD_CONSUMO",
            nullable = false,
            precision = 10
    )
    private Long cdConsumo;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "CD_DISPOSITIVO",
            referencedColumnName = "CD_DISPOSITIVO",
            nullable = false
    )
    private Dispositivo dispositivo;

    @Column(
            name = "DT_MEDICAO",
            nullable = false,
            columnDefinition = "DATE"
    )
    private LocalDateTime dtMedicao;

    @Column(
            name = "VL_KWH_CONSUMIDO",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal vlKwhConsumido;

    @Column(
            name = "VL_CUSTO_ESTIMADO",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal vlCustoEstimado;

    @Column(
            name = "ST_PROCESSADO",
            nullable = false,
            columnDefinition = "CHAR(1)"
    )
    private String stProcessado = "S";

    public ConsumoDiario() {
    }

    @PrePersist
    public void antesDeSalvar() {
        if (stProcessado == null) {
            stProcessado = "S";
        }
    }

    public Long getCdConsumo() {
        return cdConsumo;
    }

    public void setCdConsumo(Long cdConsumo) {
        this.cdConsumo = cdConsumo;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public LocalDateTime getDtMedicao() {
        return dtMedicao;
    }

    public void setDtMedicao(LocalDateTime dtMedicao) {
        this.dtMedicao = dtMedicao;
    }

    public BigDecimal getVlKwhConsumido() {
        return vlKwhConsumido;
    }

    public void setVlKwhConsumido(
            BigDecimal vlKwhConsumido
    ) {
        this.vlKwhConsumido = vlKwhConsumido;
    }

    public BigDecimal getVlCustoEstimado() {
        return vlCustoEstimado;
    }

    public void setVlCustoEstimado(
            BigDecimal vlCustoEstimado
    ) {
        this.vlCustoEstimado = vlCustoEstimado;
    }

    public String getStProcessado() {
        return stProcessado;
    }

    public void setStProcessado(String stProcessado) {
        this.stProcessado = stProcessado;
    }
}
