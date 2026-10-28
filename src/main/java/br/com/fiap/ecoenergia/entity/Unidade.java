
package br.com.fiap.ecoenergia.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ESG_UNIDADE")
public class Unidade {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "unidade_seq_generator"
    )
    @SequenceGenerator(
            name = "unidade_seq_generator",
            sequenceName = "SQ_ESG_UNIDADE",
            allocationSize = 1
    )
    @Column(name = "CD_UNIDADE", nullable = false, precision = 6)
    private Long cdUnidade;

    @Column(name = "NM_UNIDADE", nullable = false, length = 100)
    private String nmUnidade;

    @Column(
            name = "SG_ESTADO",
            nullable = false,
            columnDefinition = "CHAR(2)"
    )
    private String sgEstado;

    @Column(name = "NM_CIDADE", nullable = false, length = 60)
    private String nmCidade;

    @Column(name = "DS_SEGMENTO", nullable = false, length = 50)
    private String dsSegmento;

    @Column(
            name = "ST_UNIDADE",
            nullable = false,
            columnDefinition = "CHAR(1)"
    )
    private String stUnidade = "A";

    @Column(
            name = "DT_CADASTRO",
            nullable = false,
            columnDefinition = "DATE"
    )
    private LocalDateTime dtCadastro;

    public Unidade() {
    }

    @PrePersist
    public void antesDeSalvar() {
        if (stUnidade == null) {
            stUnidade = "A";
        }

        if (dtCadastro == null) {
            dtCadastro = LocalDateTime.now();
        }
    }

    public Long getCdUnidade() {
        return cdUnidade;
    }

    public void setCdUnidade(Long cdUnidade) {
        this.cdUnidade = cdUnidade;
    }

    public String getNmUnidade() {
        return nmUnidade;
    }

    public void setNmUnidade(String nmUnidade) {
        this.nmUnidade = nmUnidade;
    }

    public String getSgEstado() {
        return sgEstado;
    }

    public void setSgEstado(String sgEstado) {
        this.sgEstado = sgEstado;
    }

    public String getNmCidade() {
        return nmCidade;
    }

    public void setNmCidade(String nmCidade) {
        this.nmCidade = nmCidade;
    }

    public String getDsSegmento() {
        return dsSegmento;
    }

    public void setDsSegmento(String dsSegmento) {
        this.dsSegmento = dsSegmento;
    }

    public String getStUnidade() {
        return stUnidade;
    }

    public void setStUnidade(String stUnidade) {
        this.stUnidade = stUnidade;
    }

    public LocalDateTime getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDateTime dtCadastro) {
        this.dtCadastro = dtCadastro;
    }
}
