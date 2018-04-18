/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comercial.entidade;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author junior
 */
@Entity
@Table(name = "cidades")
public class Cidade implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "estado_id")
    @ManyToOne
    private Estado estado;
    @Column(name = "nome")
    private String nome;
    @Column(name = "codigo_ibge")
    private Integer codigoCid;
    @Column(name = "populacao_2010")
    private Integer populacao2010;
    @Column(name = "densidade_demo")
    private Integer densidadeDemo;
    @Column(name = "area")
    private Integer area;
    @Column(name = "gentilico")
    private String gentilico;
    

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigoCid() {
        return codigoCid;
    }

    public void setCodigoCid(Integer codigoCid) {
        this.codigoCid = codigoCid;
    }

    public Integer getPopulacao2010() {
        return populacao2010;
    }

    public void setPopulacao2010(Integer populacao2010) {
        this.populacao2010 = populacao2010;
    }

    public Integer getDensidadeDemo() {
        return densidadeDemo;
    }

    public void setDensidadeDemo(Integer densidadeDemo) {
        this.densidadeDemo = densidadeDemo;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getGentilico() {
        return gentilico;
    }

    public void setGentilico(String gentilico) {
        this.gentilico = gentilico;
    }

    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cidade)) {
            return false;
        }
        Cidade other = (Cidade) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
    
}
