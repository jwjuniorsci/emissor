/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;



import br.com.comercial.entidade.Emitente;
import br.com.comercial.entidade.Inutilizacao;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.facade.EmitenteFacade;
import br.com.comercial.facade.InutilizacaoFacade;
import br.com.comercial.facade.ParametrosFiscaisFacade;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.dom.ConfiguracoesIniciaisNfe;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.websoft.nfe.Configuracao;
import br.inf.portalfiscal.nfe.schema_4.inutNFe.TRetInutNFe;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author junior
 */
@Named
@SessionScoped
public class InutilizacaoController implements Serializable {

    private String cnpj;
    private Inutilizacao inutilizacao;
    private ParametrosFiscais parametrosFiscais;
    private Emitente emitente;

    @Inject
    private ParametrosFiscaisFacade parametrosFiscaisFacade;

    @Inject
    private EmitenteFacade emitenteFacade;
    
    @Inject
    private InutilizacaoFacade inutilizacaoFacade;
    
    public void novo(){
        inutilizacao = new Inutilizacao();
        System.out.println("Completou o metodo");
    }
    
    public void salvar() throws Exception{
        System.out.println("SAlvou o metodo");
        inutilizar65();
        inutilizacao.setData(new Date());
        inutilizacaoFacade.salvar(inutilizacao);
    }
    
    public List<Inutilizacao> getLista(){
        return inutilizacaoFacade.listar();
    }

    public void inutilizar65() throws NfeException, CertificadoException {
        if (inutilizacao.getMotivo().equals("") || inutilizacao.getMotivo() == null) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Inutilizar!", "O Campo Modelo é obrigatório"));
        } else if(inutilizacao.getModelo().equals("")){
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Inutilizar!", 
                            "O campo Modelo é obrigatório"));
        }  
        else {
            parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
            emitente = emitenteFacade.listar().get(0);
            Configuracao.iniciaConf(emitente, parametrosFiscais);

            TRetInutNFe retorno = Nfe.inutilizacao(geraChaveNfe(parametrosFiscais, inutilizacao.getModelo(), inutilizacao.getNumI(), inutilizacao.getNumF()), inutilizacao.getMotivo(), ConstantesUtil.NFCE, true);
            TRetInutNFe.InfInut infRetorno = retorno.getInfInut();
            System.out.println("Status:" + infRetorno.getCStat());
            System.out.println("Motivo:" + infRetorno.getXMotivo());
            System.out.println("Data:" + infRetorno.getDhRecbto());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operação Concluida!", "Motivo:" + infRetorno.getXMotivo()));
        }
    }

    public static String lpadTo(String input, int width, char ch) {
        String strPad;

        StringBuilder sb = new StringBuilder(input.trim());
        while (sb.length() < width) {
            sb.insert(0, ch);
        }
        strPad = sb.toString();

        if (strPad.length() > width) {
            strPad = strPad.substring(0, width);
        }
        return strPad;
    }

    public String geraChaveNfe(ParametrosFiscais p, String modelo, String iniNum, String fimNum) {
        System.out.println("Chamou o metodo");
        cnpj = p.getEmitente().getCnpj();
        cnpj = cnpj.replace(" ", ""); //tira espaço em branco
        cnpj = cnpj.replace(".", ""); //tira ponto
        cnpj = cnpj.replace("/", ""); //tira barra
        cnpj = cnpj.replace("-", ""); //tira hífen
        try {

            String cUF = "41"; // Código da UF do emitente do Documento Fiscal.    
            String dataAA = new SimpleDateFormat("yy").format(new Date()); // Ano e Mês de emissão da NF-e.    
            String cnpjCpf = cnpj; // CNPJ do emitente.    
            String mod = modelo; // Modelo do Documento Fiscal.    
            String serie = "1"; // Série do Documento Fiscal.    
            String nIni = iniNum;
            String nFim = fimNum;

            StringBuilder chave = new StringBuilder();
            chave.append(lpadTo(cUF, 2, '0'));
            chave.append(lpadTo(dataAA, 2, '0'));
            chave.append(lpadTo(cnpjCpf.replaceAll("\\D", ""), 14, '0'));
            chave.append(lpadTo(mod, 2, '0'));
            chave.append(lpadTo(serie, 3, '0'));
            chave.append(lpadTo(nIni, 9, '0'));
            chave.append(lpadTo(nFim, 9, '0'));
            chave.insert(0, "ID");

            return chave.toString();
        } catch (Exception e) {
            e.getStackTrace();
            return "";
        }
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    

    public ParametrosFiscais getParametrosFiscais() {
        return parametrosFiscais;
    }

    public void setParametrosFiscais(ParametrosFiscais parametrosFiscais) {
        this.parametrosFiscais = parametrosFiscais;
    }

    public Emitente getEmitente() {
        return emitente;
    }

    public void setEmitente(Emitente emitente) {
        this.emitente = emitente;
    }

    public ParametrosFiscaisFacade getParametrosFiscaisFacade() {
        return parametrosFiscaisFacade;
    }

    public void setParametrosFiscaisFacade(ParametrosFiscaisFacade parametrosFiscaisFacade) {
        this.parametrosFiscaisFacade = parametrosFiscaisFacade;
    }

    public EmitenteFacade getEmitenteFacade() {
        return emitenteFacade;
    }

    public void setEmitenteFacade(EmitenteFacade emitenteFacade) {
        this.emitenteFacade = emitenteFacade;
    }

    public Inutilizacao getInutilizacao() {
        return inutilizacao;
    }

    public void setInutilizacao(Inutilizacao inutilizacao) {
        this.inutilizacao = inutilizacao;
    }
    
    

}
