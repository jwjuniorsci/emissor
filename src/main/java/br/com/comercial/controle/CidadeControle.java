/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;



import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.Cidade;
import br.com.comercial.facade.CidadeFacade;
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
public class CidadeControle implements Serializable {

    @Inject
    private CidadeFacade cidadeFacade;
    private Cidade cidade;
    private ConverterGenerico converterGenerico;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(cidadeFacade);
        }
        return converterGenerico;
    }

    public void novo() {
        cidade = new Cidade();
    }

    public String salvar() throws Exception {
//        try {
//            if (cidade.getMarca() == null) {
//                FacesContext.getCurrentInstance().
//                    addMessage(null, new 
//        FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "O Campo Marca é obrigatório") );
//            } else {
                cidadeFacade.salvar(cidade);
                 FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso", "Cidade Salvo!") );
              return "list.xhtml";  
                
//               
//            }
//        } catch (Exception ex) {
//            FacesContext.getCurrentInstance().
//                    addMessage(null, new 
//        FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Contate o Suporte (44)99812-9931") );
//            
//        }
//        return "";
    }

    public void editar(Cidade e) {
        cidade = e;
    }

    public void excluir(Cidade e) throws Exception {
        try {
            cidadeFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }
        
    }

    public List<Cidade> getLista() {
        return cidadeFacade.listar();
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public List<Cidade> listaCidade(String parte) {
        return cidadeFacade.listaAutocomplete(parte);
    }
}
