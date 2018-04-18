/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.facade;

import br.com.comercial.entidade.Emissao;
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
public class EmissaoFacade extends AbstractFacade<Emissao> implements Serializable{

    @Inject
    private EntityManager em;
    
    public EmissaoFacade() {
        super(Emissao.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
    
//    public List<Emitente> listaAutocomplete(String valor) {
//        Query q = em.createQuery("from Emitente where lower(fantasia) like :filtro");
//        q.setParameter("filtro","%"+valor.toLowerCase()+"%");
//        return q.getResultList();
//    }
//    
//    public List<Emitente> listaTransportadora(){
//        Query q = getEm().createNativeQuery("select * from Emitente where referente = 'Transportadora';",Emitente.class);
//        return q.getResultList();
//    }
//    
//    public List<Emitente> listaFornecedor(){
//        Query q = getEm().createNativeQuery("select * from Emitente where referente = 'Fornecedor';",Emitente.class);
//        return q.getResultList();
//    }
//    public List<Emitente> listaEmitente(){
//        Query q = getEm().createNativeQuery("select * from Emitente where referente = 'Emitente';",Emitente.class);
//        return q.getResultList();
//    }
    
}
