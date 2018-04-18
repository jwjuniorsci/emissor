/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.facade;




import br.com.comercial.entidade.Inutilizacao;
import br.com.comercial.persistencia.Transacional;
import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author JW Junior
 */
@Transacional
public class InutilizacaoFacade extends AbstractFacade<Inutilizacao> implements Serializable{
    
    @Inject
    private EntityManager em;

    public InutilizacaoFacade() {
        super(Inutilizacao.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
    
    
}
