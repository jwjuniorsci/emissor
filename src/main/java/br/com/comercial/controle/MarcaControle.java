/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.Marca;
import br.com.comercial.facade.MarcaFacade;
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
public class MarcaControle implements Serializable {

    @Inject
    private MarcaFacade marcaFacade;
    private Marca marca;
    private ConverterGenerico converterGenerico;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(marcaFacade);
        }
        return converterGenerico;
    }

    public void novo() {
       
            marca = new Marca();
          

    }

    public String salvar() throws Exception {
        
                
                marcaFacade.salvar(marca);
                System.out.println("Salvou");
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso", "Marca Salva!"));
                return "list.xhtml";
        

    }

    public void editar(Marca e) {
        marca = e;
    }

    public void excluir(Marca e) throws Exception {
        try {
            marcaFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }

    }

    public List<Marca> getLista() {
        return marcaFacade.listar();
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

//    public List<Marca> listaMarca(String parte) {
//        return marcaFacade.listaAutocomplete(parte);
//    }
}
