/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.entidade;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author junior
 */
@Entity
public class ReboqueTrans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rplaca;
    private String ruf;
    private String rrntc;
    @ManyToOne
    private Emissao emissao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReboqueTrans)) {
            return false;
        }
        ReboqueTrans other = (ReboqueTrans) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.comercial.entidade.ReboqueTrans[ id=" + id + " ]";
    }

    public String getRplaca() {
        return rplaca;
    }

    public void setRplaca(String rplaca) {
        this.rplaca = rplaca;
    }

    public String getRuf() {
        return ruf;
    }

    public void setRuf(String ruf) {
        this.ruf = ruf;
    }

    public String getRrntc() {
        return rrntc;
    }

    public void setRrntc(String rrntc) {
        this.rrntc = rrntc;
    }

    public Emissao getEmissao() {
        return emissao;
    }

    public void setEmissao(Emissao emissao) {
        this.emissao = emissao;
    }
    
    
    
}
