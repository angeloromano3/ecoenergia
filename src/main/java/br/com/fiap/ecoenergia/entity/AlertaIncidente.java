
package br.com.fiap.ecoenergia.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ESG_ALERTA_INCIDENTE")
public class AlertaIncidente {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "alerta_seq_generator"
    )
    @SequenceGenerator(
            name = "alerta_seq_generator",
            sequenceName = "SQ_ESG_ALERTA",
            allocationSize = 1
    )
    @Column(
            name = "CD_ALERTA",
            nullable = false,
            precision = 10
    )
    private Long cdAlerta;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "CD_UNIDADE",
            referencedColumnName = "CD_UNIDADE",
            nullable = false
    )
    private Unidade unidade;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "CD_DISPOSITIVO",
            referencedColumnName = "CD_DISPOSITIVO"
    )
    private Dispositivo dispositivo;

    @Column(
            name = "DT_ALERTA",
            nullable = false,
            columnDefinition = "DATE"
    )
    private LocalDateTime dtAlerta;

    @Column(
            name = "TP_SEVERIDADE",
            nullable = false,
            length = 15
    )
    private String tpSeveridade;

    @Column(
            name = "DS_MOTIVO",
            nullable = false,
            length = 400
    )
    private String dsMotivo;

    @Column(
            name = "ST_RESOLUCAO",
            nullable = false,
            columnDefinition = "CHAR(1)"
    )
    private String stResolucao = "P";

    public AlertaIncidente() {
    }

    @PrePersist
    public void antesDeSalvar() {
        if (dtAlerta == null) {
            dtAlerta = LocalDateTime.now();
        }

        if (stResolucao == null) {
            stResolucao = "P";
        }
    }

    public Long getCdAlerta() {
        return cdAlerta;
    }

    public void setCdAlerta(Long cdAlerta) {
        this.cdAlerta = cdAlerta;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public LocalDateTime getDtAlerta() {
        return dtAlerta;
    }

    public void setDtAlerta(LocalDateTime dtAlerta) {
        this.dtAlerta = dtAlerta;
    }

    public String getTpSeveridade() {
        return tpSeveridade;
    }

    public void setTpSeveridade(String tpSeveridade) {
        this.tpSeveridade = tpSeveridade;
    }

    public String getDsMotivo() {
        return dsMotivo;
    }

    public void setDsMotivo(String dsMotivo) {
        this.dsMotivo = dsMotivo;
    }

    public String getStResolucao() {
        return stResolucao;
    }

    public void setStResolucao(String stResolucao) {
        this.stResolucao = stResolucao;
    }
}
