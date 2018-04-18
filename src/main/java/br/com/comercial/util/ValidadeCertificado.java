/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.util;

import br.com.comercial.entidade.Emitente;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

/**
 *
 * @author JW Junior
 */
public class ValidadeCertificado {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private String dataIni;
    private String validade;

    public String validaCertificado(Emitente e) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

        
            String caminhoDoCertificado = e.getCaminho();
            String senha = e.getSenhaCert();

            KeyStore keystore = KeyStore.getInstance(("PKCS12"));
            keystore.load(new FileInputStream(caminhoDoCertificado), senha.toCharArray());

            Enumeration<String> eAliases = keystore.aliases();

            while (eAliases.hasMoreElements()) {
                String alias = (String) eAliases.nextElement();
                Certificate certificado = (Certificate) keystore.getCertificate(alias);

                System.out.println("Aliais: " + alias);
                X509Certificate cert = (X509Certificate) certificado;

                System.out.println(cert.getSubjectDN().getName());
                System.out.println("Válido a partir de..: " + dateFormat.format(cert.getNotBefore()));
                System.out.println("Válido até..........: " + dateFormat.format(cert.getNotAfter()));
                
                dataIni = "Válido a partir de "+dateFormat.format(cert.getNotBefore());
                return validade = "Válido até "+dateFormat.format(cert.getNotAfter());
            }
        
        
         /** 
     * Error. 
     * @param log 
     */  
   
    /** 
     * Info 
     * @param log 
     */  
     
        return "Não foi possivel acessar informações do certifcado";
    }

    public String getDataIni() {
        return dataIni;
    }

    public void setDataIni(String dataIni) {
        this.dataIni = dataIni;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }
    
    
}
