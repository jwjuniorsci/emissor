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
 * @author JW Junior
 */
@Entity
public class ParametrosCte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    @ManyToOne
    private EmitenteCte emitente;
    private String senhaCert;
    private String caminhoCert;
    private String serie57;
    private String idToken;
    private String tipoEmi;
    private String csc;
    private Integer numNota57;
    private Integer nevento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EmitenteCte getEmitente() {
        return emitente;
    }

    public void setEmitente(EmitenteCte emitente) {
        this.emitente = emitente;
    }

    

    public String getSenhaCert() {
        return senhaCert;
    }

    public void setSenhaCert(String senhaCert) {
        this.senhaCert = senhaCert;
    }

    

    

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getCsc() {
        return csc;
    }

    public void setCsc(String csc) {
        this.csc = csc;
    }

    public String getCaminhoCert() {
        return caminhoCert;
    }

    public void setCaminhoCert(String caminhoCert) {
        this.caminhoCert = caminhoCert;
    }

    public String getTipoEmi() {
        return tipoEmi;
    }

    public void setTipoEmi(String tipoEmi) {
        this.tipoEmi = tipoEmi;
    }

    public Integer getNevento() {
        return nevento;
    }

    public void setNevento(Integer nevento) {
        this.nevento = nevento;
    }

    public String getSerie57() {
        return serie57;
    }

    public void setSerie57(String serie57) {
        this.serie57 = serie57;
    }

    public Integer getNumNota57() {
        return numNota57;
    }

    public void setNumNota57(Integer numNota57) {
        this.numNota57 = numNota57;
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
        if (!(object instanceof ParametrosCte)) {
            return false;
        }
        ParametrosCte other = (ParametrosCte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
    
}
