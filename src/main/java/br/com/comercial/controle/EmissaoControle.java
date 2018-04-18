/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import static br.com.comercial.controle.EmissaoControle65.removeAcentos;
import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.Emissao;
import br.com.comercial.entidade.Emissao65;
import br.com.comercial.entidade.ItemEmissao;
import br.com.comercial.entidade.Lacres;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.entidade.Produto;
import br.com.comercial.entidade.ReboqueTrans;
import br.com.comercial.facade.EmissaoFacade;
import br.com.comercial.facade.ParametrosFiscaisFacade;
import br.com.comercial.facade.ProdutoFacade;
import br.com.comercial.util.NfeCalculo;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.dom.Enum.StatusEnum;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.exception.NfeValidacaoException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.NFCeUtil;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.com.websoft.nfe.Configuracao;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnderEmi;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEndereco;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Dest;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISOutr;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.ImpostoDevol.IPI;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Prod;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Emit;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Ide;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.InfAdic;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Pag;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Total;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Total.ICMSTot;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Transp;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Transp.Transporta;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TUf;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TUfEmi;
import br.inf.portalfiscal.nfe.schema_4.retConsReciNFe.TRetConsReciNFe;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author JW Junior
 */
@Named
@SessionScoped
public class EmissaoControle implements Serializable {

    @Inject
    private EmissaoFacade emissaoFacade;
    @Inject
    private ProdutoFacade produtoFacade;
    
    private Emissao emissao;
    private ReboqueTrans reboqueTrans;
    private Lacres lacres;
    private ItemEmissao itemEmissao;
    private ConverterGenerico converterGenerico;
    private ParametrosFiscais parametrosFiscais;
    @Inject
    private ParametrosFiscaisFacade parametrosFiscaisFacade;

