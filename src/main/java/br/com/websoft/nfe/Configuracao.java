/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.websoft.nfe;

import br.com.comercial.entidade.Emitente;
import br.com.comercial.entidade.ParametrosFiscais;
import br.com.comercial.facade.EmitenteFacade;
import br.com.comercial.facade.ParametrosFiscaisFacade;
import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.dom.ConfiguracoesIniciaisNfe;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.Estados;
import java.io.Serializable;
import javax.inject.Inject;

/**
 *
 * @author JW Junior
 */
public class Configuracao implements Serializable {

    private ParametrosFiscais parametrosFiscais = null;
    private Emitente emitente = null;
    
    @Inject
    private ParametrosFiscaisFacade parametrosFiscaisFacade;
    @Inject
    private EmitenteFacade emitenteFacade;

    public Configuracao() throws NfeException, CertificadoException {
        parametrosFiscais = parametrosFiscaisFacade.listar().get(0);
        emitente = emitenteFacade.listar().get(0);
        iniciaConf(emitente, parametrosFiscais);
    }
    
    public static ConfiguracoesIniciaisNfe iniciaConf(Emitente e, ParametrosFiscais p) throws NfeException, CertificadoException {
        String validaAmbiente;
        if(p.getTipoEmi().equals("1")){
             validaAmbiente = ConstantesUtil.AMBIENTE.PRODUCAO;
        } else {
             validaAmbiente = ConstantesUtil.AMBIENTE.HOMOLOGACAO;
        }
        if (e != null && p != null) {
            Certificado certificado = CertificadoService.certificadoPfx(e.getCaminho(), e.getSenhaCert());
            return ConfiguracoesIniciaisNfe.iniciaConfiguracoes(Estados.PR,validaAmbiente,certificado,
                    "/home/junior/fiscal/schemas");
        } else {
            
            return ConfiguracoesIniciaisNfe.getInstance();
        }
    }

}
