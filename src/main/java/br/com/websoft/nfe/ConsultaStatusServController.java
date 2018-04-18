/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.websoft.nfe;

import br.com.comercial.controle.LoginControle;
import br.com.comercial.entidade.Emitente;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.facade.EmitenteFacade;
import br.com.comercial.facade.ParametrosFiscaisFacade;
import br.com.comercial.util.AssinarXml;
import br.com.comercial.util.ConfiguracaoNfe;
import br.com.comercial.util.ValidadeCertificado;
import br.com.samuelweb.nfe.exception.NfeException;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;

/**
 *
 * @author JW Junior
 */
@Named
@SessionScoped
public class ConsultaStatusServController {

    private ConfiguracaoNfe configuracaoNfe;

    private GeraCacerts geraCacerts;

    private NFeCadastroConsulta nFeCadastroConsulta;

    private ConsultaNfe consultaNfe;

    private ConsultaStatus consultaStatus2;

    private InutilizaNfe inutilizaNfe;

    private ValidadeCertificado validadeCertificado;

    private AssinarXml assinarXml;

    private String retornoStatus;

    private ParametrosFiscais parametrosFiscais;

    private Emitente emitente;

    @Inject
    private ParametrosFiscaisFacade parametrosFiscaisFacade;

    @Inject
    private EmitenteFacade emitenteFacade;
    
    @Inject
    private LoginControle loginControle;

    

    /*public void geraCadeiaJks(){
        try {
            FileUtils.writeByteArrayToFile(new File("C:\\chaves\\producao.cacerts"), NFGeraCadeiaCertificados.geraCadeiaCertificados(NFAmbiente.PRODUCAO, "1234"));
            System.out.println("Aparentemente gerou o de produção");
            FileUtils.writeByteArrayToFile(new File("C:\\chaves\\homologacao.cacerts"), NFGeraCadeiaCertificados.geraCadeiaCertificados(NFAmbiente.HOMOLOGACAO, "1234"));
            System.out.println("Aparentemente gerou o de homologação");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Não conseguiu gerar");
        }
    }*/
 /*public void consultar() throws IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, Exception{
        configuracaoNfe = new ConfiguracaoNfe();
        NFStatusServicoConsultaRetorno retorno = new WSFacade(configuracaoNfe).consultaStatus(NFUnidadeFederativa.PR, NFModelo.NFE);
        System.out.println(retorno.getStatus());
        System.out.println(retorno.getMotivo());
    }*/
    public void gerarCacerts() {
        geraCacerts = new GeraCacerts();
        geraCacerts.gerarCacerts();
    }

    public void relConsulta() throws NfeException {
        nFeCadastroConsulta = new NFeCadastroConsulta();
        //nFeCadastroConsulta.consultarCad();
    }

    public void statusNota() throws JAXBException {
        consultaNfe = new ConsultaNfe();
        //consultaNfe.realizarConsulta();
    }

    public String verificaCert() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        if (retornaEmi() != null && retornaParam() != null) {
            validadeCertificado = new ValidadeCertificado();
            return validadeCertificado.validaCertificado(retornaEmi());
        }
        return "Não foi possivel acessar informações do certifcado";
    }

    public void assinar() {
        assinarXml = new AssinarXml();
        assinarXml.assinar();
    }

    public void inutilizarNfe() {
        inutilizaNfe = new InutilizaNfe();
        //inutilizaNfe.inutilizar();
    }

    public void consultaCert() throws NfeException {
        configuracaoNfe = new ConfiguracaoNfe();
        //configuracaoNfe.consultaCert();
    }

    public void consultaStatus() {
        consultaStatus2 = new ConsultaStatus();
        //consultaStatus2.consultarStat();
        //retornoStatus = consultaStatus2.consultarStat();
    }
    
    public Emitente retornaEmi(){
        emitente = new Emitente();
        emitente = emitenteFacade.listar().get(0);
        return emitente;
    }
    
    public ParametrosFiscais retornaParam() {
        parametrosFiscais = new ParametrosFiscais();
        parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
        return parametrosFiscais;
    }
    
    public Boolean statusEmit(){
        /*if(emitenteFacade.listar() == null || emitenteFacade.listar().isEmpty()){
            return false;
        } else {
            emitente = emitenteFacade.listar().get(0);
        }
        
        if(emitente != null){
            return emitente.getStatus();
        } else if(loginControle.getUsuAdm() == true){
            return true; 
        } else {
            return false;
        }*/
        if(loginControle.getUsuAdm() == true){
            return true;
        } else if(emitenteFacade.listar() == null || emitenteFacade.listar().isEmpty()){
            return false;
        } else if(emitenteFacade.listar() != null || !emitenteFacade.listar().isEmpty()){
            emitente = new Emitente();
            emitente = emitenteFacade.listar().get(0);
            return emitente.getStatus();
        }
        return false;
    }

    public String getRetornoStatus() {
        return retornoStatus;
    }

    public void setRetornoStatus(String retornoStatus) {
        this.retornoStatus = retornoStatus;
    }

    public ConsultaStatus getConsultaStatus2() {
        return consultaStatus2;
    }

    public void setConsultaStatus2(ConsultaStatus consultaStatus2) {
        this.consultaStatus2 = consultaStatus2;
    }

    public ValidadeCertificado getValidadeCertificado() {
        return validadeCertificado;
    }

    public void setValidadeCertificado(ValidadeCertificado validadeCertificado) {
        this.validadeCertificado = validadeCertificado;
    }

    public Emitente getEmitente() {
        return emitente;
    }

    public void setEmitente(Emitente emitente) {
        this.emitente = emitente;
    }
    
    

}
