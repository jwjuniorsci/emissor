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
public class ParametrosFiscais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    @ManyToOne
    private Emitente emitente;
    private String senhaCert;
    private String caminhoCert;
    private String serie55;
    private String serie65;
    private String idToken;
    private String tipoEmi;
    private String csc;
    private Integer numNota65;
    private Integer numNota55;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Emitente getEmitente() {
        return emitente;
    }

    public void setEmitente(Emitente emitente) {
        this.emitente = emitente;
    }

    public String getSenhaCert() {
        return senhaCert;
    }

    public void setSenhaCert(String senhaCert) {
        this.senhaCert = senhaCert;
    }

    public String getSerie55() {
        return serie55;
    }

    public void setSerie55(String serie55) {
        this.serie55 = serie55;
    }

    public String getSerie65() {
        return serie65;
    }

    public void setSerie65(String serie65) {
        this.serie65 = serie65;
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

    public Integer getNumNota65() {
        return numNota65;
    }

    public void setNumNota65(Integer numNota65) {
        this.numNota65 = numNota65;
    }

    public Integer getNumNota55() {
        return numNota55;
    }

    public void setNumNota55(Integer numNota55) {
        this.numNota55 = numNota55;
    }

    public String getTipoEmi() {
        return tipoEmi;
    }

    public void setTipoEmi(String tipoEmi) {
        this.tipoEmi = tipoEmi;
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
        if (!(object instanceof ParametrosFiscais)) {
            return false;
        }
        ParametrosFiscais other = (ParametrosFiscais) object;
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
