/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.Cliente;
import br.com.comercial.facade.AbstractFacade;
import br.com.comercial.facade.ClienteFacade;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ricardo
 */
@Named
@SessionScoped
public class ClienteControle implements Serializable {

    @Inject
    private ClienteFacade clienteFacade;

    private Cliente cliente;
    private ConverterGenerico converterGenerico;
    private Boolean ativaF = false;
    private Boolean ativaJ = false;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(clienteFacade);
        }
        return converterGenerico;
    }

    public void novo() {
        cliente = new Cliente();
        cliente.setTipo("PF");
    }
    
    public void validaPes() {
        if(cliente.getTipo().equals("PF")){
            System.out.println("IF");
            ativaF = true;
            ativaJ = false;
        } else if(cliente.getTipo().equals("PJ")){
            System.out.println("ELSE");
            ativaF = false;
            ativaJ = true;
        }
    }

    public String salvar() throws Exception {
        try {
          
                clienteFacade.salvar(cliente);
                 FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso", "Cliente Salvo!") );
              return "list.xhtml";  
             
                
//               
//            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new 
        FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Contate o Suporte (44)99812-9931") );
            
        }
        return "";
    }

    public void editar(Cliente e) {
        cliente = e;
    }

    public void excluir(Cliente e) throws Exception {
        try {
            clienteFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }
        
    }

    public List<Cliente> getLista() {
        return clienteFacade.listar();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> listaCliente(String parte) {
        return clienteFacade.listaAutocomplete(parte);
    }

    public ClienteFacade getClienteFacade() {
        return clienteFacade;
    }

    public void setClienteFacade(ClienteFacade clienteFacade) {
        this.clienteFacade = clienteFacade;
    }

    public ConverterGenerico getConverterGenerico() {
        return converterGenerico;
    }

    public void setConverterGenerico(ConverterGenerico converterGenerico) {
        this.converterGenerico = converterGenerico;
    }

    public Boolean getAtivaF() {
        return ativaF;
    }

    public void setAtivaF(Boolean ativaF) {
        this.ativaF = ativaF;
    }

    public Boolean getAtivaJ() {
        return ativaJ;
    }

    public void setAtivaJ(Boolean ativaJ) {
        this.ativaJ = ativaJ;
    }
    
    

}
