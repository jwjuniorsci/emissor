/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.entidade.Emissao;
import br.com.comercial.entidade.Emissao65;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.facade.Emissao65Facade;
import br.com.comercial.facade.EmissaoFacade;
import br.com.comercial.facade.ParametrosFiscaisFacade;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.dom.Enum.StatusEnum;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.com.websoft.nfe.Configuracao;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envcce.TEvento;
import br.inf.portalfiscal.nfe.schema.envcce.TProcEvento;
import br.inf.portalfiscal.nfe.schema.envcce.TRetEnvEvento;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBException;

/**
 *
 * @author junior
 */
@Named
@SessionScoped
public class CartaCorrecaoNfeController implements Serializable {

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
    private String xml;

    public void cartaCorrecao() {
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
                        System.out.println("Chave: " + chave);
                        System.out.println("Protocolo: " + emissao.getProtocolo());
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

                String cnpj = parametrosFiscais.getEmitente().getCnpj();
                cnpj = cnpj.replace(" ", ""); //tira espaço em branco
                cnpj = cnpj.replace(".", ""); //tira ponto
                cnpj = cnpj.replace("/", ""); //tira barra
                cnpj = cnpj.replace("-", ""); //tira hífen

                String sequencia = parametrosFiscais.getNevento().toString();
                String id = "ID" + ConstantesUtil.EVENTO.CCE + chave + (sequencia.length() < 2 ? "0" + sequencia : sequencia);

                TEnvEvento envEvento = new TEnvEvento();
                envEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);
                envEvento.setIdLote("1");

                br.inf.portalfiscal.nfe.schema.envcce.TEvento evento = new br.inf.portalfiscal.nfe.schema.envcce.TEvento();
                evento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);

                br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento infEvento = new br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento();
                infEvento.setId(id);
                infEvento.setCOrgao("41");
                infEvento.setTpAmb(parametrosFiscais.getTipoEmi());

                infEvento.setCNPJ(cnpj);
                infEvento.setChNFe(chave);

                // Altere a Data
                infEvento.setDhEvento(XmlUtil.dataNfe());
                infEvento.setTpEvento(ConstantesUtil.EVENTO.CCE);
                infEvento.setNSeqEvento(sequencia);
                infEvento.setVerEvento(ConstantesUtil.VERSAO.EVENTO_CCE);

                br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento.DetEvento detEvento = new br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento.DetEvento();
                detEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);
                detEvento.setDescEvento("Carta de Correcao");

                // Informe a Correção
                detEvento.setXCorrecao(motivo);
                detEvento.setXCondUso("A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e pode ser utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja relacionado com: I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca de preco, quantidade, valor da operacao ou da prestacao; II - a correcao de dados cadastrais que implique mudanca do remetente ou do destinatario; III - a data de emissao ou de saida.");
                infEvento.setDetEvento(detEvento);
                evento.setInfEvento(infEvento);
                envEvento.getEvento().add(evento);
                TRetEnvEvento retorno = null;
                if (tipo.equals("55")) {
                    retorno = Nfe.cce(envEvento, false, ConstantesUtil.NFE);
                } else if (tipo.equals("65")) {
                    retorno = Nfe.cce(envEvento, false, ConstantesUtil.NFCE);
                }
                if (!StatusEnum.LOTE_EVENTO_PROCESSADO.getCodigo().equals(retorno.getCStat())) {
                    throw new NfeException("Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
                }

                if (!StatusEnum.EVENTO_VINCULADO.getCodigo().equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {
                    throw new NfeException("Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
                }

                System.out.println("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat());
                System.out.println("Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
                System.out.println("Data:" + retorno.getRetEvento().get(0).getInfEvento().getDhRegEvento());

                // Criação do ProcEventoNFe
                TProcEvento procEvento = new TProcEvento();
                procEvento.setEvento(envEvento.getEvento().get(0));
                procEvento.setRetEvento(retorno.getRetEvento().get(0));
                procEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);

                // String xmlProcEventoNfe = XmlUtil.objectToXml(procEvento);
                xml = XmlUtil.objectToXml(procEvento);
                System.out.println(xml);
                parametrosFiscais.setNevento(parametrosFiscais.getNevento() + 1);
                try {
                    parametrosFiscaisFacade.salvar(parametrosFiscais);
                } catch (Exception ex) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Salvar!",
                                    "Erro ao Salvar Parametros!"));
                    return;
                }
                if (tipo.equals("55")) {
                    try {
                        emissao.setStatus(retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
                        emissaoFacade.salvar(emissao);
                    } catch (Exception ex) {
                        FacesContext.getCurrentInstance().
                                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Carta de Correção Realizada!",
                                        retorno.getRetEvento().get(0).getInfEvento().getXMotivo()));
                        return;
                    }
                } else if (tipo.equals("65")) {
                    try {
                        emissao.setStatus(retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
                        emissao65Facade.salvar(emissao65);
                    } catch (Exception ex) {
                        FacesContext.getCurrentInstance().
                                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Salvar!",
                                        "Erro ao Salvar Nota!"));
                        return;
                    }
                }
                try {
                    criaXml();
                } catch (IOException ex) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Gerar XML",
                                    "Não foi possivel gerar o xml"));
                }
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Carta de Correção Realizada!",
                                ""));

            } catch (CertificadoException | NfeException | JAXBException e) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro na Carta de Correção!",
                                e.getMessage()));
            }

        }
    }

    public String criaXml() throws IOException {
        System.out.println("cria xml");
        // Create file 
        FileWriter fstream = new FileWriter(criaDiretorio() + "/" + chave + ".xml");
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(xml);
        //Close the output stream
        out.close();
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Status XML",
                        "XML criado com sucesso!"));
        return fstream.toString();
    }

    public String criaDiretorio() {
        System.out.println("cria dir");
        Date data = new Date();

        String ano = new SimpleDateFormat("yyyy").format(data);
        File diretorioAno = new File("/home/junior/fiscal/" + emissao.getEmitente().getRazao() + "-"
                + emissao.getEmitente().getCnpj() + "/xml/" + ano);
        if (!diretorioAno.exists()) {
            diretorioAno.mkdirs();
        }

        String mes = new SimpleDateFormat("MM").format(data);
        File diretorioMes = new File("/home/junior/fiscal/" + emissao.getEmitente().getRazao() + "-"
                + emissao.getEmitente().getCnpj() + "/xml/" + ano + "/" + mes + "/CartCorre/");
        if (!diretorioMes.exists()) {
            diretorioMes.mkdirs();
        }

        return diretorioMes.toString();
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

}
