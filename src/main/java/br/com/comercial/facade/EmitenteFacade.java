/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.facade;

import br.com.comercial.entidade.Emitente;
import br.com.comercial.entidade.Produto;
import br.com.comercial.persistencia.Transacional;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author ricardo
 */
@Transacional
public class EmitenteFacade extends AbstractFacade<Emitente> implements Serializable{

    @Inject
    private EntityManager em;
    
    public EmitenteFacade() {
        super(Emitente.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
    
    public List<Emitente> listaAutocomplete(String valor) {
        Query q = em.createQuery("from Emitente where lower(fantasia) like :filtro");
        q.setParameter("filtro","%"+valor.toLowerCase()+"%");
        return q.getResultList();
    }
    
    
    
}
