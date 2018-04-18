/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.persistencia;

import java.io.Serializable;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

/**
 *
 * @author ricardo
 */

@Transacional
@Interceptor
public class TransactionInterceptor implements Serializable{

    @Inject
    private EntityManager em;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            
            Object result = ctx.proceed();
            
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}
