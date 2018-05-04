/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.entidade.EmitenteCte;
import br.com.comercial.entidade.ParametrosCte;
import br.com.comercial.facade.ParametrosCteFacade;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.cte.Cte;
import br.com.samuelweb.cte.dom.ConfiguracoesIniciais;
import br.com.samuelweb.cte.exception.CteException;
import br.com.websoft.nfe.ConfiguracaoCte;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import br.inf.portalfiscal.cte.schema_300.retConsStatServCTe.TRetConsStatServ;

/**
 * @author junior
 */
@Named
@SessionScoped
public class ConsultaCteControle implements Serializable {

    private ParametrosCte parametrosCte;
    @Inject
    private ParametrosCteFacade parametrosCteFacade;
    private String statusCte;

    public ConsultaCteControle() {
        //consultaStatusCte();
    }
    
    

    public String consultaStatusCte() {
        if (retornaEmit() != null) {
            try {
                System.out.println("Entrou no Try");
                ConfiguracoesIniciais config
                        = ConfiguracaoCte.iniciaConfiguracoesCte(parametrosCte.getEmitente(), parametrosCte);

                TRetConsStatServ retorno = Cte.statusServico();
                System.out.println("Cte Status:" + retorno.getCStat());
                System.out.println("Cte Motivo:" + retorno.getXMotivo());
                System.out.println("Cte Data:" + retorno.getDhRecbto());
                System.out.println("Finalizou o try");
                 return statusCte = retorno.getXMotivo();
                
            } catch (CteException | CertificadoException e) {
                System.out.println("Entrou no catch");
                System.out.println("Erro:" + e.getMessage());
            }
        } else {
            System.out.println("Entrou no else");
          return statusCte = "Configurações de Cte desconhecidas";
        }
        return "";
    }

    public ParametrosCte retornaEmit() {
        if (parametrosCteFacade.listar() == null || parametrosCteFacade.listar().isEmpty()) {
            return null;
        } else {
            parametrosCte = parametrosCteFacade.listar().get(0);
            return parametrosCte;
        }
    }

    public String getStatusCte() {
        return statusCte;
    }

    public void setStatusCte(String statusCte) {
        this.statusCte = statusCte;
    }
    
    
}
