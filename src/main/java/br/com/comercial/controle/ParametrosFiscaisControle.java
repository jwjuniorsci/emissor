/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.facade.ParametrosFiscaisFacade;
import java.io.Serializable;
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
public class ParametrosFiscaisControle implements Serializable {

    @Inject
    private ParametrosFiscaisFacade parametrosFiscaisFacade;
    private ParametrosFiscais parametrosFiscais;
    private ConverterGenerico converterGenerico;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(parametrosFiscaisFacade);
        }
        return converterGenerico;
    }

    public void novo() {
        if (parametrosFiscaisFacade.listar().isEmpty()) {
            System.out.println("Não existe parametro");
            parametrosFiscais = new ParametrosFiscais();
            parametrosFiscais.setId(1);
            System.out.println(parametrosFiscais.getId());
        } else {
            parametrosFiscais = new ParametrosFiscais();
            parametrosFiscais.setId(1);
            System.out.println("Não é a primeira vez que se cria um novo");
        }

    }

    public String salvar() throws Exception {
        System.out.println("Entrou no método");
       
            if (parametrosFiscais.getSerie55().equals("") && parametrosFiscais.getSerie65().equals("")) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "Informe ao menos um Numero de Série"));
            } else if (parametrosFiscais.getSerie65().equals("")) {
                System.out.println("Entrou no método else if ");
                if (parametrosFiscais.getIdToken().equals("")) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "O Campo Código Token deve ser preenchido."));
                }
                if (parametrosFiscais.getCsc().equals("")) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "O Campo Código CSC deve ser preenchido."));
                }
            } else {
                System.out.println("Entrou no método else");
                parametrosFiscais.setId(1);
                parametrosFiscaisFacade.salvar(parametrosFiscais);
                System.out.println("Salvou");
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso", "Parametro Fiscal Salvo!"));
                return "list.xhtml";
            }

//               
//            }
       
        return "";
    }

    public void editar(ParametrosFiscais e) {
        parametrosFiscais = e;
        parametrosFiscais.setId(1);
    }

    public void excluir(ParametrosFiscais e) throws Exception {
        try {
            parametrosFiscaisFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }

    }

    public List<ParametrosFiscais> getLista() {
        return parametrosFiscaisFacade.listar();
    }

    public ParametrosFiscais getParametrosFiscais() {
        return parametrosFiscais;
    }

    public void setParametrosFiscais(ParametrosFiscais parametrosFiscais) {
        this.parametrosFiscais = parametrosFiscais;
    }

//    public List<ParametrosFiscais> listaParametrosFiscais(String parte) {
//        return parametrosFiscaisFacade.listaAutocomplete(parte);
//    }
}
