/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.envers.Audited;

/**
 *
 * @author ricardo
 */
@Entity
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    private String nome;
    private String unidade;
    private String codBarras;
    private String ncm;
    private String icmsCsosn;
    private String origem;
    private Float peso;
    private BigDecimal precoCompra;
    private BigDecimal precoVenda;
    private BigDecimal icmsPerc;
    private BigDecimal icmsPred;
    private String pisCst;
    private BigDecimal pisPerc;
    private String ipiCst;
    private BigDecimal ipiPerc;
    private String cofinsCst;
    private String cfop;
    private BigDecimal cofinsPerc;
    
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public BigDecimal getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(BigDecimal precoCompra) {
        this.precoCompra = precoCompra;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public BigDecimal getIcmsPerc() {
        return icmsPerc;
    }

    public void setIcmsPerc(BigDecimal icmsPerc) {
        this.icmsPerc = icmsPerc;
    }

    public BigDecimal getIcmsPred() {
        return icmsPred;
    }

    public void setIcmsPred(BigDecimal icmsPred) {
        this.icmsPred = icmsPred;
    }

    public String getPisCst() {
        return pisCst;
    }

    public void setPisCst(String pisCst) {
        this.pisCst = pisCst;
    }

    public BigDecimal getPisPerc() {
        return pisPerc;
    }

    public void setPisPerc(BigDecimal pisPerc) {
        this.pisPerc = pisPerc;
    }

    public String getIpiCst() {
        return ipiCst;
    }

    public void setIpiCst(String ipiCst) {
        this.ipiCst = ipiCst;
    }

    public BigDecimal getIpiPerc() {
        return ipiPerc;
    }

    public void setIpiPerc(BigDecimal ipiPerc) {
        this.ipiPerc = ipiPerc;
    }

    public String getCofinsCst() {
        return cofinsCst;
    }

    public void setCofinsCst(String cofinsCst) {
        this.cofinsCst = cofinsCst;
    }

    public BigDecimal getCofinsPerc() {
        return cofinsPerc;
    }

    public void setCofinsPerc(BigDecimal cofinsPerc) {
        this.cofinsPerc = cofinsPerc;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public String getIcmsCsosn() {
        return icmsCsosn;
    }

    public void setIcmsCsosn(String icmsCsosn) {
        this.icmsCsosn = icmsCsosn;
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
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
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
