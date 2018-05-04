/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author JW Junior
 */
@Entity
public class Emissao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String formPag;
    private String finalidade;
    private String numNfeFat;
    private String serie;
    private String modelo;
    private String chave;
    private String dataDigit;
    private String dataEmissao;
    private String dataCancel;
    private String indIEDest;
    private String destOp;
    private String ie;
    private String protocolo;
    private BigDecimal icmsBc;
    private BigDecimal ipiBc;
    private BigDecimal pisBc;
    private BigDecimal cofinsBc;
    private BigDecimal icmsVlr;
    private BigDecimal ipiVlr;
    private BigDecimal pisVlr;
    private BigDecimal cofinsVlr;
    private Integer qtdVolumes;
    private Integer numNfe;
    private Float pesoVolumes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "emissao", fetch = FetchType.LAZY)
    private List<ItemEmissao> listaProd;
    @ManyToOne
    private Emitente emitente;
    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Transportadora transportadora;
    private String plavaVeiculo;
    private String ufPlaca;
    private String status;
    private String natOp;
    private String indPres;
    private String tpNf;
    private String caminho;
    private String tpImp;
    private String modalidadeFrete;
    private BigDecimal vlrProdNf;
    private BigDecimal vlrDescNf;
    private BigDecimal vlrTotalNf;
    //ICMS retido do Transportador
    private BigDecimal vserv;
    private BigDecimal vbCRet;
    private BigDecimal piCMSRet;
    private BigDecimal viCMSRet;
    private String cfopTransp;
    //Identificação do Veículo do Transportador
    private String vplaca;
    private String vuf;
    private String vrntc;
    //Identificação do Reboque do Transportador
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "emissao", fetch = FetchType.LAZY)
    private List<ReboqueTrans> reboques;
    //Volumes
    private BigDecimal qvol;
    private BigDecimal pesoL;
    private BigDecimal pesoB;
    private String esp;
    private String marca;
    private String nvol;
    private String lacres;
    //Lacres
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "emissao", fetch = FetchType.LAZY)
    private List<Lacres> listaLacres;
    public Emissao() {
        listaProd = new ArrayList<ItemEmissao>();
        reboques = new ArrayList<ReboqueTrans>();
        listaLacres = new ArrayList<Lacres>();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormPag() {
        return formPag;
    }

    public void setFormPag(String formPag) {
        this.formPag = formPag;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

   

    public String getNumNfeFat() {
        return numNfeFat;
    }

    public void setNumNfeFat(String numNfeFat) {
        this.numNfeFat = numNfeFat;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
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

    public Integer getQtdVolumes() {
        return qtdVolumes;
    }

    public void setQtdVolumes(Integer qtdVolumes) {
        this.qtdVolumes = qtdVolumes;
    }

    public Integer getNumNfe() {
        return numNfe;
    }

    public void setNumNfe(Integer numNfe) {
        this.numNfe = numNfe;
    }

    public Float getPesoVolumes() {
        return pesoVolumes;
    }

    public void setPesoVolumes(Float pesoVolumes) {
        this.pesoVolumes = pesoVolumes;
    }

    public List<ItemEmissao> getListaProd() {
        return listaProd;
    }

    public void setListaProd(List<ItemEmissao> listaProd) {
        this.listaProd = listaProd;
    }
    
    public String getPlavaVeiculo() {
        return plavaVeiculo;
    }

    public void setPlavaVeiculo(String plavaVeiculo) {
        this.plavaVeiculo = plavaVeiculo;
    }

    public String getUfPlaca() {
        return ufPlaca;
    }

    public void setUfPlaca(String ufPlaca) {
        this.ufPlaca = ufPlaca;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

    public BigDecimal getVlrProdNf() {
        return vlrProdNf;
    }

    public void setVlrProdNf(BigDecimal vlrProdNf) {
        this.vlrProdNf = vlrProdNf;
    }

    public BigDecimal getVlrDescNf() {
        return vlrDescNf;
    }

    public void setVlrDescNf(BigDecimal vlrDescNf) {
        this.vlrDescNf = vlrDescNf;
    }

    public BigDecimal getVlrTotalNf() {
        return vlrTotalNf;
    }

    public void setVlrTotalNf(BigDecimal vlrTotalNf) {
        this.vlrTotalNf = vlrTotalNf;
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

    public String getDataDigit() {
        return dataDigit;
    }

    public void setDataDigit(String dataDigit) {
        this.dataDigit = dataDigit;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDataCancel() {
        return dataCancel;
    }

    public void setDataCancel(String dataCancel) {
        this.dataCancel = dataCancel;
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

    public String getIndPres() {
        return indPres;
    }

    public void setIndPres(String indPres) {
        this.indPres = indPres;
    }

    public String getIndIEDest() {
        return indIEDest;
    }

    public void setIndIEDest(String indIEDest) {
        this.indIEDest = indIEDest;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getDestOp() {
        return destOp;
    }

    public void setDestOp(String destOp) {
        this.destOp = destOp;
    }

    public String getTpImp() {
        return tpImp;
    }

    public void setTpImp(String tpImp) {
        this.tpImp = tpImp;
    }

    public String getModalidadeFrete() {
        return modalidadeFrete;
    }

    public void setModalidadeFrete(String modalidadeFrete) {
        this.modalidadeFrete = modalidadeFrete;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public BigDecimal getVserv() {
        return vserv;
    }

    public void setVserv(BigDecimal vserv) {
        this.vserv = vserv;
    }

    public BigDecimal getVbCRet() {
        return vbCRet;
    }

    public void setVbCRet(BigDecimal vbCRet) {
        this.vbCRet = vbCRet;
    }

    public BigDecimal getPiCMSRet() {
        return piCMSRet;
    }

    public void setPiCMSRet(BigDecimal piCMSRet) {
        this.piCMSRet = piCMSRet;
    }

    public BigDecimal getViCMSRet() {
        return viCMSRet;
    }

    public void setViCMSRet(BigDecimal viCMSRet) {
        this.viCMSRet = viCMSRet;
    }

    

    public String getCfopTransp() {
        return cfopTransp;
    }

    public void setCfopTransp(String cfopTransp) {
        this.cfopTransp = cfopTransp;
    }

    
    public BigDecimal getPesoL() {
        return pesoL;
    }

    public void setPesoL(BigDecimal pesoL) {
        this.pesoL = pesoL;
    }

    public BigDecimal getPesoB() {
        return pesoB;
    }

    public void setPesoB(BigDecimal pesoB) {
        this.pesoB = pesoB;
    }

    public String getEsp() {
        return esp;
    }

    public void setEsp(String esp) {
        this.esp = esp;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    

    public String getLacres() {
        return lacres;
    }

    public void setLacres(String lacres) {
        this.lacres = lacres;
    }

    public String getVplaca() {
        return vplaca;
    }

    public void setVplaca(String vplaca) {
        this.vplaca = vplaca;
    }

    public String getVuf() {
        return vuf;
    }

    public void setVuf(String vuf) {
        this.vuf = vuf;
    }

    public String getVrntc() {
        return vrntc;
    }

    public void setVrntc(String vrntc) {
        this.vrntc = vrntc;
    }


    public BigDecimal getQvol() {
        return qvol;
    }

    public void setQvol(BigDecimal qvol) {
        this.qvol = qvol;
    }

    public String getNvol() {
        return nvol;
    }

    public void setNvol(String nvol) {
        this.nvol = nvol;
    }

    public List<ReboqueTrans> getReboques() {
        return reboques;
    }

    public void setReboques(List<ReboqueTrans> reboques) {
        this.reboques = reboques;
    }

    public List<Lacres> getListaLacres() {
        return listaLacres;
    }

    public void setListaLacres(List<Lacres> listaLacres) {
        this.listaLacres = listaLacres;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
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
        if (!(object instanceof Emissao)) {
            return false;
        }
        Emissao other = (Emissao) object;
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
