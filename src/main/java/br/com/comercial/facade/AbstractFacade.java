/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.facade;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author ricardo
 * @param <T>
 */
public abstract class AbstractFacade<T> implements Serializable {

    private Class<T> classe;

    public AbstractFacade(Class<T> classe) {
        this.classe = classe;
    }

    public abstract EntityManager getEm();

    
    public T salvar(T entidade) throws Exception {
        try {
            entidade = getEm().merge(entidade);
            getEm().flush();
            return entidade;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void excluir(T entidade) throws Exception {
        try {
            getEm().remove(getEm().merge(entidade));
            getEm().flush();
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public T pesquisarId(Long id){
        return getEm().find(classe, id);
    }

    public List<T> listar() {
        return getEm().createQuery("FROM "+classe.getSimpleName()).getResultList();
    }

}
