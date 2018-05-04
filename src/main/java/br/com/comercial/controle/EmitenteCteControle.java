/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;

import br.com.comercial.converter.ConverterGenerico;
import br.com.comercial.entidade.EmitenteCte;
import br.com.comercial.facade.EmitenteCteFacade;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author JW Junior
 */
@Named
@SessionScoped
public class EmitenteCteControle implements Serializable {

    @Inject
    private EmitenteCteFacade emitenteFacade;
    private EmitenteCte emitente;
    private String caminho;
    private ConverterGenerico converterGenerico;

    public ConverterGenerico converter() {
        if (converterGenerico == null) {
            converterGenerico = new ConverterGenerico(emitenteFacade);
        }
        return converterGenerico;
    }

    public void novo() {
        emitente = new EmitenteCte();
        emitente.setStatus(Boolean.TRUE);
        emitente.setReferente("EmitenteCte");
    }

    public String salvar() throws Exception {
        try {
            String nomeCer = emitente.getRazao();
                nomeCer = nomeCer.replaceAll(" ", "");
                Path origem = Paths.get(caminho);
                Path destino = Paths.get("/home/junior/fiscal/"+nomeCer+".pfx");
                Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
                caminho = "/home/junior/fiscal/"+nomeCer+".pfx";
                emitente.setCaminho(caminho);
            if (emitente.getTipo().equals("")) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "O Campo Tipo é obrigatório."));
            } else if(emitente.getCaminho() == null || emitente.getCaminho().equals("")){
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao Salvar", "É obrigatório adicionar o certificado digital."));
            } 
            else {
                
                criaDiretorio();
                emitenteFacade.salvar(emitente);
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso", "EmitenteCte Salvo!"));
                return "list.xhtml";
            }

//               
//            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Contate o Suporte (44)99812-9931"));

        }
        return "";
    }

    public void editar(EmitenteCte e) {
        emitente = e;
    }

    public void excluir(EmitenteCte e) throws Exception {
        try {
            emitenteFacade.excluir(e);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Excluir!", "Não é possível excluir a coluna."));
        }

    }

    public void upload(FileUploadEvent evento) {
        try {
            UploadedFile arquivo = evento.getFile();
            Path arquivoTemp = Files.createTempFile(null, null);
            Files.copy(arquivo.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
            caminho = arquivoTemp.toString();
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Upload Realizado", "Upload do certificado realizado"));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao Realizar Upload!", "Não foi possivel realizar o Upload"));
            ex.printStackTrace();
        }
    }
    
    public void criaDiretorio() {
        File diretorioEmit = new File("/home/junior/fiscal/"+emitente.getRazao()+"-"+emitente.getCnpj());
        if(!diretorioEmit.exists()){
            diretorioEmit.mkdirs();
        }
    }

    public List<EmitenteCte> getLista() {
        return emitenteFacade.listar();
    }

    public EmitenteCte getEmitenteCte() {
        return emitente;
    }

    public void setEmitenteCte(EmitenteCte emitente) {
        this.emitente = emitente;
    }

    public List<EmitenteCte> listaEmitenteCte(String parte) {
        return emitenteFacade.listaAutocomplete(parte);
    }
}
