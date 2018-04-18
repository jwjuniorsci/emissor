/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.facade;


import br.com.comercial.entidade.Usuario;
import br.com.comercial.persistencia.Transacional;
import br.com.comercial.util.Criptografia;
import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author JW Junior
 */
@Transacional
public class UsuarioFacade extends AbstractFacade<Usuario> implements Serializable{
    
    @Inject
    private EntityManager em;

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
    
    public Usuario pesquisaUsuario(String login, String senha){
        Query query = em.createQuery("FROM Usuario AS u WHERE u.login='" + login + "' AND u.senha='" + Criptografia.md5(senha) + "'");
        if (query.getResultList().size() == 1) {
            return (Usuario) query.getResultList().get(0);
        }
        return null;
    }
}
