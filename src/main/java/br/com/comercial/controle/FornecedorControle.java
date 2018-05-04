/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;



import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.Emitente;
import br.com.comercial.facade.EmitenteFacade;
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
public class FornecedorControle implements Serializable {

    @Inject
    private EmitenteFacade fornecedorFacade;
    private Emitente fornecedor;
    private ConverterGenerico converterGenerico;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(fornecedorFacade);
        }
        return converterGenerico;
    }

    public void novo() {
        fornecedor = new Emitente();
        fornecedor.setStatus(Boolean.TRUE);
        fornecedor.setReferente("Fornecedor");
    }

    public String salvar() throws Exception {
        try {
          if(fornecedor.getTipo().equals("")){
              FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "O Campo Tipo é obrigatório.") );
          } else {
                fornecedorFacade.salvar(fornecedor);
                 FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso", "Fornecedor Salvo!") );
              return "list.xhtml";  
        }      
                
//               
//            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Contate o Suporte (44)99812-9931") );
            
        }
        return "";
    }

    public void editar(Emitente e) {
        fornecedor = e;
    }

    public void excluir(Emitente e) throws Exception {
        try {
            fornecedorFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }
        
    }

    /*public List<Emitente> getLista() {
        return fornecedorFacade.listaFornecedor();
    }*/

    public Emitente getEmitente() {
        return fornecedor;
    }

    public void setEmitente(Emitente fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<Emitente> listaEmitente(String parte) {
        return fornecedorFacade.listaAutocomplete(parte);
    }

    public Emitente getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Emitente fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    
}
