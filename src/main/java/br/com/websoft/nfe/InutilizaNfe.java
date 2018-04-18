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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;


/**
 *
 * @author JW Junior
 */
public class InutilizaNfe {

    /*public void inutilizar() {
        try {
            Certificado certificado = CertificadoUtil.certificadoPfx("C:\\nfe\\DADOS\\EMPR0001\\certificado.pfx", "1234");
            ConfiguracoesIniciaisNfe config = ConfiguracoesIniciaisNfe.iniciaConfiguracoes(Estados.PR, ConstantesUtil.AMBIENTE.HOMOLOGACAO,
                    certificado, "C:\\nfee\\Schemas", ConstantesUtil.VERSAO.V3_10);
            config = ConfiguracoesIniciaisNfe.getInstance();

            String id = "ID41170960995300012655001000000003000000003";

            TInutNFe inutNFe = new TInutNFe();
            inutNFe.setVersao("3.10");

            InfInut infInut = new TInutNFe.InfInut();
            infInut.setId(id);
            infInut.setTpAmb(config.getAmbiente());
            infInut.setXServ("INUTILIZAR");
            infInut.setCUF("41");
            infInut.setAno("17");

            infInut.setCNPJ("09609953000126");
            infInut.setMod("55");
            infInut.setSerie("1");

            infInut.setNNFIni("3");
            infInut.setNNFFin("3");

            infInut.setXJust("Teste de Inutilização da Nfe");
            inutNFe.setInfInut(infInut);

            TRetInutNFe retorno = Nfe.inutilizacao(inutNFe, true, ConstantesUtil.NFE);
            br.inf.portalfiscal.nfe.schema.retinutnfe.TRetInutNFe.InfInut infRetorno = retorno.getInfInut();
            System.out.println("Status:" + infRetorno.getCStat());
            System.out.println("Motivo:" + infRetorno.getXMotivo());
            System.out.println("Data:" + infRetorno.getDhRecbto());
        } catch (NfeException e) {
            System.out.println("Erro: "+e.getMessage());
        }
    }*/

}
