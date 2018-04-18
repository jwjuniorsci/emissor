/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author JW Junior
 */
public class GeraChaveNfe {
    
    private String cUf; //codigo do estado
    private String dataAAMM; //Ano e Mês de emissão da NF-e
    private String cnpjCpf; //documento do emitente
    private String mod; //modelo do documento fiscal
    private String serie; //Série do documento fiscal
    private String tpEmis; //Forma de emissão da NF-e
    private String nNF; //Nº do documento fiscal
    private String cNF; //Código Numérico que compõe a chave de acesso
    
    public void geraNumero(Date dataNfe) {
        SimpleDateFormat formato = new SimpleDateFormat("yyMM");
        dataAAMM = formato.format(dataNfe);
        
        StringBuilder chave = new StringBuilder();
        chave.append(lpadTo(cUf, 2, '0'));
        chave.append(lpadTo(dataAAMM, 4, '0'));
        chave.append(lpadTo(cnpjCpf.replaceAll("\\D", ""), 14, '0'));
        chave.append(lpadTo(mod, 2, '0'));
        chave.append(lpadTo(serie, 3, '0'));
        chave.append(lpadTo(String.valueOf(nNF), 9, '0'));
        chave.append(lpadTo(tpEmis, 1, '0'));
        chave.append(lpadTo(cNF, 8, '0'));
        chave.append(modulo11(chave.toString()));
    }
    
    public static String lpadTo(String input, int width, char ch){
        String strPad = "";
        
        StringBuffer sb = new StringBuffer(input.trim());
        while(sb.length() < width) {
            sb.insert(0, ch);
        }
        strPad = sb.toString();
        if(strPad.length() > width) {
            strPad = strPad.substring(0,width);
        }
        return strPad;
    }
    
    public static int modulo11(String chave) {  
        int total = 0;  
        int peso = 2;  
              
        for (int i = 0; i < chave.length(); i++) {  
            total += (chave.charAt((chave.length()-1) - i) - '0') * peso;  
            peso ++;  
            if (peso == 10)  
                peso = 2;  
        }  
        int resto = total % 11;  
        return (resto == 0 || resto == 1) ? 0 : (11 - resto);  
    }  
  
    

    public String getcUf() {
        return cUf;
    }

    public void setcUf(String cUf) {
        this.cUf = cUf;
    }

    public String getDataAAMM() {
        return dataAAMM;
    }

    public void setDataAAMM(String dataAAMM) {
        this.dataAAMM = dataAAMM;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTpEmis() {
        return tpEmis;
    }

    public void setTpEmis(String tpEmis) {
        this.tpEmis = tpEmis;
    }

    public String getnNF() {
        return nNF;
    }

    public void setnNF(String nNF) {
        this.nNF = nNF;
    }

    public String getcNF() {
        return cNF;
    }

    public void setcNF(String cNF) {
        this.cNF = cNF;
    }
    
    
    
}
