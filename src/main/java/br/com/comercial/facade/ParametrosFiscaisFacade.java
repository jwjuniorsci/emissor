/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.facade;

import br.com.comercial.entidade.Emitente;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.entidade.Produto;
import br.com.comercial.persistencia.Transacional;
import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author ricardo
 */
@Transacional
public class ParametrosFiscaisFacade extends AbstractFacade<ParametrosFiscais> implements Serializable{

    @Inject
    private EntityManager em;
    
    public ParametrosFiscaisFacade() {
        super(ParametrosFiscais.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
    
}
