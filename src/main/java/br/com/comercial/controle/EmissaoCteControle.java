/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.entidade.ParametrosCte;
import br.com.comercial.facade.ParametrosCteFacade;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.cte.Cte;
import br.com.samuelweb.cte.dom.StatusEnum;
import br.com.samuelweb.cte.exception.CteException;
import br.com.samuelweb.cte.util.ObjetoUtil;
import br.com.samuelweb.cte.util.XmlUtil;
import br.com.websoft.nfe.EstadoCod;
import br.inf.portalfiscal.cte.schema_300.cteModalRodoviario.Rodo;
import br.inf.portalfiscal.cte.schema_300.enviCTe.TEnviCTe;
import br.inf.portalfiscal.cte.schema_300.retEnviCTe.TRetEnviCTe;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;

/**
 *
 * @author junior
 */
@Named
@SessionScoped
public class EmissaoCteControle implements Serializable {

    private String cnpj;
    private String cNfu2;
    private Integer dv;
    private ParametrosCte parametrosCte;

    @Inject
    private ParametrosCteFacade parametrosCteFacade;

    public String geraChaveCte(ParametrosCte p) {
        System.out.println("Chamou o metodo gerar");
        cnpj = p.getEmitente().getCnpj();
        cnpj = cnpj.replace(" ", ""); //tira espaço em branco
        cnpj = cnpj.replace(".", ""); //tira ponto
        cnpj = cnpj.replace("/", ""); //tira barra
        cnpj = cnpj.replace("-", ""); //tira hífen
        try {

            String cUF = "41"; // Código da UF do emitente do Documento Fiscal.    
            String dataAAMM = new SimpleDateFormat("yyMM").format(new Date()); // Ano e Mês de emissão da NF-e.    
            String cnpjCpf = cnpj; // CNPJ do emitente.    
            String mod = "57"; // Modelo do Documento Fiscal.
            String serie = p.getSerie57(); // Série do Documento Fiscal.    
            String tpEmis = "1"; // Forma de emissão da NF-e    
            String nNF = Integer.toString(p.getNumNota57()); // Número do Documento Fiscal.    
            String cNF = lpadTo(Integer.toString(new Random().nextInt(99999999)), 8, '0'); // Código Numérico que compõe a Chave de Acesso.    
            cNfu2 = cNF;
            StringBuilder chave = new StringBuilder();
            chave.append(lpadTo(cUF, 2, '0'));
            chave.append(lpadTo(dataAAMM, 4, '0'));
            chave.append(lpadTo(cnpjCpf.replaceAll("\\D", ""), 14, '0'));
            chave.append(lpadTo(mod, 2, '0'));
            chave.append(lpadTo(serie, 3, '0'));
            chave.append(lpadTo(String.valueOf(nNF), 9, '0'));
            chave.append(lpadTo(tpEmis, 1, '0'));
            chave.append(lpadTo(cNF, 8, '0'));
            dv = modulo11(chave.toString());
            chave.append(modulo11(chave.toString()));
            //emissao.setChave(chave.toString());
            chave.insert(0, "CTe");

            System.out.println("Chave Ct-e: " + chave.toString());
            return chave.toString();
        } catch (Exception e) {
            e.getStackTrace();
            return "";
        }
    }

