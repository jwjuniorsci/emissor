/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class ItemEmissao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Produto produto;
    private String cfop;
    private String icmsCst;
    private String ipiCst;
    private String icmsCsosn;
    private String pisCst;
    private String cofinsCst;
    private BigDecimal quant;
    private Integer icmsPorc;
    private Integer ipiPorc;
    private BigDecimal pisPorc;
    private BigDecimal cofinsPorc;
    private BigDecimal icmsRed;
    private Float peso;
    private BigDecimal vlrUnit;
    private BigDecimal vlrProd;
    private BigDecimal vlrDesconto;
    private BigDecimal vlrTotal;
    private BigDecimal icmsBc;
    private BigDecimal ipiBc;
    private BigDecimal pisBc;
    private BigDecimal cofinsBc;
    private BigDecimal icmsVlr;
    private BigDecimal ipiVlr;
    private BigDecimal pisVlr;
    private BigDecimal cofinsVlr;
    private BigDecimal itemQuant;
    @ManyToOne
    private Emissao emissao;
    @ManyToOne
    private Emissao65 emissao65;
    
    public BigDecimal getSubTotal() {
        return vlrProd.multiply(itemQuant);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public String getIcmsCst() {
        return icmsCst;
    }

    public void setIcmsCst(String icmsCst) {
        this.icmsCst = icmsCst;
    }

    public String getIpiCst() {
        return ipiCst;
    }

    public void setIpiCst(String ipiCst) {
        this.ipiCst = ipiCst;
    }

    public String getPisCst() {
        return pisCst;
    }

    public void setPisCst(String pisCst) {
        this.pisCst = pisCst;
    }

    public String getCofinsCst() {
        return cofinsCst;
    }

    public void setCofinsCst(String cofinsCst) {
        this.cofinsCst = cofinsCst;
    }

    public BigDecimal getQuant() {
        return quant;
    }

    public void setQuant(BigDecimal quant) {
        this.quant = quant;
    }

    

    public Integer getIcmsPorc() {
        return icmsPorc;
    }

    public void setIcmsPorc(Integer icmsPorc) {
        this.icmsPorc = icmsPorc;
    }

    public Integer getIpiPorc() {
        return ipiPorc;
    }

    public void setIpiPorc(Integer ipiPorc) {
        this.ipiPorc = ipiPorc;
    }

    public BigDecimal getPisPorc() {
        return pisPorc;
    }

    public void setPisPorc(BigDecimal pisPorc) {
        this.pisPorc = pisPorc;
    }

    public BigDecimal getCofinsPorc() {
        return cofinsPorc;
    }

    public void setCofinsPorc(BigDecimal cofinsPorc) {
        this.cofinsPorc = cofinsPorc;
    }

    public BigDecimal getIcmsRed() {
        return icmsRed;
    }

    public void setIcmsRed(BigDecimal icmsRed) {
        this.icmsRed = icmsRed;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public BigDecimal getVlrUnit() {
        return vlrUnit;
    }

    public void setVlrUnit(BigDecimal vlrUnit) {
        this.vlrUnit = vlrUnit;
    }

    public BigDecimal getVlrProd() {
        return vlrProd;
    }

    public void setVlrProd(BigDecimal vlrProd) {
        this.vlrProd = vlrProd;
    }

    public BigDecimal getVlrDesconto() {
        return vlrDesconto;
    }

    public void setVlrDesconto(BigDecimal vlrDesconto) {
        this.vlrDesconto = vlrDesconto;
    }

    public BigDecimal getVlrTotal() {
        return vlrTotal;
    }

    public void setVlrTotal(BigDecimal vlrTotal) {
        this.vlrTotal = vlrTotal;
    }

    public BigDecimal getIcmsBc() {
        return icmsBc;
    }

    public void setIcmsBc(BigDecimal icmsBc) {
        this.icmsBc = icmsBc;
    }

    public BigDecimal getIpiBc() {
        return ipiBc;
    }

    public void setIpiBc(BigDecimal ipiBc) {
        this.ipiBc = ipiBc;
    }

    public BigDecimal getPisBc() {
        return pisBc;
    }

    public void setPisBc(BigDecimal pisBc) {
        this.pisBc = pisBc;
    }

    public BigDecimal getCofinsBc() {
        return cofinsBc;
    }

    public void setCofinsBc(BigDecimal cofinsBc) {
        this.cofinsBc = cofinsBc;
    }

    public BigDecimal getIcmsVlr() {
        return icmsVlr;
    }

    public void setIcmsVlr(BigDecimal icmsVlr) {
        this.icmsVlr = icmsVlr;
    }

    public BigDecimal getIpiVlr() {
        return ipiVlr;
    }

    public void setIpiVlr(BigDecimal ipiVlr) {
        this.ipiVlr = ipiVlr;
    }

    public BigDecimal getPisVlr() {
        return pisVlr;
    }

    public void setPisVlr(BigDecimal pisVlr) {
        this.pisVlr = pisVlr;
    }

    public BigDecimal getCofinsVlr() {
        return cofinsVlr;
    }

    public void setCofinsVlr(BigDecimal cofinsVlr) {
        this.cofinsVlr = cofinsVlr;
    }

    public Emissao getEmissao() {
        return emissao;
    }

    public void setEmissao(Emissao emissao) {
        this.emissao = emissao;
    }

    public Emissao65 getEmissao65() {
        return emissao65;
    }

    public void setEmissao65(Emissao65 emissao65) {
        this.emissao65 = emissao65;
    }

    public BigDecimal getItemQuant() {
        return itemQuant;
    }

    public void setItemQuant(BigDecimal itemQuant) {
        this.itemQuant = itemQuant;
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
        if (!(object instanceof ItemEmissao)) {
            return false;
        }
        ItemEmissao other = (ItemEmissao) object;
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
