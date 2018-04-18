/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.Produto;
import br.com.comercial.facade.ProdutoFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author JW Junior
 */
@Named
@SessionScoped
public class ProdutoControle implements Serializable {

    @Inject
    private ProdutoFacade produtoFacade;
    private Produto produto;
    private ConverterGenerico converterGenerico;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(produtoFacade);
        }
        return converterGenerico;
    }

    public void novo() {
        produto = new Produto();
        produto.setPeso(Float.valueOf("0.0"));
        produto.setPrecoCompra(BigDecimal.ZERO);
        produto.setPrecoVenda(BigDecimal.ZERO);
        produto.setIcmsPerc(BigDecimal.ZERO);
        produto.setIcmsPred(BigDecimal.ZERO);
        produto.setIpiPerc(BigDecimal.ZERO);
        produto.setPisPerc(BigDecimal.ZERO);
        produto.setPisPerc(BigDecimal.ZERO);
        produto.setCofinsPerc(BigDecimal.ZERO);
    }

    public String salvar() throws Exception {
        try {
            if (produto.getPrecoCompra().equals(0) || produto.getPrecoCompra() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Preço de Compra é obrigatório e deve ser maior que 0"));
                return "";
            } else if (produto.getPrecoVenda().equals(0) || produto.getPrecoVenda() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Preço de Venda é obrigatório e deve ser maior que 0"));
                return "";
            } else if (produto.getIcmsPerc() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "% ICMS é obrigatório"));
                return "";
            } else if (produto.getIcmsPred() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "% ICMS(Red) é obrigatório"));
                return "";
            } else if (produto.getIpiPerc() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "% IPI é obrigatório"));
                return "";
            } else if (produto.getPisPerc() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "% PIS é obrigatório"));
                return "";
            } else if (produto.getCofinsPerc() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "% COFINS é obrigatório"));
                return "";
            } else if (produto.getIcmsCsosn().equals("")) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "CSOSN ICMS é obrigatório"));
                return "";
            } else if (produto.getIpiCst().equals("")) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "CST IPI é obrigatório"));
                return "";
            } else if (produto.getPisCst().equals("")) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "CST PIS é obrigatório"));
                return "";
            } else if (produto.getCofinsCst().equals("")) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "CST COFINS é obrigatório"));
                return "";
            } else if (produto.getUnidade().equals("")) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Unidade é obrigatória")); 
                return "";
            } else {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso", "Produto Salvo!"));
                produtoFacade.salvar(produto);
                return "list?faces-redirect=true";
                
                
            }

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Contate o Suporte (44)99812-9931"));
            return "";

        }
       
    }

    public void editar(Produto e) {
        produto = e;
    }

    public void excluir(Produto e) throws Exception {
        try {
            produtoFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }

    }

    public List<Produto> getLista() {
        return produtoFacade.listar();
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public List<Produto> listaProduto(String parte) {
        return produtoFacade.listaAutocomplete(parte);
    }
    
    public void teste(){
        System.out.println("Testando");
    }
}
