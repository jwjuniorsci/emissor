/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.entidade.Emissao;
import br.com.comercial.entidade.Emissao65;
import br.com.comercial.entidade.Emitente;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.facade.Emissao65Facade;
import br.com.comercial.facade.EmissaoFacade;
import br.com.comercial.facade.ParametrosFiscaisFacade;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.dom.Enum.StatusEnum;
import br.com.samuelweb.nfe.dom.Enum.TipoManifestacao;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.com.websoft.nfe.Configuracao;
import br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.TRetConsSitNFe;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TProcEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junior
 */
@Named
@SessionScoped
public class FiscalUtilsControle implements Serializable {

    private Emissao emissao;
    private Emissao65 emissao65;

    private ParametrosFiscais parametrosFiscais;
    @Inject
    private ParametrosFiscaisFacade parametrosFiscaisFacade;
    @Inject
    private EmissaoFacade emissaoFacade;
    @Inject
    private Emissao65Facade emissao65Facade;

    private List<Emissao> emissoes;
    private List<Emissao65> emissoes65;
    private String chave;
    private String tipo;
    private String motivo;
    private String manifestar;

    public FiscalUtilsControle() {
        tipo = "";
        chave = "";
    }

    public void consultaNf() {
        try {
            parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
            Configuracao.iniciaConf(parametrosFiscais.getEmitente(), parametrosFiscais);
            TRetConsSitNFe retorno = null;
            if (tipo.equals("55")) {
                retorno = Nfe.consultaXml(chave, ConstantesUtil.NFE);
            } else if (tipo.equals("65")) {
                retorno = Nfe.consultaXml(chave, ConstantesUtil.NFCE);
            }
            System.out.println("Status:" + retorno.getCStat());
            System.out.println("Motivo:" + retorno.getXMotivo());
            System.out.println("Data:" + retorno.getProtNFe().getInfProt().getDhRecbto());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Consulta Realizada!",
                            "Chave:" + chave + " | Status: " + retorno.getXMotivo()));
        } catch (CertificadoException | NfeException e) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Consultar!",
                            "Não foi Possivel Consultar!"));
        }
    }

    public void cancelarNf() {
        if (motivo.length() < 15) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Cancelar!",
                            "O Motivo deve conter no minimo 15 caracteres!"));
        } else {
            if (tipo.equals("55")) {

                emissoes = emissaoFacade.listar();
                for (Emissao e : emissoes) {

                    if (chave.equals(e.getChave())) {
                        System.out.println("Entrou no if");
                        emissao = e;
                    }
                }
            } else if (tipo.equals("65")) {
                emissoes65 = emissao65Facade.listar();
                for (Emissao65 ec : emissoes65) {
                    if (chave.equals(ec.getChave())) {
                        emissao65 = ec;
                    }
                }
            } else {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Cancelar!",
                                "Selecione o Modelo!"));
                return;
            }

            try {
                parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
                Configuracao.iniciaConf(parametrosFiscais.getEmitente(), parametrosFiscais);
                String protocolo = "";
                String cnpj = parametrosFiscais.getEmitente().getCnpj();

                String id = "ID" + ConstantesUtil.EVENTO.CANCELAR + chave + "01";
                TEnvEvento envEvento = new TEnvEvento();
                envEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);
                envEvento.setIdLote("1");

                TEvento eventoCancela = new TEvento();
                eventoCancela.setVersao(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);

                TEvento.InfEvento infEvento = new TEvento.InfEvento();
                infEvento.setId(id);
                infEvento.setChNFe(chave);
                infEvento.setCOrgao(String.valueOf("41"));
                infEvento.setTpAmb(parametrosFiscais.getTipoEmi());
                String cnpj2;
                cnpj = cnpj.replace(" ", ""); //tira espaço em branco
                cnpj = cnpj.replace(".", ""); //tira ponto
                cnpj = cnpj.replace("/", ""); //tira barra
                cnpj = cnpj.replace("-", ""); //tira hífen
                infEvento.setCNPJ(cnpj);

                infEvento.setDhEvento(XmlUtil.dataNfe());
                infEvento.setTpEvento(ConstantesUtil.EVENTO.CANCELAR);
                infEvento.setVerEvento(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);
                infEvento.setNSeqEvento(parametrosFiscais.getNevento().toString());

                TEvento.InfEvento.DetEvento detEvento = new TEvento.InfEvento.DetEvento();
                detEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);
                detEvento.setDescEvento("Cancelamento");
                if (tipo.equals("55")) {
                    System.out.println("Protocolo 55");
                    System.out.println(emissao.getProtocolo());
                    detEvento.setNProt(emissao.getProtocolo());
                } else if (tipo.equals("65")) {
                    System.out.println("Protocolo 65");
                    System.out.println(emissao65.getProtocolo());
                    detEvento.setNProt(emissao65.getProtocolo());
                }
                detEvento.setXJust(motivo);
                infEvento.setDetEvento(detEvento);
                eventoCancela.setInfEvento(infEvento);
                envEvento.getEvento().add(eventoCancela);
                TRetEnvEvento retorno = null;
                if (tipo.equals("55")) {
                    retorno = Nfe.cancelarNfe(envEvento, false, ConstantesUtil.NFE);
                } else if (tipo.equals("65")) {
                    retorno = Nfe.cancelarNfe(envEvento, false, ConstantesUtil.NFCE);
                }
                System.out.println("Parte de erro");
                if (!StatusEnum.LOTE_EVENTO_PROCESSADO.getCodigo().equals(retorno.getCStat())) {
                    System.out.println(retorno.getXMotivo());
                    throw new NfeException("Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
                }

                if (!StatusEnum.EVENTO_VINCULADO.getCodigo().equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {
                    System.out.println(retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
                    throw new NfeException("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat() + " - Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
                }
                System.out.println("Ate aqui não deu erro");
                System.out.println("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat());
                System.out.println("Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
                System.out.println("Data:" + retorno.getRetEvento().get(0).getInfEvento().getDhRegEvento());

                // Cria TProcEventoNFe
                TProcEvento procEvento = new TProcEvento();
                procEvento.setVersao("1.00");
                procEvento.setEvento(envEvento.getEvento().get(0));
                procEvento.setRetEvento(retorno.getRetEvento().get(0));
                System.out.println("Vai exibir o xml");
                System.out.println(XmlUtil.objectToXml(procEvento));
                if (tipo.equals("55")) {
                    try {
                        emissao.setDataCancel(new Date().toString());
                        emissao.setStatus(retorno.getXMotivo());
                        emissaoFacade.salvar(emissao);
                    } catch (Exception ex) {
                        FacesContext.getCurrentInstance().
                                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Salvar!",
                                        "Não foi Possivel Salvar a nota 55!"));
                    }
                } else if (tipo.equals("65")) {
                    try {
                        emissao65.setStatus(retorno.getXMotivo());
                        emissao65.setDataCanc(new Date().toString());
                        emissao65Facade.salvar(emissao65);
                    } catch (Exception ex) {
                        FacesContext.getCurrentInstance().
                                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Salvar!",
                                        "Não foi Possivel Salvar a nota 65!"));
                    }
                }
                parametrosFiscais.setNevento(parametrosFiscais.getNevento() + 1);
                try {
                    parametrosFiscaisFacade.salvar(parametrosFiscais);
                } catch (Exception ex) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Salvar!",
                                    "Não foi Possivel o Parametro Fiscal"));
                }
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelado com Sucesso!",
                                retorno.getXMotivo()));

            } catch (CertificadoException | NfeException | JAXBException ex) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Cancelar!",
                                ex.getMessage()));
            }
        }
    }

    public void manifesto() {
        try {
            parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
            Configuracao.iniciaConf(parametrosFiscais.getEmitente(), parametrosFiscais);

            String cnpj = parametrosFiscais.getEmitente().getCnpj();
            cnpj = cnpj.replace(" ", ""); //tira espaço em branco
            cnpj = cnpj.replace(".", ""); //tira ponto
            cnpj = cnpj.replace("/", ""); //tira barra
            cnpj = cnpj.replace("-", ""); //tira hífen
            TipoManifestacao tipoManifestacao = null;
            if (manifestar.equals("cp")) {
                tipoManifestacao = TipoManifestacao.CIENCIA_DA_OPERACAO;
            }else if(manifestar.equals("co")){
                tipoManifestacao = TipoManifestacao.CONFIRMACAO_DA_OPERACAO;
            }else if(manifestar.equals("do")){
                tipoManifestacao = TipoManifestacao.DESCONHECIMENTO_DA_OPERACAO;
            }else if(manifestar.equals("or")){
                tipoManifestacao = TipoManifestacao.OPERACAO_NAO_REALIZADA;
            }

            br.inf.portalfiscal.nfe.schema.envConfRecebto.TRetEnvEvento retorno = Nfe.manifestacao(chave, tipoManifestacao, cnpj, motivo, XmlUtil.dataNfe());

            if (!StatusEnum.LOTE_EVENTO_PROCESSADO.getCodigo().equals(retorno.getCStat())) {
                FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Executar Manifesto!",
                                    "Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo()));
                
                return;
            }

            if (!StatusEnum.EVENTO_VINCULADO.getCodigo().equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {
                FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Executar Manifesto!",
                                   "Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat() + " - Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo()));
                return;
            }

            System.out.println("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat());
            System.out.println("Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
            System.out.println("Data:" + retorno.getRetEvento().get(0).getInfEvento().getDhRegEvento());
            FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Manifesto Executado!",
                                   retorno.getRetEvento().get(0).getInfEvento().getXMotivo()));

        } catch (CertificadoException | NfeException e) {
           FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao ExecutarManifesto!",
                            e.getMessage() ));
        }

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

    public ParametrosFiscais getParametrosFiscais() {
        return parametrosFiscais;
    }

    public void setParametrosFiscais(ParametrosFiscais parametrosFiscais) {
        this.parametrosFiscais = parametrosFiscais;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getManifestar() {
        return manifestar;
    }

    public void setManifestar(String manifestar) {
        this.manifestar = manifestar;
    }

    

}
