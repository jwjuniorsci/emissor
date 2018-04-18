/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.controle;


import br.com.comercial.util.ZipUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author junior
 */
@Named
@SessionScoped
public class EscritorioController implements Serializable{
    
    //Constantes
   static final int TAMANHO_BUFFER = 400000096; // 4kb
   
   //método para compactar arquivo
   public static void compactarParaZip(String arqSaida,String arqEntrada)
throws IOException{
      // para compactar faça
            File origem = new File(arqEntrada);
            File destino = new File(arqSaida);
            ZipUtils.compress(origem, destino);
   } 
    
}
