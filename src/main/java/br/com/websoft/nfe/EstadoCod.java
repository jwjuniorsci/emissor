/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.websoft.nfe;

import java.io.Serializable;

/**
 *
 * @author junior
 */
public class EstadoCod implements Serializable {

    public static String codEstado(String sigla) {
        if (sigla.equals("AC")) {
            return "12";
        } else if (sigla.equals("AL")) {
            return "27";
        } else if (sigla.equals("AM")) {
            return "13";
        } else if (sigla.equals("AP")) {
            return "16";
        } else if (sigla.equals("BA")) {
            return "29";
        } else if (sigla.equals("CE")) {
            return "23";
        } else if (sigla.equals("DF")) {
            return "53";
        } else if (sigla.equals("ES")) {
            return "32";
        } else if (sigla.equals("GO")) {
            return "52";
        } else if(sigla.equals("MA")){
            return "21";
        } else if(sigla.equals("MG")){
            return "31";
        } else if(sigla.equals("MS")){
            return "50";
        } else if(sigla.equals("MT")){
            return "51";    
        } else if(sigla.equals("PA")){
            return "15";
        } else if(sigla.equals("PB")){
            return "25";
        } else if(sigla.equals("PE")){
            return "26";
        } else if(sigla.equals("PI")){
            return "22";
        } else if(sigla.equals("PR")){
            return "41";
        } else if(sigla.equals("RJ")){
            return "33";
        } else if(sigla.equals("RN")){
            return "24";
        } else if(sigla.equals("RO")){
            return "11"; 
        } else if(sigla.equals("RR")){
            return "14";
        } else if(sigla.equals("SC")){
            return "42";
        } else if(sigla.equals("SE")){
            return "28";
        } else if(sigla.equals("SP")){
            return "35";    
        } else if(sigla.equals("TO")){
            return "17";
        }
        return "";
    }

}
