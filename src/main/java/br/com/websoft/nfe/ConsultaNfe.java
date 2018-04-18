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
import br.com.samuelweb.nfe.util.XmlUtil;
import java.net.URL;
import java.security.Security;
import javax.xml.bind.JAXBException;

/**
 *
 * @author JW Junior
 */
public class ConsultaNfe {

    /*public void realizarConsulta() throws JAXBException {
        try {
            Certificado certificado = CertificadoUtil.certificadoPfx("C:\\nfe\\DADOS\\EMPR0001\\certificado.pfx", "1234");
            ConfiguracoesIniciaisNfe config = ConfiguracoesIniciaisNfe.iniciaConfiguracoes(Estados.PR, ConstantesUtil.AMBIENTE.HOMOLOGACAO,
                    certificado, "C:\\nfee\\Schemas", ConstantesUtil.VERSAO.V3_10);
            config = ConfiguracoesIniciaisNfe.getInstance();

            TConsSitNFe consSitNFe = new TConsSitNFe();
            consSitNFe.setVersao(config.getVersaoNfe());
            consSitNFe.setTpAmb(config.getAmbiente());
            consSitNFe.setXServ("CONSULTAR");
            consSitNFe.setChNFe("41170709609953000126650010000000161000001672");

            br.inf.portalfiscal.nfe.schema.retconssitnfe.TRetConsSitNFe retorno = Nfe.consultaXml(consSitNFe, true, ConstantesUtil.NFCE);

            System.out.println("Status:" + retorno.getCStat());
            System.out.println("Motivo:" + retorno.getXMotivo());
            System.out.println("Data:" + retorno.getProtNFe().getInfProt().getDhRecbto());
            
            String xmlProtNfe = XmlUtil.objectToXml(retorno.getProtNFe());
            System.out.println(xmlProtNfe);

        } catch (NfeException e) {
            System.out.println("Erro: "+ e.getMessage());
        }
    }*/

}
