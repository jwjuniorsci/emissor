/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.Cliente;
import br.com.comercial.entidade.Emissao;
import br.com.comercial.entidade.Emissao65;
import br.com.comercial.entidade.Emitente;
import br.com.comercial.entidade.ItemEmissao;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.entidade.Produto;
import br.com.comercial.facade.Emissao65Facade;
import br.com.comercial.facade.EmissaoFacade;
import br.com.comercial.facade.EmitenteFacade;
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
import br.com.samuelweb.nfe.util.WebServiceUtil;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.com.websoft.nfe.Configuracao;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnderEmi;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.*;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Prod;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Total.ICMSTot;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TUfEmi;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
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
import java.io.*;
import java.text.Normalizer;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author JW Junior
 */
@Named
@SessionScoped
public class EmissaoControle65 implements Serializable {

    @Inject
    private Emissao65Facade emissao65Facade;
    @Inject
    private ProdutoFacade produtoFacade;
    @Inject
    private EmitenteFacade emitenteFacade;
    private Emissao65 emissao;
    private ItemEmissao itemEmissao;
    private ConverterGenerico converterGenerico;
    private Emitente emitente;
    private String cnpj;
    private String cNfu2;
    private String xml;
    private String caminho;
    private String mes;
    private String ano;
    private String caminhoDownload;
    private Integer dv2 = 5;
    private Integer quant;
    private ParametrosFiscais parametrosFiscais;
    private Cliente cliente;
    private Boolean ativaDownload = false;
    private StreamedContent file;
    @Inject
    private ParametrosFiscaisFacade parametrosFiscaisFacade;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(emissao65Facade);
        }
        return converterGenerico;
    }

    public void novo() {
        emitente = new Emitente();
        emitente = emitenteFacade.listar().get(0);
        emissao = new Emissao65();
        itemEmissao = new ItemEmissao();
        //valoresPadraoItem();
        emissao.setStatus("EM DIGITAÇÃO");
        emissao.setValorDesconto(BigDecimal.ZERO);
        emissao.setValorTotal(BigDecimal.ZERO);
        emissao.setEmitente(emitente);
        itemEmissao.setEmissao65(emissao);
        itemEmissao.setItemQuant(BigDecimal.ZERO);
    }

    public String salvar() throws Exception {
//        try {
//          if(emissao.getTipo().equals("")){
//              FacesContext.getCurrentInstance().
//                    addMessage(null, new 
//        FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "O Campo Tipo é obrigatório.") );
//          } else {
        if (emissao.getTpNf().equals("")) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "O Campo Tipo é obrigatório."));
            return "";
        } else if (emissao.getNatOp().equals("")) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "O Campo Natureza da Operação é obrigatório."));
            return "";
        } else if (emissao.getListaProd() == null || emissao.getListaProd().isEmpty()) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "É obrigatório adicionar ao menos um Produto."));
            return "";
        } else {
            emissao.setEmitente(emitente);
            emiteNota(emissao);
            emissao65Facade.salvar(emissao);
            return "list.xhtml";
        }

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

    public void editar(Emissao65 e) {
        emissao = e;
    }

    public void excluir(Emissao65 e) throws Exception {
        try {
            emissao65Facade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }

    }

    /*public void defineTributacao() throws Exception {
        if (emissao != null && itemEmissao != null && emissao.getEmitente() != null && emissao.getCliente() != null) {
            System.out.println(emissao.getCliente());
            NfeCalculo calculo = NfeCalculoController.calculo(itemEmissao, emitente, cliente);

            //VALORES ICMS
            itemEmissao.setIcmsBc(calculo.getBaseCalculoIcms());
            emissao.setIcmsBc(emissao.getIcmsBc().add(itemEmissao.getIcmsBc()));
            itemEmissao.setIcmsVlr(calculo.getValorIcms());
            emissao.setIcmsVlr(emissao.getIcmsVlr().add(itemEmissao.getIcmsVlr()));

            //VALORES IPI
            itemEmissao.setIpiBc(calculo.getBaseCalculoIpi());
            itemEmissao.setIpiVlr(calculo.getValorIpi());

            //VALORES PIS
            itemEmissao.setPisBc(calculo.getBaseCalculoPis());
            itemEmissao.setPisVlr(calculo.getValorPis());

            //VALORES COFINS
            itemEmissao.setCofinsBc(calculo.getBaseCalculoCofins());
            itemEmissao.setCofinsVlr(calculo.getValorCofins());
        } else {
            System.out.println("Entrou no else");
            System.out.println(emissao.getEmitente());
            System.out.println(emissao.getCliente());
        }
    }*/
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
            totalProdutos = totalProdutos.add(it.getVlrProd());
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
        /*emissao.setVlrProdNf(totalProdutos);
        emissao.setIcmsBc(baseCalculoIcms);
        emissao.setIcmsVlr(valorIcms);
        emissao.setIpiVlr(valorIpi);
        emissao.setPisVlr(valorPis);

        emissao.setVlrTotalNf(valorNotaFiscal);*/

    }

    /*public void valoresPadraoNfe() {

        emissao.setSerie("001");
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

    }*/

 /*public void valoresPadraoItem() {
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
    }*/
    public void addItem() throws Exception {

        if (itemEmissao.getProduto() == null) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Adicionar!",
                            "O Campo Produto é obrigatório!"));
        } else if (itemEmissao.getCfop().equals("")) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Adicionar!",
                            "O Campo CFOP é obrigatório!"));
        } else if (itemEmissao.getItemQuant() == null || itemEmissao.getItemQuant() == BigDecimal.ZERO) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Adicionar!",
                            "O Campo Quantidade é obrigatório e deve ser maior que 0!"));
        } else if (itemEmissao.getVlrUnit() == null || itemEmissao.getVlrUnit() == BigDecimal.ZERO) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Adicionar!",
                            "O Campo Valor Unit. é obrigatório!"));
        } else if (itemEmissao.getVlrProd() == null || itemEmissao.getVlrProd() == BigDecimal.ZERO) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Adicionar!",
                            "O Campo Valor Prod. é obrigatório!"));
        } else {
            itemEmissao.setVlrTotal(itemEmissao.getSubTotal());
            Boolean produtoExiste = false;
            for (ItemEmissao ie : emissao.getListaProd()) {
                if (ie.getProduto().equals(itemEmissao.getProduto())) {
                    ie.setItemQuant(ie.getItemQuant().add(itemEmissao.getItemQuant()));
                    produtoExiste = true;
                }
            }
            if (!produtoExiste) {
                emissao.getListaProd().add(itemEmissao);
            }

            //defineTributacao();
            //atualizaTotais();
            itemEmissao = new ItemEmissao();
            itemEmissao.setEmissao65(emissao);
            itemEmissao.setItemQuant(BigDecimal.ZERO);
            //valoresPadraoItem();
            /*System.out.println(emissao.getIcmsBc());
        System.out.println(emissao.getIcmsVlr());
        System.out.println(emissao.getVlrDescNf());
        System.out.println(emissao.getVlrProdNf());
        System.out.println(emissao.getVlrTotalNf());*/
        }
    }

    public void removerItem(ItemEmissao ie) {
        emissao.getListaProd().remove(ie);
    }

    public void atlzCampProd() {
        if (itemEmissao.getProduto() != null) {
            itemEmissao.setCfop(itemEmissao.getProduto().getCfop());
            itemEmissao.setVlrUnit(itemEmissao.getProduto().getPrecoVenda());
            itemEmissao.setVlrProd(itemEmissao.getProduto().getPrecoVenda());
            itemEmissao.setPeso(itemEmissao.getProduto().getPeso());
            itemEmissao.setIcmsCst(itemEmissao.getProduto().getIcmsCsosn());
            itemEmissao.setIcmsRed(itemEmissao.getProduto().getIcmsPred());
        }
    }

    public List<Emissao65> lista65() {
        return emissao65Facade.listar();
    }

    public Emissao65 getEmissao65() {
        return emissao;
    }

    public void setEmissao65(Emissao65 emissao) {
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

    public String geraChaveNfe(ParametrosFiscais p) {
        System.out.println("Chamou o metodo");
        cnpj = p.getEmitente().getCnpj();
        cnpj = cnpj.replace(" ", ""); //tira espaço em branco
        cnpj = cnpj.replace(".", ""); //tira ponto
        cnpj = cnpj.replace("/", ""); //tira barra
        cnpj = cnpj.replace("-", ""); //tira hífen
        try {

            String cUF = "41"; // Código da UF do emitente do Documento Fiscal.    
            String dataAAMM = new SimpleDateFormat("yyMM").format(new Date()); // Ano e Mês de emissão da NF-e.    
            String cnpjCpf = cnpj; // CNPJ do emitente.    
            String mod = "65"; // Modelo do Documento Fiscal.    
            String serie = "1"; // Série do Documento Fiscal.    
            String tpEmis = "1"; // Forma de emissão da NF-e    
            String nNF = Integer.toString(p.getNumNota65()); // Número do Documento Fiscal.    
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

    public static int calculeDVModulo11(String numero) {
        int dv = 0;
        int[] values = new int[numero.length()];
        for (int i = 0; i < numero.length(); i++) {
            values[i] = Integer.parseInt(numero.charAt(i) + "");
        }
        int soma = 0;
        int vetpos = numero.length() - 1;
        while (vetpos >= 0) {
            for (int i = 2; i < 10; i++) {
                soma += values[vetpos] * i;
                vetpos--;
                if (vetpos < 0) {
                    break;
                }
            }
        }

        if (soma < 11) {
            dv = soma - 11;
        } else {
            int resto = soma % 11;
            dv = 11 - resto;
        }

        if (dv > 9) {
            dv = 0;
        }

        return dv;
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

    public void emiteNota(Emissao65 e) {
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
                ide.setMod("65");
                //Serie
                ide.setSerie("1");
                //Numero da Nota Fiscal
                ide.setNNF(Integer.toString(parametrosFiscais.getNumNota65()));
                //Hora de emissao
                ide.setDhEmi(formato7);
                //Tipo de Operacao (entrada/saida)
                ide.setTpNF(emissao.getTpNf());
                //Destino da Operacao 1 Op interna 2 Op interestadual 3 Op exterior
                ide.setIdDest("1");
                //Cod Municipio
                ide.setCMunFG(emissao.getEmitente().getCodMunicipio());
                //Tipo de Impressão
                ide.setTpImp("4");
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
                    dest.setCPF("10119722984");
                    dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
                    dest.setIndIEDest("9");
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
                    /*ICMS10 icms10 = new ICMS10();
            icms10.setOrig(ie.getProduto().getOrigem());
            //icms10.set'(chave); ie.getProduto().getIcmsCst());
            icms.setICMS20(icms20);*/

                    JAXBElement<ICMS> icmsElement = new JAXBElement<ICMS>(new QName("ICMS"), ICMS.class, icms);
                    imposto.getContent().add(icmsElement);

                    det.setImposto(imposto);
                    infNfe.getDet().add(det);
                    emissao.setValorTotal(emissao.getValorTotal().add(BigDecimal.ONE));
                } else if (emissao.getCliente() != null) {
                    String cpf = emissao.getCliente().getCpfCnpj();
                    cpf = cpf.replace(" ", ""); //tira espaço em branco
                    cpf = cpf.replace(".", ""); //tira ponto
                    cpf = cpf.replace("/", ""); //tira barra
                    cpf = cpf.replace("-", ""); //tira hífen

                    dest = new Dest();
                    dest.setCPF("10119722984");
                    dest.setXNome(emissao.getCliente().getNome());
                    //1 - Contribuinte ICMS (informar a tag IE do destinatário);
                    //2 - Contribuinte isento de Inscrição no cadastro de Contribuintes do ICMS - não informar a tag IE;
                    //9 - Não Contribuinte, que pode ou não possuir Inscrição Estadual no Cadastro de Contribuintes do ICMS - não informar a tag IE.
                    dest.setIndIEDest("9");
                    infNfe.setDest(dest);
                    num = 1;

                } else {
                    dest = new Dest();
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
                    if (ie.getProduto().getIcmsCsosn().equals("500")) {
                        ICMSSN500 icms500 = new ICMSSN500();
                        icms500.setOrig(ie.getProduto().getOrigem());
                        icms500.setCSOSN("500");
                        icms.setICMSSN500(icms500);
                    } else if (ie.getProduto().getIcmsCsosn().equals("200")) {
                        ICMSSN102 icmssn102 = new ICMSSN102();
                        icmssn102.setOrig(ie.getProduto().getOrigem());
                        icmssn102.setCSOSN("102");
                        icms.setICMSSN102(icmssn102);
                    }

                    /*ICMS10 icms10 = new ICMS10();
            icms10.setOrig(ie.getProduto().getOrigem());
            //icms10.set'(chave); ie.getProduto().getIcmsCst());
            icms.setICMS20(icms20);*/
                    JAXBElement<ICMS> icmsElement = new JAXBElement<ICMS>(new QName("ICMS"), ICMS.class, icms);
                    imposto.getContent().add(icmsElement);

                    det.setImposto(imposto);
                    infNfe.getDet().add(det);
                    num++;
                }
 
                //Totais
                Total total = new Total();
                ICMSTot icmsTot = new Total.ICMSTot();
                icmsTot.setVBC("0.00");
                icmsTot.setVICMS("0.00");
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
                icmsTot.setVProd(emissao.getValorTotal().add(emissao.getValorDesconto()).toString());
                icmsTot.setVFrete("0.00");
                icmsTot.setVSeg("0.00");
                icmsTot.setVDesc(emissao.getValorDesconto().toString());
                icmsTot.setVII("0.00");
                icmsTot.setVIPI("0.00");
                icmsTot.setVIPIDevol("0.00");
                icmsTot.setVPIS("0.00");
                icmsTot.setVCOFINS("0.00");
                icmsTot.setVOutro("0.00");
                icmsTot.setVTotTrib("0.00");
                icmsTot.setVNF(emissao.getValorTotal().toString());
                total.setICMSTot(icmsTot);
                infNfe.setTotal(total);

                Transp transp = new Transp();
                /*0-Contratação do Frete por conta do Remetente (CIF);
            1-Contratação do Frete por conta do Destinatário (FOB);
            2-Contratação do Frete por conta de Terceiros;
            3-Transporte Próprio por conta do Remetente;
            4-Transporte Próprio por conta do Destinatário;
            9-Sem Ocorrência de Transporte.*/
                transp.setModFrete("9");
                infNfe.setTransp(transp);

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
                detPag.setVPag(emissao.getValorTotal().toString());
                pag.getDetPag().add(detPag);

                infNfe.setPag(pag);

                nfe.setInfNFe(infNfe);

                //Monta EnviNfe
                TEnviNFe enviNFe = new TEnviNFe();
                enviNFe.setVersao("4.00");
                //Adicionar numero incremental
                enviNFe.setIdLote("1");
                enviNFe.setIndSinc("1");
                enviNFe.getNFe().add(nfe);

                //Monta e Assina o XML
                try {
                    enviNFe = Nfe.montaNfe(enviNFe, true);
                } catch (NfeValidacaoException nexc) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Gerar XML para receita",
                                    nexc.getMessage()));
                    return;
                }
                //QRCODE
                String qrCode = NFCeUtil.getCodeQRCode(infNfe.getId().substring(3),
                        "100",
                        ide.getTpAmb(),
                        dest.getCNPJ() == null ? dest.getCPF() : dest.getCNPJ(),
                        ide.getDhEmi(),
                        total.getICMSTot().getVNF(),
                        total.getICMSTot().getVICMS(),/*Base64.getEncoder().encodeToString(enviNFe.getNFe().get(0).getSignature().getSignedInfo().getReference().getDigestValue())*/
                        Base64.getEncoder().encodeToString(enviNFe.getNFe().get(0).getSignature().getSignedInfo().getReference().getDigestValue()),
                        "000001",
                        parametrosFiscais.getCsc(),
                        "http://www.fazenda.pr.gov.br/nfce/qrcode");

                TNFe.InfNFeSupl infNFeSupl = new TNFe.InfNFeSupl();
                infNFeSupl.setQrCode(qrCode);
                infNFeSupl.setUrlChave("http://www.fazenda.pr.gov.br");
                enviNFe.getNFe().get(0).setInfNFeSupl(infNFeSupl);

//Eniva a Nfe para a Sefaz
                TRetEnviNFe retorno = Nfe.enviarNfe(enviNFe, ConstantesUtil.NFCE);

                if (!retorno.getCStat().equals(StatusEnum.LOTE_PROCESSADO.getCodigo())) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Emitir NFCe", "Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo()));
                    return;
                }

                if (!retorno.getProtNFe().getInfProt().getCStat().equals(StatusEnum.AUTORIZADO.getCodigo())) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Emitir NFCe", "Status:" + retorno.getProtNFe().getInfProt().getCStat() + " - Motivo:" + retorno.getProtNFe().getInfProt().getXMotivo()));
                    return;
                }
                System.out.println("Status:" + retorno.getProtNFe().getInfProt().getCStat());
                System.out.println("Motivo:" + retorno.getProtNFe().getInfProt().getXMotivo());
                System.out.println("Data:" + retorno.getProtNFe().getInfProt().getDhRecbto());
                System.out.println("Protocolo:" + retorno.getProtNFe().getInfProt().getNProt());

                //System.out.println("Xml Final :" + XmlUtil.criaNfeProc(enviNFe, retorno.getProtNFe()));
                xml = XmlUtil.criaNfeProc(enviNFe, retorno.getProtNFe());
                parametrosFiscais.setNumNota65(parametrosFiscais.getNumNota65() + 1);
                try {
                    parametrosFiscaisFacade.salvar(parametrosFiscais);
                } catch (Exception ex) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na Transmissão",
                                    "Não foi possivel salvar o numero da NFC-e"));
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
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "NFC-e Transmitida", retorno.getProtNFe().getInfProt().getXMotivo()));

            }
        } catch (NfeException | JAXBException | CertificadoException exs) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Transmitir  NFC-e",
                            exs.getMessage()));
            System.out.println("Erro:" + exs.getMessage());
        }
    }

    public String criaXml(Emissao65 e) throws IOException {
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
        Date data = new Date();

        String ano = new SimpleDateFormat("yyyy").format(data);
        File diretorioAno = new File("/home/junior/fiscal/" + emissao.getEmitente().getRazao() + "-"
                + emissao.getEmitente().getCnpj() + "/xml/" + ano);
        if (!diretorioAno.exists()) {
            diretorioAno.mkdirs();
        }

        String mes = new SimpleDateFormat("MM").format(data);
        File diretorioMes = new File("/home/junior/fiscal/" + emissao.getEmitente().getRazao() + "-"
                + emissao.getEmitente().getCnpj() + "/xml/" + ano + "/" + mes);
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

    public static String removeAcentos(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
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

    public StreamedContent downloadFile() throws FileNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        InputStream stream = new FileInputStream(caminhoDownload);
        file = new DefaultStreamedContent(stream, "", ano + "-" + mes + ".zip");
        return file;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Emitente getEmitente() {
        return emitente;
    }

    public void setEmitente(Emitente emitente) {
        this.emitente = emitente;
    }

    public Integer getQuant() {
        return quant;
    }

    public void setQuant(Integer quant) {
        this.quant = quant;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Emissao65 getEmissao() {
        return emissao;
    }

    public void setEmissao(Emissao65 emissao) {
        this.emissao = emissao;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getCaminhoDownload() {
        return caminhoDownload;
    }

    public void setCaminhoDownload(String caminhoDownload) {
        this.caminhoDownload = caminhoDownload;
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

    public void setarCliente() {
        if (emissao.getCliente() != null) {
            cliente = emissao.getCliente();
        } else {
            System.out.println("Entrou no else do cliente");
        }
    }

}
