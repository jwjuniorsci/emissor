/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.websoft.nfe;

import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.CertificadoUtil;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.Estados;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author JW Junior
 */
public class ConsultaStatus {

    private String status;

    public ConsultaStatus() {

    }

    /*public String consultarStat() {
        TRetConsStatServ retorno = null;
        try {
            Certificado certificado = CertificadoUtil.certificadoPfx("C:\\nfe\\DADOS\\EMPR0001\\certificado.pfx", "1234");
            ConfiguracoesIniciaisNfe config = ConfiguracoesIniciaisNfe.iniciaConfiguracoes(Estados.PR, ConstantesUtil.AMBIENTE.HOMOLOGACAO,
                    certificado, "C:\\nfee\\Schemas", ConstantesUtil.VERSAO.V3_10);
            config = ConfiguracoesIniciaisNfe.getInstance();
            TConsStatServ consStatServ = new TConsStatServ();
            consStatServ.setTpAmb(config.getAmbiente());
            consStatServ.setCUF(config.getEstado().getCodigoIbge());
            consStatServ.setVersao(config.getVersaoNfe());
            consStatServ.setXServ("STATUS");

            retorno = Nfe.statusServico(consStatServ, true, ConstantesUtil.NFE);
            System.out.println("Status: " + retorno.getCStat());
            System.out.println("Motivo: " + retorno.getXMotivo());
            System.out.println("Data: " + retorno.getDhRecbto());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Status do Serviço", retorno.getCStat()));

        } catch (NfeException e) {
            System.out.println("Erro:" + e.getMessage());
        }
        return status = "Status: " + retorno.getXMotivo();
    }*/

}
