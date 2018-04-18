/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.websoft.nfe;

import br.com.comercial.util.SocketFactoryDinamico;
import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.CertificadoUtil;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.Estados;
import br.inf.portalfiscal.nfe.schema.consCad.TConsCad;
import br.inf.portalfiscal.nfe.schema.consCad.TUfCons;
import br.inf.portalfiscal.nfe.schema.retConsCad.TRetConsCad;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 *
 * @author JW Junior
 */
public class NFeCadastroConsulta {

    private static final int SSL_PORT = 443;

    /*public void Consultar() {
        try {

            String estadoConsulta = "41";
            System.out.println("linha 24");
            String estadoXML = "PR";
            System.out.println("linha 25");

            String cnpjConsultado = "09609953000126";
            System.out.println("linha 26");
            URL url = new URL("https://homologacao.nfe.fazenda.pr.gov.br/nfe/CadConsultaCadastro2?wsdl");

            String caminhoDoCertificado = "C:\\nfe\\DADOS\\EMPR0001\\certificado.pfx";
            System.out.println("linha 33");
            String senhaDoCertificado = "1234";
            System.out.println("linha 35");
            String arquivoCacerts = "C:\\Users\\JW Junior\\Desktop\\Nfe\\NFeCacerts";
            System.out.println("linha 36");

            InputStream entrada = new FileInputStream(caminhoDoCertificado);
            KeyStore ks = KeyStore.getInstance("pkcs12");
            try {
                ks.load(entrada, senhaDoCertificado.toCharArray());
            } catch (IOException e) {
                throw new Exception(
                        "Senha do Certificado Digital esta incorreta ou Certificado inválido.");
            }

            String alias = "";
            Enumeration<String> aliasesEnum = ks.aliases();
            while (aliasesEnum.hasMoreElements()) {
                alias = (String) aliasesEnum.nextElement();
                if (ks.isKeyEntry(alias)) {
                    break;
                }
            }
            X509Certificate certificate = (X509Certificate) ks
                    .getCertificate(alias);
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias,
                    senhaDoCertificado.toCharArray());
            SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(
                    certificate, privateKey);
            socketFactoryDinamico
                    .setFileCacerts(arquivoCacerts);

            Protocol protocol = new Protocol("https", socketFactoryDinamico,
                    SSL_PORT);
            Protocol.registerProtocol("https", protocol);

            StringBuilder xml = new StringBuilder();

            xml.append("<nfeDadosMsg>")
                    .append("<ConsCad versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")
                    .append("<infCons>").append("<xServ>CONS-CAD</xServ>")
                    .append("<UF>").append(estadoXML).append("</UF>")
                    .append("<CNPJ>").append(cnpjConsultado).append("</CNPJ>")
                    .append("</infCons>").append("</ConsCad>")
                    .append("</nfeDadosMsg>");

            XMLStreamReader dadosXML = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml.toString()));
            System.out.println("linha 74");
            CadConsultaCadastro2Stub.NfeDadosMsg dadosMsg = CadConsultaCadastro2Stub.NfeDadosMsg.Factory.parse(dadosXML);
            System.out.println("linha 76");

            CadConsultaCadastro2Stub.NfeCabecMsg nfeCabecMsg = new CadConsultaCadastro2Stub.NfeCabecMsg();
            System.out.println("linha 79");

            nfeCabecMsg.setCUF(estadoConsulta);
            System.out.println("linha 82");

            nfeCabecMsg.setVersaoDados("2.00");
            System.out.println("linha 85");
            CadConsultaCadastro2Stub.NfeCabecMsgE nfeCabecMsgE = new CadConsultaCadastro2Stub.NfeCabecMsgE();
            System.out.println("linha 87");
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);
            System.out.println("linha 89");

            CadConsultaCadastro2Stub stub = new CadConsultaCadastro2Stub(url.toString());

            CadConsultaCadastro2Stub.ConsultaCadastro2Result result = stub.consultaCadastro2(dadosMsg, nfeCabecMsgE);

            System.out.println(result.getExtraElement().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public void consultarCad() throws NfeException {
        try {
            Certificado certificado = CertificadoUtil.certificadoPfx("C:\\nfe\\DADOS\\EMPR0001\\certificado.pfx", "1234");
            ConfiguracoesIniciaisNfe config = ConfiguracoesIniciaisNfe.iniciaConfiguracoes(Estados.PR, ConstantesUtil.AMBIENTE.HOMOLOGACAO,
                    certificado, "C:\\nfee\\Schemas", ConstantesUtil.VERSAO.V3_10);
            TConsCad consCad = new TConsCad();
            consCad.setVersao("2.00");

            TConsCad.InfCons infCons = new TConsCad.InfCons();
            infCons.setCNPJ("09609953000126");
            infCons.setXServ("CONS-CAD");
            infCons.setUF(TUfCons.valueOf(config.getEstado().toString()));

            consCad.setInfCons(infCons);

            TRetConsCad retorno = Nfe.consultaCadastro(consCad, true);

            if (retorno.getInfCons().getCStat().equals("111")) {
                System.out.println("Razão Social: " + retorno.getInfCons().getInfCad().get(0).getXNome());
                System.out.println("Cnpj:" + retorno.getInfCons().getInfCad().get(0).getCNPJ());
                System.out.println("Ie:" + retorno.getInfCons().getInfCad().get(0).getIE());
            } else {
                System.err.println(retorno.getInfCons().getCStat() + " - " + retorno.getInfCons().getXMotivo());
            }
        } catch (NfeException e) {
            System.out.println(e.getMessage());
        }
    }*/
}
