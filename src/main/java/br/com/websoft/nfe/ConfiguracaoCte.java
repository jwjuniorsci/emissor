/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.websoft.nfe;

import br.com.comercial.entidade.EmitenteCte;
import br.com.comercial.entidade.ParametrosCte;
import br.com.comercial.facade.EmitenteCteFacade;
import br.com.comercial.facade.ParametrosCteFacade;
import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.cte.dom.ConfiguracoesIniciais;
import br.com.samuelweb.cte.exception.CteException;
import br.com.samuelweb.cte.util.ConstantesCte;
import br.com.samuelweb.cte.util.Estados;
import java.io.Serializable;
import javax.inject.Inject;

/**
 *
 * @author junior
 */
public class ConfiguracaoCte implements Serializable {
    
    private ParametrosCte parametrosCte = null;
    private EmitenteCte emitenteCte = null;
    
    @Inject
    private ParametrosCteFacade parametrosCteFacade;
    @Inject
    private EmitenteCteFacade emitenteCteFacade;

    public ConfiguracaoCte() throws CertificadoException, CteException {
        parametrosCte = parametrosCteFacade.listar().get(0);
        emitenteCte = emitenteCteFacade.listar().get(0);
        iniciaConfiguracoesCte(emitenteCte, parametrosCte);
    }

    public static ConfiguracoesIniciais iniciaConfiguracoesCte(EmitenteCte e, ParametrosCte p) throws CertificadoException, CteException {
        String validaCte = "";

        if (p.getTipoEmi().equals("1")) {
            validaCte = ConstantesCte.AMBIENTE.PRODUCAO;
        }else if(p.getTipoEmi().equals("2")) {
            validaCte = ConstantesCte.AMBIENTE.HOMOLOGACAO;
        }
        
        if(e != null && p != null){
            Certificado certificado = CertificadoService.certificadoPfx("/home/junior/fiscal/Moraes&AmericoLTDAME.pfx", "1234");
            
            return ConfiguracoesIniciais.iniciaConfiguracoes(Estados.PR, ConstantesCte.AMBIENTE.HOMOLOGACAO, 
                    certificado, 
                    "/home/junior/fiscal/Cte/schemas", ConstantesCte.VERSAO.V3_00);
        } else {
            return ConfiguracoesIniciais.getInstance();
        }
    }
}
