
package br.com.fiap.ecoenergia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TB_ESG_USUARIO")
public class Usuario {

    @Id
    @Column(
            name = "NM_USUARIO",
            nullable = false,
            length = 80
    )
    private String nmUsuario;

    @Column(
            name = "DS_SENHA_HASH",
            nullable = false,
            length = 255
    )
    private String dsSenhaHash;

    @Column(
            name = "ST_ATIVO",
            nullable = false,
            columnDefinition = "CHAR(1)"
    )
    private String stAtivo = "S";

    public Usuario() {
    }

    @PrePersist
    public void antesDeSalvar() {
        if (stAtivo == null) {
            stAtivo = "S";
        }
    }

    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    public String getDsSenhaHash() {
        return dsSenhaHash;
    }

    public void setDsSenhaHash(String dsSenhaHash) {
        this.dsSenhaHash = dsSenhaHash;
    }

    public String getStAtivo() {
        return stAtivo;
    }

    public void setStAtivo(String stAtivo) {
        this.stAtivo = stAtivo;
    }
}