    private String cnpj;
    private String cNfu2;
    private String xml;
    private String caminho;
    private String mes;
    private String ano;
    private String caminhoDownload;
    private Integer dv2 = 5;
    private Integer quant;
    private Boolean ativaDownload = false;
    private StreamedContent file;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(emissaoFacade);
        }
        return converterGenerico;
    }

    public void novo() {
        emissao = new Emissao();
        parametrosFiscais = new ParametrosFiscais();
        parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
        emissao.setEmitente(parametrosFiscais.getEmitente());
        itemEmissao = new ItemEmissao();
        emissao.setStatus("EM DIGITAÇÃO");
        emissao.setModelo("55");
        emissao.setNumNfe(parametrosFiscais.getNumNota55());
        itemEmissao.setEmissao(emissao);
        reboqueTrans = new ReboqueTrans();
        reboqueTrans.setEmissao(emissao);
        lacres = new Lacres();
        lacres.setEmissao(emissao);
        valoresPadraoNfe();
        valoresPadraoItem();

    }

    public String salvar() throws Exception {
        System.out.println("chamou o metodo");
//        try {
//          if(emissao.getTipo().equals("")){
//              FacesContext.getCurrentInstance().
//                    addMessage(null, new 
//        FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "O Campo Tipo é obrigatório.") );
//          } else {
        System.out.println("Iniciou o metodo");
        emiteNota(emissao);
        System.out.println("saiu do metodo");
        emissaoFacade.salvar(emissao);
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Status de Tranmissão", "Testo da Transmissão!"));
        return "list.xhtml";
//        }      
//                
////               
////            }
//        } catch (Exception ex) {
//            FacesContext.getCurrentInstance().
//                    addMessage(null, new 
//        FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Contate o Suporte (44)99812-9931") );
//            
//        }
//        return "";
    }

    public void editar(Emissao e) {
        emissao = e;
    }

    public void excluir(Emissao e) throws Exception {
        try {
            emissaoFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }

    }

    public void atlzCampProd() {
        if (itemEmissao.getProduto() != null) {
            itemEmissao.setCfop(itemEmissao.getProduto().getCfop());
            itemEmissao.setVlrUnit(itemEmissao.getProduto().getPrecoVenda());
            itemEmissao.setVlrProd(itemEmissao.getProduto().getPrecoVenda());
            itemEmissao.setPeso(itemEmissao.getProduto().getPeso());
            itemEmissao.setIcmsCsosn(itemEmissao.getProduto().getIcmsCsosn());
            itemEmissao.setIcmsRed(itemEmissao.getProduto().getIcmsPred());
        }
    }

    public void defineTributacao() throws Exception {
        NfeCalculo calculo = NfeCalculoController.calculo(itemEmissao, emissao.getEmitente(), emissao.getCliente());

        //VALORES ICMS
        itemEmissao.setIcmsBc(calculo.getBaseCalculoIcms());
        itemEmissao.setIcmsVlr(calculo.getValorIcms());

        //VALORES IPI
        itemEmissao.setIpiBc(calculo.getBaseCalculoIpi());
        itemEmissao.setIpiVlr(calculo.getValorIpi());

        //VALORES PIS
        itemEmissao.setPisBc(calculo.getBaseCalculoPis());
        itemEmissao.setPisVlr(calculo.getValorPis());

        //VALORES COFINS
        itemEmissao.setCofinsBc(calculo.getBaseCalculoCofins());
        itemEmissao.setCofinsVlr(calculo.getValorCofins());
    }

    public void atualizaTotais() {
        BigDecimal totalProdutos = BigDecimal.ZERO;
        BigDecimal valorFrete = BigDecimal.ZERO;
        BigDecimal valorSeguro = BigDecimal.ZERO;
        BigDecimal valorOutrasDespesas = BigDecimal.ZERO;
        BigDecimal desconto = BigDecimal.ZERO;
        BigDecimal baseCalculoIcms = BigDecimal.ZERO;
        BigDecimal valorIcms = BigDecimal.ZERO;
        BigDecimal baseCalculoIcmsSt = BigDecimal.ZERO;
        BigDecimal valorIcmsSt = BigDecimal.ZERO;
        BigDecimal valorIpi = BigDecimal.ZERO;
        BigDecimal valorPis = BigDecimal.ZERO;
        BigDecimal valorCofins = BigDecimal.ZERO;
        BigDecimal valorNotaFiscal = BigDecimal.ZERO;

        BigDecimal totalServicos = BigDecimal.ZERO;
        BigDecimal baseCalculoIssqn = BigDecimal.ZERO;
        BigDecimal valorIssqn = BigDecimal.ZERO;
        BigDecimal valorPisIssqn = BigDecimal.ZERO;
        BigDecimal valorCofinsIssqn = BigDecimal.ZERO;

        for (ItemEmissao it : emissao.getListaProd()) {
            totalProdutos = totalProdutos.add(it.getVlrProd().multiply(it.getItemQuant()));
            desconto = desconto.add(it.getVlrDesconto());

            if (it.getIcmsBc() != null) {
                baseCalculoIcms = baseCalculoIcms.add(it.getIcmsBc());
            }
            if (it.getIcmsVlr() != null) {
                valorIcms = valorIcms.add(it.getIcmsVlr());
            }
            if (it.getPisVlr() != null) {
                valorPis = valorPis.add(it.getPisVlr());
            }
            if (it.getIpiVlr() != null) {
                valorIpi = valorIpi.add(it.getIpiVlr());
            }
            if (it.getCofinsVlr() != null) {
                valorCofins = valorCofins.add(it.getCofinsVlr());
            }
        }

        valorNotaFiscal = totalProdutos.add(valorIpi).add(valorOutrasDespesas).subtract(desconto);
        emissao.setVlrProdNf(totalProdutos);
        emissao.setIcmsBc(baseCalculoIcms);
        emissao.setIcmsVlr(valorIcms);
        emissao.setIpiVlr(valorIpi);
        emissao.setPisVlr(valorPis);

        emissao.setVlrTotalNf(valorNotaFiscal);

    }

    public void valoresPadraoNfe() {

        emissao.setSerie("1");
        emissao.setIcmsBc(BigDecimal.ZERO);
        emissao.setIcmsVlr(BigDecimal.ZERO);
        emissao.setIpiBc(BigDecimal.ZERO);
        emissao.setIpiVlr(BigDecimal.ZERO);
        emissao.setPisBc(BigDecimal.ZERO);
        emissao.setPisVlr(BigDecimal.ZERO);
        emissao.setCofinsBc(BigDecimal.ZERO);
        emissao.setCofinsVlr(BigDecimal.ZERO);

        emissao.setVlrProdNf(BigDecimal.ZERO);
        emissao.setVlrDescNf(BigDecimal.ZERO);
        emissao.setVlrTotalNf(BigDecimal.ZERO);

    }

    public void valoresPadraoItem() {
        //itemEmissao.setQuant(0);
        itemEmissao.setVlrUnit(BigDecimal.ZERO);
        itemEmissao.setVlrProd(BigDecimal.ZERO);
        itemEmissao.setVlrDesconto(BigDecimal.ZERO);
        itemEmissao.setVlrTotal(BigDecimal.ZERO);
        itemEmissao.setIcmsBc(BigDecimal.ZERO);
        itemEmissao.setIcmsPorc(0);
        itemEmissao.setIcmsRed(BigDecimal.ZERO);
        itemEmissao.setIcmsVlr(BigDecimal.ZERO);
        itemEmissao.setIpiBc(BigDecimal.ZERO);
        itemEmissao.setIpiVlr(BigDecimal.ZERO);
        itemEmissao.setIpiPorc(0);
        itemEmissao.setPisBc(BigDecimal.ZERO);
        itemEmissao.setPisVlr(BigDecimal.ZERO);
        itemEmissao.setPisPorc(BigDecimal.ZERO);
        itemEmissao.setCofinsBc(BigDecimal.ZERO);
        itemEmissao.setCofinsVlr(BigDecimal.ZERO);
        itemEmissao.setCofinsPorc(BigDecimal.ZERO);
    }

    public void addItem()  {
        try {
        emissao.getListaProd().add(itemEmissao);
        defineTributacao();
        atualizaTotais();
        itemEmissao = new ItemEmissao();
        itemEmissao.setEmissao(emissao);
        valoresPadraoItem();
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    public void addReboque() {
        emissao.getReboques().add(reboqueTrans);
        reboqueTrans = new ReboqueTrans();
        reboqueTrans.setEmissao(emissao);
    }
    
    public void removeReboque(ReboqueTrans r){
        emissao.getReboques().remove(r);
    }
    
    public void addLacre() {
        emissao.getListaLacres().add(lacres);
        lacres = new Lacres();
        lacres.setEmissao(emissao);
    }
    
    public void removeLacre(Lacres l){
        emissao.getListaLacres().remove(l);
    }

    public String geraChaveNfe(ParametrosFiscais p) {
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
            String mod = "55"; // Modelo do Documento Fiscal.    
            String serie = "1"; // Série do Documento Fiscal.    
            String tpEmis = "1"; // Forma de emissão da NF-e    
            String nNF = Integer.toString(p.getNumNota55()); // Número do Documento Fiscal.    
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
            dv2 = modulo11(chave.toString());
            chave.append(modulo11(chave.toString()));
            emissao.setChave(chave.toString());
            chave.insert(0, "NFe");

            System.out.println("Chave NF-e: " + chave.toString());
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

    public void emiteNota(Emissao e) {
        System.out.println("Chamou metodo emitir");
        try {
            parametrosFiscais = new ParametrosFiscais();
            parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
            if (parametrosFiscais == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Emitir NFCe", "Dados do Emitente não localizados!"));
            } else {

                Date data = new Date();
                /*SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-ddT HH:mm:ss-02:00");
        String dataFormatada = formato.format(data);*/

                SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
                String dataFormatada1 = formato2.format(data);
                SimpleDateFormat formato3 = new SimpleDateFormat("HH:mm:ss");
                String dataFormatada2 = formato3.format(data);
                SimpleDateFormat testedata2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                String formato7 = testedata2.format(data);

                Configuracao.iniciaConf(emissao.getEmitente(), parametrosFiscais);
                TNFe nfe = new TNFe();
                InfNFe infNfe = new InfNFe();
                infNfe.setId(geraChaveNfe(parametrosFiscais));
                infNfe.setVersao("4.00");

                Ide ide = new Ide();
                ide.setCUF("41");
                //Adicionar numero auto incrementado a partir de nº da nota fiscal do emissor
                ide.setCNF(cNfu2);
                //Natureza da Operacao
                ide.setNatOp(emissao.getNatOp());
                //Modelo
                ide.setMod("55");
                //Serie
                ide.setSerie("1");
                //Numero da Nota Fiscal
                ide.setNNF(Integer.toString(parametrosFiscais.getNumNota55()));
                //Hora de emissao
                ide.setDhEmi(formato7);
                //Tipo de Operacao (entrada/saida)
                ide.setTpNF(emissao.getTpNf());
                //Destino da Operacao 1 Op interna 2 Op interestadual 3 Op exterior
                ide.setIdDest(emissao.getDestOp());
                //Cod Municipio
                ide.setCMunFG(emissao.getEmitente().getCodMunicipio());
                //Tipo de Impressão
                ide.setTpImp(emissao.getTpImp());
                //Tipo de emissao(normal ou contingencia)
                ide.setTpEmis("1");
                //Digito verificador
                ide.setCDV(dv2.toString());
                //1 - Produção 2 - Homologação
                ide.setTpAmb(parametrosFiscais.getTipoEmi());
                //Finalidade 1 - NF-e normal 2 - NF-e complementar 3 - NF-e de ajuste 4 - Devolução
                ide.setFinNFe("1");
                //Consumidor Final
                ide.setIndFinal("1");
                //Informar o indicador de presença do comprador no estabelecimento comercial no momento da operação:
                ide.setIndPres("1");
                //informar o código de identificação do processo de emissão da NF-e: Identificador do processo de emissão da NF-e:
                ide.setProcEmi("0");
                //Nome do Emissor
                ide.setVerProc("EnotaNFCe Grat");
                infNfe.setIde(ide);

                Emit emit = new Emit();
                emit.setCNPJ(cnpj);
                emit.setXNome(parametrosFiscais.getEmitente().getRazao());
                emit.setXFant(parametrosFiscais.getEmitente().getFantasia());
                TEnderEmi enderEmi = new TEnderEmi();
                enderEmi.setXLgr(parametrosFiscais.getEmitente().getRua());
                enderEmi.setNro(parametrosFiscais.getEmitente().getNumero());
                enderEmi.setXCpl(parametrosFiscais.getEmitente().getComplemento());
                enderEmi.setXBairro(parametrosFiscais.getEmitente().getBairro());
                //Alterar para automatico cod do municipio
                enderEmi.setCMun("4124608");
                enderEmi.setXMun(removeAcentos(parametrosFiscais.getEmitente().getCidade().getNome()));
                enderEmi.setUF(TUfEmi.PR);
                String cep = parametrosFiscais.getEmitente().getCep();
                cep = cep.replace("-", "");
                cep = cep.replace(" ", "");
                cep = cep.replace(".", "");
                enderEmi.setCEP(cep);
                enderEmi.setCPais("1058");
                enderEmi.setXPais("Brasil");
                String fone = parametrosFiscais.getEmitente().getFone();
                fone = fone.replace("(", "");
                fone = fone.replace(")", "");
                fone = fone.replace("-", "");
                fone = fone.replace(" ", "");
                enderEmi.setFone(fone);
                emit.setEnderEmit(enderEmi);
                String ie4 = emissao.getEmitente().getIe();
                ie4 = ie4.replace(" ", ""); //tira espaço em branco
                ie4 = ie4.replace(".", ""); //tira ponto
                ie4 = ie4.replace("/", ""); //tira barra
                ie4 = ie4.replace("-", ""); //tira hífen
                emit.setIE(ie4);
                emit.setCRT(parametrosFiscais.getEmitente().getCrt().toString());
                infNfe.setEmit(emit);

                //Destinatario
                int num;
                Dest dest;
                if (parametrosFiscais.getTipoEmi().equals("2")) {
                    dest = new Dest();
                    String doc = emissao.getCliente().getCpfCnpj();
                    doc = doc.replace(" ", ""); //tira espaço em branco
                    doc = doc.replace(".", ""); //tira ponto
                    doc = doc.replace("/", ""); //tira barra
                    doc = doc.replace("-", ""); //tira hífen
                    dest.setCPF("10119722984");
                    dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
                    dest.setIndIEDest("9");
                    TEndereco enderDest = new TEndereco();
                    enderDest.setXLgr(emissao.getCliente().getRua());
                    enderDest.setNro(emissao.getCliente().getNumero());
                    enderDest.setXBairro(emissao.getCliente().getBairro());
                    enderDest.setCMun(emissao.getCliente().getCodMuni());
                    enderDest.setXMun(removeAcentos(emissao.getCliente().getCidade().getNome()));
                    enderDest.setUF(TUf.valueOf(emissao.getCliente().getCidade().getEstado().getSigla()));
                    String cepD = emissao.getCliente().getCep();
                    cepD = cepD.replace(".", "");
                    cepD = cepD.replace("-", "");
                    cepD = cepD.replace(" ", "");
                    enderDest.setCEP(cepD);
                    enderDest.setCPais("1058");
                    enderDest.setXPais("Brasil");
                    String fone2 = emissao.getCliente().getFone();
                    fone2 = fone2.replace("(", "");
                    fone2 = fone2.replace(")", "");
                    fone2 = fone2.replace(" ", "");
                    fone2 = fone2.replace("-", "");
                    enderDest.setFone(fone2);
                    dest.setEnderDest(enderDest);
                    dest.setEmail(emissao.getCliente().getEmail());
                    infNfe.setDest(dest);
                    num = 2;
                    Det det = new Det();
                    det.setNItem("1");
                    Prod prod = new Prod();
                    prod.setCProd("+4059");
                    prod.setCEAN("00000011111115");
                    prod.setXProd("NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
                    prod.setNCM("84439939");
                    //Verificar com o alisson
                    //prod.setCEST(ie.getProduto().getNcm());
                    //preencher com S para produto foi fabricado em escala relevante
                    //preencher com N produto foi fabricado em escala NÃO relevante
                    //prod.setIndEscala("N");
                    prod.setCFOP("5405");
                    prod.setUCom("UN");
                    prod.setQCom("1");
                    //COnfirmar com o alisson se é o mesmo do valor do produto
                    prod.setVUnCom("0");
                    //resultado da multiplicação de qcom com vUnCom
                    prod.setVProd("0");
                    //Verificar com o alisson se é utilizado
                    prod.setCEANTrib("00000011111115");
                    prod.setUTrib("UN");
                    prod.setQTrib("1");
                    prod.setVUnTrib("0");
                    //0 - o valor do item (vProd) não compõe o valor total da NF-e 
                    //1 - o valor do item (vProd) compõe o valor total da NF-e
                    prod.setIndTot("1");
                    det.setProd(prod);
                    Imposto imposto = new Imposto();
                    ICMS icms = new ICMS();
                    ICMSSN500 icms500 = new ICMSSN500();        
                    icms500.setOrig("0");
                    icms500.setCSOSN("500");
                    icms.setICMSSN500(icms500);

                    PIS pis = new PIS();
                    PISOutr pisAliq = new PISOutr();
                    pisAliq.setCST("99");
                    pisAliq.setVBC("0.00");
                    pisAliq.setPPIS("0.00");
                    pisAliq.setVPIS("0.00");
                    pis.setPISOutr(pisAliq);

                    COFINS cofins = new COFINS();
                    COFINSOutr cofinsAliq = new COFINSOutr();
                    cofinsAliq.setCST("99");
                    cofinsAliq.setVBC("0.00");
                    cofinsAliq.setPCOFINS("0.00");
                    cofinsAliq.setVCOFINS("0.00");
                    cofins.setCOFINSOutr(cofinsAliq);
                    
                    
                    /*ICMS10 icms10 = new ICMS10();
            icms10.setOrig(ie.getProduto().getOrigem());
            //icms10.set'(chave); ie.getProduto().getIcmsCst());
            icms.setICMS20(icms20);*/

                    JAXBElement<ICMS> icmsElement = new JAXBElement<ICMS>(new QName("ICMS"), ICMS.class, icms);
                    imposto.getContent().add(icmsElement);

                    JAXBElement<PIS> pisElement = new JAXBElement<PIS>(new QName("PIS"), PIS.class, pis);
                    imposto.getContent().add(pisElement);

                    JAXBElement<COFINS> cofinsElement = new JAXBElement<COFINS>(new QName("COFINS"), COFINS.class, cofins);
                    imposto.getContent().add(cofinsElement);

                    det.setImposto(imposto);
                    infNfe.getDet().add(det);
                } else if (emissao.getCliente() != null) {
                    String cpf = emissao.getCliente().getCpfCnpj();
                    cpf = cpf.replace(" ", ""); //tira espaço em branco
                    cpf = cpf.replace(".", ""); //tira ponto
                    cpf = cpf.replace("/", ""); //tira barra
                    cpf = cpf.replace("-", ""); //tira hífen

                    dest = new Dest();
                    if (cpf.length() == 11) {
                        dest.setCPF(cpf);
                        dest.setXNome(emissao.getCliente().getNome());
                    } else if (cpf.length() == 14) {
                        dest.setCNPJ(cpf);
                        dest.setXNome(emissao.getCliente().getNome());
                    }

                    //1 - Contribuinte ICMS (informar a tag IE do destinatário);
                    //2 - Contribuinte isento de Inscrição no cadastro de Contribuintes do ICMS - não informar a tag IE;
                    //9 - Não Contribuinte, que pode ou não possuir Inscrição Estadual no Cadastro de Contribuintes do ICMS - não informar a tag IE.
                    if (emissao.getIndIEDest().equals("9")) {
                        dest.setIndIEDest("9");
                    } else if (emissao.getIndIEDest().equals("1")) {
                        dest.setIndIEDest("1");
                        dest.setIE(emissao.getCliente().getRginsEst());
                    } else if (emissao.getIndIEDest().equals("2")) {
                        dest.setIndIEDest("2");
                    }
                    TEndereco enderDest = new TEndereco();
                    enderDest.setXLgr(emissao.getCliente().getRua());
                    enderDest.setNro(emissao.getCliente().getNumero());
                    enderDest.setXBairro(emissao.getCliente().getBairro());
                    enderDest.setCMun(emissao.getCliente().getCodMuni());
                    enderDest.setXMun(removeAcentos(emissao.getCliente().getCidade().getNome()));
                    enderDest.setUF(TUf.valueOf(emissao.getCliente().getCidade().getEstado().getSigla()));
                    String cepD = emissao.getCliente().getCep();
                    cepD = cepD.replace(".", "");
                    cepD = cepD.replace("-", "");
                    cepD = cepD.replace(" ", "");
                    enderDest.setCEP(cepD);
                    enderDest.setCPais("1058");
                    enderDest.setXPais("Brasil");
                    String fone2 = emissao.getCliente().getFone();
                    fone2 = fone2.replace("(", "");
                    fone2 = fone2.replace(")", "");
                    fone2 = fone2.replace(" ", "");
                    fone2 = fone2.replace("-", "");
                    enderDest.setFone(fone2);
                    dest.setEnderDest(enderDest);
                    dest.setEmail(emissao.getCliente().getEmail());
                    infNfe.setDest(dest);
                    num = 1;

                } else {
                    dest = new TNFe.InfNFe.Dest();
                    dest.setCPF("");
                    dest.setXNome("Consumidor Final");
                    dest.setIndIEDest("9");
                    infNfe.setDest(dest);
                    num = 1;
                }

                for (ItemEmissao ie : e.getListaProd()) {
                    System.out.println("Entrou no for");
                    Det det = new Det();
                    det.setNItem(Integer.toString(num));

                    //Produto
                    Prod prod = new Prod();
                    prod.setCProd(ie.getProduto().getId().toString());
                    if (ie.getProduto().getCodBarras().equals("")) {
                        prod.setCEAN("");
                        prod.setCEANTrib("");
                    } else {
                        prod.setCEAN(ie.getProduto().getCodBarras());
                        prod.setCEANTrib(ie.getProduto().getCodBarras());
                    }
                    prod.setXProd(ie.getProduto().getNome());
                    prod.setNCM(ie.getProduto().getNcm());
                    //Verificar com o alisson
                    //prod.setCEST(ie.getProduto().getNcm());
                    //preencher com S para produto foi fabricado em escala relevante
                    //preencher com N produto foi fabricado em escala NÃO relevante
                    //prod.setIndEscala("N");
                    prod.setCFOP(ie.getCfop());
                    prod.setUCom(ie.getProduto().getUnidade());
                    prod.setQCom(ie.getItemQuant().toString());
                    //COnfirmar com o alisson se é o mesmo do valor do produto
                    prod.setVUnCom(ie.getProduto().getPrecoVenda().toString());
                    //resultado da multiplicação de qcom com vUnCom
                    prod.setVProd(ie.getProduto().getPrecoVenda().multiply(ie.getItemQuant()).toString());
                    //Verificar com o alisson se é utilizado

                    prod.setUTrib(ie.getProduto().getUnidade());
                    prod.setQTrib(ie.getItemQuant().toString());
                    prod.setVUnTrib(ie.getProduto().getPrecoVenda().toString());
                    //0 - o valor do item (vProd) não compõe o valor total da NF-e 
                    //1 - o valor do item (vProd) compõe o valor total da NF-e
                    prod.setIndTot("1");
                    det.setProd(prod);

                    Imposto imposto = new Imposto();
                    ICMS icms = new ICMS();
                    //ICMS
                    if (ie.getProduto().getIcmsCsosn().equals("500")) {
                        ICMSSN500 icms500 = new ICMSSN500();
                        icms500.setOrig(ie.getProduto().getOrigem());
                        icms500.setCSOSN("500");
                        icms.setICMSSN500(icms500);
                    } else if (ie.getProduto().getIcmsCsosn().equals("102")) {
                        ICMSSN102 icmssn102 = new ICMSSN102();
                        icmssn102.setOrig(ie.getProduto().getOrigem());
                        icmssn102.setCSOSN("102");
                        icms.setICMSSN102(icmssn102);

                    }
                    //PIS
                    PIS pis = null;
                    if (ie.getProduto().getPisCst().equals("99")) {
                        pis = new PIS();
                        PISOutr pISOutr = new PISOutr();
                        pISOutr.setCST(ie.getProduto().getPisCst());
                        pISOutr.setVBC("0.00");
                        pISOutr.setPPIS("0.00");
                        pISOutr.setVPIS("0.00");
                        pis.setPISOutr(pISOutr);
                    }
                    //COFINS
                    COFINS cofins = null;
                    if (ie.getProduto().getCofinsCst().equals("99")) {
                        cofins = new COFINS();
                        COFINSOutr cOFINSOutr = new COFINSOutr();
                    cOFINSOutr.setCST(ie.getProduto().getCofinsCst());
                        cOFINSOutr.setVBC("0.00");
                        cOFINSOutr.setPCOFINS("0.00");
                        cOFINSOutr.setVCOFINS("0.00");
                        cofins.setCOFINSOutr(cOFINSOutr);
                    }

                    /*ICMS10 icms10 = new ICMS10();
            icms10.setOrig(ie.getProduto().getOrigem());
            //icms10.set'(chave); ie.getProduto().getIcmsCst());
            icms.setICMS20(icms20);*/
                    JAXBElement<ICMS> icmsElement = new JAXBElement<ICMS>(new QName("ICMS"), ICMS.class, icms);
                    imposto.getContent().add(icmsElement);

                    JAXBElement<PIS> pisElement = new JAXBElement<PIS>(new QName("PIS"), PIS.class, pis);
                    imposto.getContent().add(pisElement);

                    JAXBElement<COFINS> cofinsElement = new JAXBElement<COFINS>(new QName("COFINS"), COFINS.class, cofins);
                    imposto.getContent().add(cofinsElement);

                    det.setImposto(imposto);
                    infNfe.getDet().add(det);
                    num++;
                }
                System.out.println("Terminou o for");
                //Totais
                Total total = new Total();
                ICMSTot icmsTot = new ICMSTot();
                icmsTot.setVBC(emissao.getIcmsBc().toString());
                icmsTot.setVICMS(emissao.getIcmsVlr().toString());
                //Valor do ICMS da desoneração
                icmsTot.setVICMSDeson("0.00");
                //Valor do FCP
                icmsTot.setVFCP("0.00");
                //Valor da Base de Cálculo do FCP retido por Substituição Tributária
                icmsTot.setVFCPST("0.00");
                //Valor do FCP retido anteriormente por Substituição Tributária
                icmsTot.setVFCPSTRet("0.00");
                //
                icmsTot.setVBCST("0.00");
                icmsTot.setVST("0.00");
                icmsTot.setVProd(emissao.getVlrTotalNf().add(emissao.getVlrDescNf()).toString());
                icmsTot.setVFrete("0.00");
                icmsTot.setVSeg("0.00");
                icmsTot.setVDesc(emissao.getVlrDescNf().toString());
                icmsTot.setVII("0.00");
                icmsTot.setVIPI("0.00");
                icmsTot.setVIPIDevol("0.00");
                icmsTot.setVPIS("0.00");
                icmsTot.setVCOFINS("0.00");
                icmsTot.setVOutro("0.00");
                icmsTot.setVTotTrib("0.00");
                icmsTot.setVNF(emissao.getVlrTotalNf().toString());
                total.setICMSTot(icmsTot);
                infNfe.setTotal(total);
                System.out.println("Terminou o total");
                Transp transp = new Transp();
                /*0-Contratação do Frete por conta do Remetente (CIF);
            1-Contratação do Frete por conta do Destinatário (FOB);
            2-Contratação do Frete por conta de Terceiros;
            3-Transporte Próprio por conta do Remetente;
            4-Transporte Próprio por conta do Destinatário;
            9-Sem Ocorrência de Transporte.*/
                if(emissao.getModalidadeFrete().equals("9")){
                    transp.setModFrete(emissao.getModalidadeFrete());
                } else {
                    transp.setModFrete(emissao.getModalidadeFrete());
                    Transporta tp = new Transporta();
                    tp.setXNome(emissao.getTransportadora().getRazao());
                    tp.setXEnder(emissao.getTransportadora().getRua());
                    tp.setXMun(emissao.getTransportadora().getCidade().getNome());
                    tp.setUF(TUf.valueOf(emissao.getTransportadora().getCidade().getEstado().getSigla()));
                    String tIe = emissao.getTransportadora().getIe();
                    tIe = tIe.replaceAll(".", "");
                    tIe = tIe.replaceAll("/", "");
                    tIe = tIe.replaceAll("-", "");
                    tIe = tIe.replaceAll(" ", "");
                    tp.setIE(tIe);
                    String tCnpj = emissao.getTransportadora().getCnpj();
                    tCnpj = tCnpj.replaceAll(".", "");
                    tCnpj = tCnpj.replaceAll("-", "");
                    tCnpj = tCnpj.replaceAll("/", "");
                    tCnpj = tCnpj.replaceAll(" ", "");
                    tp.setCNPJ(tCnpj);
                    transp.setTransporta(tp);
                }
                
                infNfe.setTransp(transp);
                System.out.println("Terminou a transportadora");
                InfAdic infAdic = new InfAdic();
                //Adicionar informações adicionais
                infAdic.setInfCpl("DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL NAO GERA DIREITO A CRÉDITO FISCAL IPI.");
                infNfe.setInfAdic(infAdic);

                Pag pag = new Pag();
                Pag.DetPag detPag = new Pag.DetPag();
                /*01=Dinheiro 02=Cheque 03=Cartão de Crédito 04=Cartão de Débito
        05 = Crédito Loja
        10=Vale Alimentação
        11=Vale Refeição
        12=Vale Presente
        13=Vale Combustível
        99=Outros*/
                detPag.setTPag("01");
                //Campo opcional
                detPag.setVPag(emissao.getVlrTotalNf().toString());
                pag.getDetPag().add(detPag);

                infNfe.setPag(pag);
                System.out.println("Terminou o pagamento");
                nfe.setInfNFe(infNfe);

                //Monta EnviNfe
                TEnviNFe enviNFe = new TEnviNFe();
                enviNFe.setVersao("4.00");
                //Adicionar numero incremental
                enviNFe.setIdLote("1");
                enviNFe.setIndSinc("1");
                enviNFe.getNFe().add(nfe);
                System.out.println("Terminou o xml");
                //Monta e Assina o XML
                try {
                    System.out.println("entrou no try");
                    enviNFe = Nfe.montaNfe(enviNFe, true);
                    System.out.println("Montou xml");
                } catch (NfeValidacaoException nexc) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Gerar XML para receita",
                                    nexc.getMessage()));
                    System.out.println(nexc.getMessage());
                    return;
                }
               
              

            // Envia a Nfe para a Sefaz
            TRetEnviNFe retorno = Nfe.enviarNfe(enviNFe, ConstantesUtil.NFE);

            if (!retorno.getCStat().equals(StatusEnum.LOTE_PROCESSADO.getCodigo())) {
                throw new NfeException("Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
            }

            if (!retorno.getProtNFe().getInfProt().getCStat().equals(StatusEnum.AUTORIZADO.getCodigo())) {
                throw new NfeException("Status:" + retorno.getProtNFe().getInfProt().getCStat() + " - Motivo:" + retorno.getProtNFe().getInfProt().getXMotivo());
            }

            System.out.println("Status:" + retorno.getProtNFe().getInfProt().getCStat());
            System.out.println("Motivo:" + retorno.getProtNFe().getInfProt().getXMotivo());
            System.out.println("Data:" + retorno.getProtNFe().getInfProt().getDhRecbto());
            System.out.println("Protocolo:" + retorno.getProtNFe().getInfProt().getNProt());

                //System.out.println("Xml Final :" + XmlUtil.criaNfeProc(enviNFe, retorno.getProtNFe()));
                xml = XmlUtil.criaNfeProc(enviNFe, retorno.getProtNFe());
                parametrosFiscais.setNumNota55(parametrosFiscais.getNumNota55() + 1);
                try {
                    parametrosFiscaisFacade.salvar(parametrosFiscais);
                } catch (Exception ex) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na Transmissão",
                                    "Não foi possivel salvar o numero da NF-e"));
                    ex.getMessage();
                }
                emissao.setStatus(retorno.getProtNFe().getInfProt().getXMotivo());
                try {
                    criaXml(emissao);
                } catch (IOException ex) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Gerar XML",
                                    "Não foi possivel gerar o xml"));
                }
                emissao.setCaminho(caminho);
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "NF-e Transmitida", retorno.getProtNFe().getInfProt().getXMotivo()));

            }
        } catch (NfeException | JAXBException | CertificadoException exs) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Transmitir  NFC-e",
                            exs.getMessage()));
            System.out.println("Erro:" + exs.getMessage());
        }
    }

    public static String removeAcentos(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }
     
    public String criaXml(Emissao e) throws IOException {
        System.out.println("cria xml");
        // Create file 
        FileWriter fstream = new FileWriter(criaDiretorio() + "/" + e.getChave() + ".xml");
        caminho = criaDiretorio() + "/" + e.getChave() + ".xml";
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
                + emissao.getEmitente().getCnpj() + "/xml/" + ano + "/" + mes +"/55/");
        if (!diretorioMes.exists()) {
            diretorioMes.mkdirs();
        }

        return diretorioMes.toString();
    }

    public void testaData() {
        System.out.println("Chamou o metodo");
        Date data = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataFormatada = formato.format(data);
        System.out.println(dataFormatada);
    }

   

    public void zipaArquivo() throws IOException {
        parametrosFiscais = new ParametrosFiscais();
        parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
        File diretorio = new File("/home/ubuntu/gerencial/" + parametrosFiscais.getEmitente().getRazao() + "-"
                + parametrosFiscais.getEmitente().getCnpj() + "/xml/" + ano + "/" + mes);
        if (!diretorio.exists()) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro!",
                            "Nenhum arquivo encontrado"));
        } else {
            EscritorioController.compactarParaZip(diretorio.toString() + ".zip", diretorio.toString());
            ativaDownload = true;
            caminhoDownload = diretorio.toString() + ".zip";
        }
    }

    public StreamedContent downloadFileZip() throws FileNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        InputStream stream = new FileInputStream(caminhoDownload);
        file = new DefaultStreamedContent(stream, "", ano + "-" + mes + ".zip");
        return file;
    }
    
    public StreamedContent downloadFile(Emissao v) throws FileNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        InputStream stream = new FileInputStream(v.getCaminho());
        file = new DefaultStreamedContent(stream, "application/xml", v.getChave());
        return file;
    }

    public List<Emissao> getLista() {
        return emissaoFacade.listar();
    }

    public Emissao getEmissao() {
        return emissao;
    }

    public void setEmissao(Emissao emissao) {
        this.emissao = emissao;
    }

//    public List<Emissao> listaEmissao(String parte) {
//        return emissaoFacade.listaAutocomplete(parte);
//    }
    public ItemEmissao getItemEmissao() {
        return itemEmissao;
    }

    public void setItemEmissao(ItemEmissao itemEmissao) {
        this.itemEmissao = itemEmissao;
    }

    public ProdutoFacade getProdutoFacade() {
        return produtoFacade;
    }

    public void setProdutoFacade(ProdutoFacade produtoFacade) {
        this.produtoFacade = produtoFacade;
    }

    public List<Produto> listaProduto(String parte) {
        return produtoFacade.listaAutocomplete(parte);
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Boolean getAtivaDownload() {
        return ativaDownload;
    }

    public void setAtivaDownload(Boolean ativaDownload) {
        this.ativaDownload = ativaDownload;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public ReboqueTrans getReboqueTrans() {
        return reboqueTrans;
    }

    public void setReboqueTrans(ReboqueTrans reboqueTrans) {
        this.reboqueTrans = reboqueTrans;
    }

    public Lacres getLacres() {
        return lacres;
    }

    public void setLacres(Lacres lacres) {
        this.lacres = lacres;
    }
    
    

    
}
