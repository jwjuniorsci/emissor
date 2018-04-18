/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;



import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.Transportadora;
import br.com.comercial.facade.TransportadoraFacade;
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
public class TransportadoraControle implements Serializable {

    @Inject
    private TransportadoraFacade transportadoraFacade;
    private Transportadora transportadora;
    private ConverterGenerico converterGenerico;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(transportadoraFacade);
        }
        return converterGenerico;
    }

    public void novo() {
        transportadora = new Transportadora();
        transportadora.setReferente("Transportadora");
    }

    public String salvar() throws Exception {
        try {
          if(transportadora.getTipo().equals("")){
              FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "O Campo Tipo é obrigatório.") );
          } else {
                transportadoraFacade.salvar(transportadora);
                 FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso", "Transportadora Salva!") );
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

    public void editar(Transportadora e) {
        transportadora = e;
    }

    public void excluir(Transportadora e) throws Exception {
        try {
            transportadoraFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }
        
    }

    public List<Transportadora> getLista() {
        return transportadoraFacade.listar();
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public List<Transportadora> listaTransportadora(String parte) {
        return transportadoraFacade.listaAutocomplete(parte);
    }

    public TransportadoraFacade getTransportadoraFacade() {
        return transportadoraFacade;
    }

    public void setTransportadoraFacade(TransportadoraFacade transportadoraFacade) {
        this.transportadoraFacade = transportadoraFacade;
    }

    
    
    
}