    public static int modulo11(String chave) {
        int total = 0;
        int peso = 2;

        for (int i = 0; i < chave.length(); i++) {
            total += (chave.charAt((chave.length() - 1) - i) - '0') * peso;
            peso++;
            if (peso == 10) {
                peso = 2;
            }
        }
        int resto = total % 11;
        return (resto == 0 || resto == 1) ? 0 : (11 - resto);
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

    public void emitirCte() throws JAXBException, CteException, InterruptedException {

        parametrosCte = parametrosCteFacade.listar().get(0);
        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe cte = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe();
        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte infCTe = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte();

        infCTe.setId(geraChaveCte(parametrosCte));
        infCTe.setVersao("3.00");

        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Ide ide = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Ide();
        ide.setCUF(EstadoCod.codEstado(parametrosCte.getEmitente().getCidade().getEstado().getSigla()));
        ide.setCCT(cNfu2);
        ide.setCFOP("5351");
        ide.setNatOp("TRANSPORTE INTERNO");
        ide.setMod("57");
        ide.setSerie("1");
        ide.setNCT(parametrosCte.getNumNota57().toString());
        ide.setDhEmi(XmlUtil.dataCte(LocalDateTime.now()));
        ide.setTpImp("1");
        ide.setTpEmis("1");
        ide.setCDV(dv.toString());
        ide.setTpAmb(parametrosCte.getTipoEmi());
        ide.setTpCTe("0");
        ide.setProcEmi("0");
        ide.setVerProc("1.0");
        ide.setCMunEnv(parametrosCte.getEmitente().getCodMunicipio());
        ide.setXMunEnv(parametrosCte.getEmitente().getCidade().getNome());
        ide.setUFEnv(br.inf.portalfiscal.cte.schema_300.enviCTe.TUf.valueOf(parametrosCte.getEmitente().getCidade().getEstado().getSigla()));
        ide.setModal("01");
        ide.setTpServ("0");
        ide.setCMunIni(parametrosCte.getEmitente().getCodMunicipio());
        ide.setXMunIni(parametrosCte.getEmitente().getCidade().getNome());
        ide.setUFIni(br.inf.portalfiscal.cte.schema_300.enviCTe.TUf.valueOf(parametrosCte.getEmitente().getCidade().getEstado().getSigla()));
        ide.setCMunFim(parametrosCte.getEmitente().getCodMunicipio());
        ide.setXMunFim(parametrosCte.getEmitente().getCidade().getNome());
        ide.setUFFim(br.inf.portalfiscal.cte.schema_300.enviCTe.TUf.valueOf(parametrosCte.getEmitente().getCidade().getEstado().getSigla()));
        ide.setRetira("1");
        ide.setIndIEToma("9");

        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Ide.Toma3 toma3 = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Ide.Toma3();
        toma3.setToma("3");
        ide.setToma3(toma3);
        infCTe.setIde(ide);

        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Compl compl = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Compl();
        compl.setXObs("FORMA DE PAGAMENTO DEPOSITO BANCARIO,FAVOR ENVIAR COPIA DO COMPROVANTE PARA O TRANSPORTADOR E ANEXAR VIA A DA CONTABILIDADE");
        infCTe.setCompl(compl);

        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Emit emit = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Emit();
        emit.setCNPJ(cnpj);
        String ie = parametrosCte.getEmitente().getIe();
        ie = ie.replaceAll("-", "");
        ie = ie.replaceAll(".", "");
        ie = ie.replaceAll("/", "");
        ie = ie.replaceAll(" ", "");
        emit.setIE("9044301536");
        emit.setXFant(parametrosCte.getEmitente().getFantasia());
        emit.setXNome(parametrosCte.getEmitente().getRazao());

        br.inf.portalfiscal.cte.schema_300.enviCTe.TEndeEmi enderEmit = new br.inf.portalfiscal.cte.schema_300.enviCTe.TEndeEmi();
        enderEmit.setXLgr(parametrosCte.getEmitente().getRua());
        enderEmit.setNro(parametrosCte.getEmitente().getNumero());
        enderEmit.setXCpl(parametrosCte.getEmitente().getComplemento());
        enderEmit.setXBairro(parametrosCte.getEmitente().getBairro());
        enderEmit.setCMun(parametrosCte.getEmitente().getCodMunicipio());
        enderEmit.setXMun(parametrosCte.getEmitente().getCidade().getNome());
        enderEmit.setUF(br.inf.portalfiscal.cte.schema_300.enviCTe.TUFSemEX.valueOf(parametrosCte.getEmitente().getCidade().getEstado().getSigla()));
        String cep = parametrosCte.getEmitente().getCep();
        cep = cep.replace("-", "");
        cep = cep.replace(" ", "");
        cep = cep.replace(".", "");
        enderEmit.setCEP(cep);
        String fone = parametrosCte.getEmitente().getFone();
        fone = fone.replace("(", "");
        fone = fone.replace(")", "");
        fone = fone.replace("-", "");
        fone = fone.replace(" ", "");
        enderEmit.setFone(fone);
        
        emit.setEnderEmit(enderEmit);
        infCTe.setEmit(emit);
        
        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Rem rem = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Rem();
        rem.setCNPJ("");
        rem.setIE("");
        rem.setXNome("CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
        rem.setEmail("jwjuniorsi@gmail.com");
        
        br.inf.portalfiscal.cte.schema_300.enviCTe.TEndereco enderRem = new br.inf.portalfiscal.cte.schema_300.enviCTe.TEndereco();
        enderRem.setXLgr("Rua: Teste");
        enderRem.setNro("0");
        enderRem.setXBairro("TESTE");
        enderRem.setCMun("4115200");
        enderRem.setXMun("Maringa");
        enderRem.setUF(br.inf.portalfiscal.cte.schema_300.enviCTe.TUf.PR);
        enderRem.setCEP("86010390");
        enderRem.setCPais("1058");
        enderRem.setXPais("Brasil");
        rem.setEnderReme(enderRem);
        infCTe.setRem(rem);
        
        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Dest dest = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Dest();
        dest.setCNPJ("");
        dest.setIE("");
        dest.setXNome("CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
        dest.setEmail("samuel@autocomsistemas.com.br");

        br.inf.portalfiscal.cte.schema_300.enviCTe.TEndereco enderDest = new br.inf.portalfiscal.cte.schema_300.enviCTe.TEndereco();
        enderDest.setXLgr("Rua: Teste");
        enderDest.setNro("0");
        enderDest.setXBairro("TESTE");
        enderDest.setCMun("4115200");
        enderDest.setXMun("Maringa");
        enderDest.setUF(br.inf.portalfiscal.cte.schema_300.enviCTe.TUf.PR);
        enderDest.setCEP("86010390");
        enderDest.setCPais("1058");
        enderDest.setXPais("Brasil");
        dest.setEnderDest(enderDest);
        infCTe.setDest(dest);
        
        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.VPrest prest = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.VPrest();
        prest.setVTPrest("410.00");
        prest.setVRec("410.00");
        infCTe.setVPrest(prest);
        
        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Imp imp = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.Imp();

        br.inf.portalfiscal.cte.schema_300.enviCTe.TImp icms = new br.inf.portalfiscal.cte.schema_300.enviCTe.TImp();

        br.inf.portalfiscal.cte.schema_300.enviCTe.TImp.ICMS45 icms45 = new br.inf.portalfiscal.cte.schema_300.enviCTe.TImp.ICMS45();
        icms45.setCST("40");
        icms.setICMS45(icms45);
        
        imp.setICMS(icms);
        infCTe.setImp(imp);
        
        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm infCTeNorm = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm();

        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfCarga infCarga = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfCarga();
        infCarga.setVCarga("29100.00");
        infCarga.setProPred("OLEO DIESEL");

        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfCarga.InfQ infQ = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfCarga.InfQ();
        infQ.setCUnid("04");
        infQ.setTpMed("LT");
        infQ.setQCarga("10000.0000");
        infCarga.getInfQ().add(infQ);
        
        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfDoc infDoc = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfDoc();
        
        br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfDoc.InfNFe infNFe = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfDoc.InfNFe();
        infNFe.setChave("41180409609953000126550010000001081379047450");
        infDoc.getInfNFe().add(infNFe);
        
         br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfModal infModal = new br.inf.portalfiscal.cte.schema_300.enviCTe.TCTe.InfCte.InfCTeNorm.InfModal();
        infModal.setVersaoModal("3.00");
        
        br.inf.portalfiscal.cte.schema_300.cteModalRodoviario.Rodo rodo = new br.inf.portalfiscal.cte.schema_300.cteModalRodoviario.Rodo();
        rodo.setRNTRC("ISENTO");

        infModal.setAny(ObjetoUtil.objectToElement(rodo, Rodo.class, "rodo"));

        infCTeNorm.setInfCarga(infCarga);
        infCTeNorm.setInfDoc(infDoc);
        infCTeNorm.setInfModal(infModal);

        infCTe.setInfCTeNorm(infCTeNorm);
        
        // Monta EnviCte
        TEnviCTe enviCTe = new TEnviCTe();
        enviCTe.setVersao("3.00");
        enviCTe.setIdLote("1");

        cte.setInfCte(infCTe);
        enviCTe.getCTe().add(cte);

        // MOnta e Assina o XML
        enviCTe = Cte.montaCte(enviCTe, true);

        // Envia a Cte para a Sefaz
        TRetEnviCTe retorno = Cte.enviarCte(enviCTe);
        if (!retorno.getCStat().equals(StatusEnum.LOTE_RECEBIDO.getCode())) {
            throw new CteException("Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
        }

        String recibo = retorno.getInfRec().getNRec();

        br.inf.portalfiscal.cte.schema_300.retConsReciCTe.TRetConsReciCTe retornoCte;
        while (true) {
            retornoCte = Cte.consultaRecibo(recibo);
            if (retornoCte.getCStat().equals("105")) {
                System.out.println("Lote Em Processamento, vai tentar novamente apos 2 Segundo.");
                Thread.sleep(2000);
            } else {
                break;
            }
        }

        if (!retornoCte.getCStat().equals(StatusEnum.LOTE_PROCESSADO.getCode())) {
            throw new CteException("Status:" + retornoCte.getCStat() + " - " + retornoCte.getXMotivo());
        }

        System.out.println("Status: " + retornoCte.getProtCTe().get(0).getInfProt().getCStat() + " - " + retornoCte.getProtCTe().get(0).getInfProt().getXMotivo());
        System.out.println("Data: " + retornoCte.getProtCTe().get(0).getInfProt().getDhRecbto());

        if (retornoCte.getProtCTe().get(0).getInfProt().getCStat().equals(StatusEnum.AUTORIZADO.getCode())) {
            System.out.println("Protocolo: " + retornoCte.getProtCTe().get(0).getInfProt().getNProt());
            System.out.println("XML Final: " + XmlUtil.criaCteProc(enviCTe, retornoCte.getProtCTe().get(0)));
        }

    

    

}
}    
