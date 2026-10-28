
package br.com.fiap.ecoenergia.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ESG_DISPOSITIVO")
public class Dispositivo {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "dispositivo_seq_generator"
    )
    @SequenceGenerator(
            name = "dispositivo_seq_generator",
            sequenceName = "SQ_ESG_DISPOSITIVO",
            allocationSize = 1
    )
    @Column(
            name = "CD_DISPOSITIVO",
            nullable = false,
            precision = 8
    )
    private Long cdDispositivo;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "CD_UNIDADE",
            nullable = false,
            referencedColumnName = "CD_UNIDADE"
    )
    private Unidade unidade;

    @Column(
            name = "NM_DISPOSITIVO",
            nullable = false,
            length = 80
    )
    private String nmDispositivo;

    @Column(
            name = "DS_TIPO_DISPOSITIVO",
            nullable = false,
            length = 40
    )
    private String dsTipoDispositivo;

    @Column(
            name = "VL_LIMITE_KWH_HORA",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal vlLimiteKwhHora;

    @Column(
            name = "ST_DISPOSITIVO",
            nullable = false,
            columnDefinition = "CHAR(1)"
    )
    private String stDispositivo = "O";

    @Column(
            name = "DT_INSTALACAO",
            nullable = false,
            columnDefinition = "DATE"
    )
    private LocalDateTime dtInstalacao;

    public Dispositivo() {
    }

    public Long getCdDispositivo() {
        return cdDispositivo;
    }

    public void setCdDispositivo(Long cdDispositivo) {
        this.cdDispositivo = cdDispositivo;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public String getNmDispositivo() {
        return nmDispositivo;
    }

    public void setNmDispositivo(String nmDispositivo) {
        this.nmDispositivo = nmDispositivo;
    }

    public String getDsTipoDispositivo() {
        return dsTipoDispositivo;
    }

    public void setDsTipoDispositivo(String dsTipoDispositivo) {
        this.dsTipoDispositivo = dsTipoDispositivo;
    }

    public BigDecimal getVlLimiteKwhHora() {
        return vlLimiteKwhHora;
    }

    public void setVlLimiteKwhHora(
            BigDecimal vlLimiteKwhHora
    ) {
        this.vlLimiteKwhHora = vlLimiteKwhHora;
    }

    public String getStDispositivo() {
        return stDispositivo;
    }

    public void setStDispositivo(String stDispositivo) {
        this.stDispositivo = stDispositivo;
    }

    public LocalDateTime getDtInstalacao() {
        return dtInstalacao;
    }

    public void setDtInstalacao(
            LocalDateTime dtInstalacao
    ) {
        this.dtInstalacao = dtInstalacao;
    }
}
