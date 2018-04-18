/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;



import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.NumeroNfe;
import br.com.comercial.facade.NumeroNfeFacade;
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
public class NumeroNfeControle implements Serializable {

    @Inject
    private NumeroNfeFacade numeroNfeFacade;
    private NumeroNfe numeroNfe;
    private List<NumeroNfe> numeroNfeAux;
    private ConverterGenerico converterGenerico;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(numeroNfeFacade);
        }
        return converterGenerico;
    }

    public void novo() {
        numeroNfe = new NumeroNfe();
    }

    public void salvar() throws Exception {
        try {
        numeroNfeAux = numeroNfeFacade.listar();
        for(NumeroNfe nf : numeroNfeAux){
            if(nf.getAmbiente().equals(numeroNfe.getAmbiente()) && nf.getModeloNfe().equals(numeroNfe.getModeloNfe())){
                numeroNfe.setId(nf.getId());
            }
        }
        
                numeroNfeFacade.salvar(numeroNfe);
                 FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso", "Numero de Nota Fiscal Salvo!") );
              
                
               
            
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Contate o Suporte (44)99812-9931") );
            
        }

    }

    public void editar(NumeroNfe e) {
        numeroNfe = e;
    }

    public void excluir(NumeroNfe e) throws Exception {
        try {
            numeroNfeFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }
        
    }

    public List<NumeroNfe> getLista() {
        return numeroNfeFacade.listar();
    }

    public NumeroNfe getNumeroNfe() {
        return numeroNfe;
    }

    public void setNumeroNfe(NumeroNfe numeroNfe) {
        this.numeroNfe = numeroNfe;
    }

   
}
