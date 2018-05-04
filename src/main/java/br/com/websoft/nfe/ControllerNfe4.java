/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.websoft.nfe;

import br.com.comercial.entidade.Emitente;
import br.com.comercial.entidade.EmitenteCte;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.facade.EmitenteCteFacade;
import br.com.comercial.facade.EmitenteFacade;
import br.com.comercial.facade.ParametrosFiscaisFacade;
import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoProperties;
import br.com.samuelweb.certificado.CertificadoService;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.dom.ConfiguracoesIniciaisNfe;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.CertificadoUtil;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.Estados;
import br.inf.portalfiscal.nfe.schema_4.retConsStatServ.TRetConsStatServ;
import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author JW Junior
 */
@Named
@SessionScoped
public class ControllerNfe4 implements Serializable {

    private String statusNfe;
    private String statusNfce;
    private ParametrosFiscais parametrosFiscais;
    private Emitente emitente;
    private EmitenteCte emitenteCte;
    @Inject
    private EmitenteFacade emitenteFacade;
    @Inject
    private EmitenteCteFacade emitenteCteFacade;
    @Inject
    private ParametrosFiscaisFacade parametrosFiscaisFacade;

    public ControllerNfe4() {

    }

    public static ConfiguracoesIniciaisNfe iniciaConf() throws NfeException, CertificadoException {
        Certificado certificado = CertificadoService.certificadoPfx("C:\\nfe\\DADOS\\EMPR0001\\certificado.pfx", "1234");
        return ConfiguracoesIniciaisNfe.iniciaConfiguracoes(Estados.PR, ConstantesUtil.AMBIENTE.HOMOLOGACAO, certificado,
                "C:\\schemas\\schemas");
    }

    public String consultaStatusNfe() {
        TRetConsStatServ retorno = null;

        if (retornaParam() != null && retornaEmi() != null) {
            try {
                //Certificado certificado = CertificadoService.certificadoPfx("C:\\nfe\\DADOS\\EMPR0001\\certificado.pfx", "1234");
                //ConfiguracoesIniciaisNfe config = ConfiguracoesIniciaisNfe.iniciaConfiguracoes(Estados.PR, ConstantesUtil.AMBIENTE.HOMOLOGACAO,
                //        certificado, "C:\\schemas\\schemas");
                //config = ConfiguracoesIniciaisNfe.getInstance();
                Configuracao.iniciaConf(emitente, parametrosFiscais);
                retorno = Nfe.statusServico(ConstantesUtil.NFE);
                statusNfe = retorno.getXMotivo();
                System.out.println("Status:" + retorno.getCStat());
                System.out.println("Motivo:" + retorno.getXMotivo());
                System.out.println("Data:" + retorno.getDhRecbto());
                statusNfe = retorno.getXMotivo();
                return statusNfe;
            } catch (CertificadoException | NfeException e) {
                System.err.println(e.getMessage());
            }
        } else {
            return statusNfe = "Configuraçoes Fiscais desconhecidas";
        }

        return "Erro ao Consultar";

    }

    public String consultaStatusNfce() {
        if (retornaParam() != null && retornaEmi() != null) {
            try {
                Configuracao.iniciaConf(emitente, parametrosFiscais);
                TRetConsStatServ retorno = Nfe.statusServico(ConstantesUtil.NFCE);
                statusNfce = retorno.getXMotivo();
                return statusNfce;
            } catch (CertificadoException | NfeException e) {
                System.err.println(e.getMessage());
            }
        } else {
            statusNfce = "Configuraçoes Fiscais desconhecidas";
        }
        return "Erro ao Consultar";
    }

    public Emitente retornaEmi() {
        if (emitenteFacade.listar() == null || emitenteFacade.listar().isEmpty()) {
            return null;
        } else {
            emitente = new Emitente();
            emitente = emitenteFacade.listar().get(0);
            return emitente;
        }
    }
    
    public EmitenteCte retornaEmiCte(){
        if(emitenteCteFacade.listar() == null || emitenteCteFacade.listar().isEmpty()){
            return null;
        } else {
            emitenteCte = new EmitenteCte();
            emitenteCte = emitenteCteFacade.listar().get(0);
            return emitenteCte;
        }
    }

    public ParametrosFiscais retornaParam() {
        parametrosFiscais = new ParametrosFiscais();
        parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
        return parametrosFiscais;
    }

    public String getStatusNfe() {
        return statusNfe;
    }

    public void setStatusNfe(String statusNfe) {
        this.statusNfe = statusNfe;
    }

    public String getStatusNfce() {
        return statusNfce;
    }

    public void setStatusNfce(String statusNfce) {
        this.statusNfce = statusNfce;
    }

}
