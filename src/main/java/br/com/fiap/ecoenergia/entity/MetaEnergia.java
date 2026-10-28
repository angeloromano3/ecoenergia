
package br.com.fiap.ecoenergia.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "TB_ESG_META_ENERGIA",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UN_META_UNIDADE_MES_ANO",
                        columnNames = {
                                "CD_UNIDADE",
                                "NR_MES",
                                "NR_ANO"
                        }
                )
        }
)
public class MetaEnergia {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "meta_seq_generator"
    )
    @SequenceGenerator(
            name = "meta_seq_generator",
            sequenceName = "SQ_ESG_META",
            allocationSize = 1
    )
    @Column(
            name = "CD_META",
            nullable = false,
            precision = 8
    )
    private Long cdMeta;

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

    @Column(
            name = "NR_MES",
            nullable = false,
            precision = 2
    )
    private Integer nrMes;

    @Column(
            name = "NR_ANO",
            nullable = false,
            precision = 4
    )
    private Integer nrAno;

    @Column(
            name = "VL_META_MAX_KWH",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal vlMetaMaxKwh;

    @Column(
            name = "VL_KWH_ACUMULADO",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal vlKwhAcumulado = BigDecimal.ZERO;

    @Column(
            name = "ST_META",
            nullable = false,
            length = 20
    )
    private String stMeta = "EM_ANDAMENTO";

    public MetaEnergia() {
    }

    @PrePersist
    public void antesDeSalvar() {
        if (vlKwhAcumulado == null) {
            vlKwhAcumulado = BigDecimal.ZERO;
        }

        if (stMeta == null) {
            stMeta = "EM_ANDAMENTO";
        }
    }

    public Long getCdMeta() {
        return cdMeta;
    }

    public void setCdMeta(Long cdMeta) {
        this.cdMeta = cdMeta;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Integer getNrMes() {
        return nrMes;
    }

    public void setNrMes(Integer nrMes) {
        this.nrMes = nrMes;
    }

    public Integer getNrAno() {
        return nrAno;
    }

    public void setNrAno(Integer nrAno) {
        this.nrAno = nrAno;
    }

    public BigDecimal getVlMetaMaxKwh() {
        return vlMetaMaxKwh;
    }

    public void setVlMetaMaxKwh(
            BigDecimal vlMetaMaxKwh
    ) {
        this.vlMetaMaxKwh = vlMetaMaxKwh;
    }

    public BigDecimal getVlKwhAcumulado() {
        return vlKwhAcumulado;
    }

    public void setVlKwhAcumulado(
            BigDecimal vlKwhAcumulado
    ) {
        this.vlKwhAcumulado = vlKwhAcumulado;
    }

    public String getStMeta() {
        return stMeta;
    }

    public void setStMeta(String stMeta) {
        this.stMeta = stMeta;
    }
}
