/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.facade.AbstractFacade;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author ricardo
 * @param <T>
 */
public abstract class AbstractControle<T> implements Serializable {

    private Class<T> classe;
    private List<T> listagem;
    private T entidade;

    public AbstractControle(Class<T> classe) {
        this.classe = classe;
    }

    public abstract AbstractFacade<T> getFacade();
    
    public void novo(){
        try {
            entidade = classe.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            mensagem("Erro ao instanciar "+ex.getMessage(), FacesMessage.SEVERITY_FATAL);
        }
    }

    public void salvar() {
        try {
            getFacade().salvar(entidade);
            mensagem("Salvo com sucesso", FacesMessage.SEVERITY_INFO);
        } catch (Exception ex) {
            mensagem("Erro ao salvar: " + ex.getMessage(), FacesMessage.SEVERITY_FATAL);
        }
    }

    public void excluir() {
        try {
            getFacade().excluir(entidade);
            mensagem("Excluido com sucesso", FacesMessage.SEVERITY_INFO);
        } catch (Exception ex) {
            mensagem("Erro ao excluir: " + ex.getMessage(), FacesMessage.SEVERITY_FATAL);
        }
    }

    public List<T> getListar() {
        if (listagem == null) {
            listagem = getFacade().listar();
        }
        return listagem;
    }

    protected void mensagem(String msg, FacesMessage.Severity tipo) {
        FacesMessage message
                = new FacesMessage(tipo,
                        msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public T getEntidade() {
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
    }

}
