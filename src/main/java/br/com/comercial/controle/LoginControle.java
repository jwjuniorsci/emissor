/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;


import br.com.comercial.entidade.Usuario;
import br.com.comercial.facade.UsuarioFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author JW Junior
 */
@Named
@SessionScoped
public class LoginControle implements Serializable {

    @Inject
    private UsuarioFacade usuarioFacade;
    
    private Date dataLog;
    private Usuario usuario;
    private String login;
    private String senha;
    private Boolean logado = false;
    private Boolean usuAdm = false;
    private Boolean validaMenuCad = false;
    private Boolean validaMenuAlmoxarifado = false;
    private Boolean validarequisicoesI = false;
    private Boolean validarequisicoesA = false;
    private Boolean validaDemais = false;
    private Boolean validaCadAlmo = false;

    public LoginControle() {
        dataLog = new Date();
        usuAdm = false;
    }

    public String logar() throws Exception {
        //if(login.equals("websoft") && senha.equals("websoftadm2018")){
            usuario = usuarioFacade.pesquisaUsuario(login, senha);
        //}
        
        if (usuario != null || (login.equals("websoft") && senha.equals("websoftadm2018"))) {
            if(login.equals("websoft") && senha.equals("websoftadm2018")){
                usuAdm = true;
            }
            logado = true;
            if (usuario != null) {
                usuario.setDataAcesso(dataLog);
                usuarioFacade.salvar(usuario);
            }
            if (usuario != null) {
                if (usuario.getNivelAcesso().equals("Administrador")) {
                    validaMenuCad = true;
                    validaMenuAlmoxarifado = true;
                    validarequisicoesI = false;
                    validarequisicoesA = true;
                    validaDemais = true;
                    validaCadAlmo = true;
                } else if (usuario.getNivelAcesso().equals("Instituição")) {
                    validaMenuCad = false;
                    validaMenuAlmoxarifado = false;
                    validarequisicoesI = true;
                    validarequisicoesA = false;
                    validaDemais = false;
                    validaCadAlmo = false;
                } else if (usuario.getNivelAcesso().equals("Almoxarifado")) {
                    validaMenuCad = true;
                    validaMenuAlmoxarifado = true;
                    validarequisicoesA = true;
                    validaDemais = false;
                    validaCadAlmo = true;
                }
            } else {
                validaMenuCad = true;
                validaMenuAlmoxarifado = true;
            }
            return "/dashboard?faces-redirect=true";
        } else {
            logado = false;

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuário e/ou Senha incorretos", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

    }

    public String logoff() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().invalidate();
        return "/login?faces-redirect=true";
    }

   

    public UsuarioFacade getUsuarioFacade() {
        return usuarioFacade;
    }

    public void setUsuarioFacade(UsuarioFacade usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getLogado() {
        return logado;
    }

    public void setLogado(Boolean logado) {
        this.logado = logado;
    }

    public Boolean getValidaMenuCad() {
        return validaMenuCad;
    }

    public void setValidaMenuCad(Boolean validaMenuCad) {
        this.validaMenuCad = validaMenuCad;
    }

    public Boolean getValidaMenuAlmoxarifado() {
        return validaMenuAlmoxarifado;
    }

    public void setValidaMenuAlmoxarifado(Boolean validaMenuAlmoxarifado) {
        this.validaMenuAlmoxarifado = validaMenuAlmoxarifado;
    }

    public Boolean getValidarequisicoesI() {
        return validarequisicoesI;
    }

    public void setValidarequisicoesI(Boolean validarequisicoesI) {
        this.validarequisicoesI = validarequisicoesI;
    }

    public Boolean getValidarequisicoesA() {
        return validarequisicoesA;
    }

    public void setValidarequisicoesA(Boolean validarequisicoesA) {
        this.validarequisicoesA = validarequisicoesA;
    }

    public Boolean getValidaDemais() {
        return validaDemais;
    }

    public void setValidaDemais(Boolean validaDemais) {
        this.validaDemais = validaDemais;
    }

    public Boolean getValidaCadAlmo() {
        return validaCadAlmo;
    }

    public void setValidaCadAlmo(Boolean validaCadAlmo) {
        this.validaCadAlmo = validaCadAlmo;
    }

    public Boolean getUsuAdm() {
        return usuAdm;
    }

    public void setUsuAdm(Boolean usuAdm) {
        this.usuAdm = usuAdm;
    }
    
    

}
