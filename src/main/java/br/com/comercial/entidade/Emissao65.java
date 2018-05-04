/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author JW Junior
 */
@Entity
public class Emissao65 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //Natureza da operação(venda,compra e etc) 
    private String natOp;
    //Tipo da Nota 0 entrada 1 saida
    private String tpNf;
    private String chave;
    private String status;
    private String dadosAdd;
    private String caminho;
    private String protocolo;
    private String dataCanc;
    @ManyToOne
    private Emitente emitente;
    @ManyToOne
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "emissao65", fetch = FetchType.EAGER)
    private List<ItemEmissao> listaProd;
    private BigDecimal valorTotal;
    private BigDecimal valorDesconto;

    public Emissao65() {
        listaProd = new ArrayList<ItemEmissao>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNatOp() {
        return natOp;
    }

    public void setNatOp(String natOp) {
        this.natOp = natOp;
    }

    public String getTpNf() {
        return tpNf;
    }

    public void setTpNf(String tpNf) {
        this.tpNf = tpNf;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Emitente getEmitente() {
        return emitente;
    }

    public void setEmitente(Emitente emitente) {
        this.emitente = emitente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemEmissao> getListaProd() {
        return listaProd;
    }

    public void setListaProd(List<ItemEmissao> listaProd) {
        this.listaProd = listaProd;
    }

    public BigDecimal getValorTotal() {
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        for(ItemEmissao ie : listaProd) {
            total = total.add(ie.getSubTotal());
        }
        valorTotal=total;
        if(valorDesconto == null){
            valorDesconto = new BigDecimal(BigInteger.ZERO);
        }
        valorTotal = total.subtract(valorDesconto);
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDadosAdd() {
        return dadosAdd;
    }

    public void setDadosAdd(String dadosAdd) {
        this.dadosAdd = dadosAdd;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getDataCanc() {
        return dataCanc;
    }

    public void setDataCanc(String dataCanc) {
        this.dataCanc = dataCanc;
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
        if (!(object instanceof Emissao65)) {
            return false;
        }
        Emissao65 other = (Emissao65) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.comercial.entidade.Emissao65[ id=" + id + " ]";
    }
    
}
