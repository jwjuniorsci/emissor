/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.facade;

import br.com.comercial.entidade.Pessoa;
import br.com.comercial.persistencia.Transacional;
import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author ricardo
 */

@Transacional
public class PessoaFacade extends AbstractFacade<Pessoa> implements Serializable {
    
    @Inject
    private EntityManager em;

    public PessoaFacade() {
        super(Pessoa.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
    
    
}
