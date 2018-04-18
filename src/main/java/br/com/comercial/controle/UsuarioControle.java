/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;


import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.Usuario;
import br.com.comercial.facade.UsuarioFacade;
import br.com.comercial.util.Criptografia;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaimedias
 */
@Named
@SessionScoped
public class UsuarioControle implements Serializable {

    private Usuario usuario;
    private String confirmaSenha;
    private String senha;
    private Boolean valida = false;
    @Inject
    private UsuarioFacade usuarioFacade;

    public UsuarioControle() {
        valida = false;
    }

    private ConverterGenerico usuarioConverter;

    public ConverterGenerico converter() {
        if (usuarioConverter == null) {
            usuarioConverter = new ConverterGenerico(usuarioFacade);
        }
        return usuarioConverter;
    }

    public void novo() {
        usuario = new Usuario();
        confirmaSenha = "";
    }

    public String salvar() throws Exception {
        
        if (usuario.getNivelAcesso().equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Selecione o Privilégio!"));
            return "";
        } else {
            usuario.setSenha(Criptografia.md5(senha));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Salvo com sucesso", "Usuario salvo."));
            usuarioFacade.salvar(usuario);
            return "list.xhtml";
        }

    }

    public void editar(Usuario e) {
        usuario = e;
    }

    public void excluir(Usuario e) throws Exception {
        usuarioFacade.excluir(e);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getLista() {
        return usuarioFacade.listar();
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getValida() {
        return valida;
    }

    public void setValida(Boolean valida) {
        this.valida = valida;
    }
    
    
    
    public void validaCentroEdu(){
        if(usuario.getNivelAcesso().equals("Instituição")){
            valida = true;
        } else {
            valida = false;
        }
    }

}
