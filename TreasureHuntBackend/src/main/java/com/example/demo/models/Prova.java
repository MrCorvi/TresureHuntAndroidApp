package com.example.demo.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="prova")
public class Prova implements Serializable {
    
    @Id
    @Column(name = "idprova", nullable =false)
    private String idprova;
    @Column(name = "testo")
    private String testo;
    @Column(name = "numero")
    private Integer numero;

    public Prova(){}

    public Prova(String idprova, String testo, Integer numero){
        
        this.idprova=idprova;
        this.testo=testo;
        this.numero=numero;
    }

    public String getIdProva(){return idprova;}
    public String getTesto(){return testo;}
    public Integer getNumero(){return numero;}

    public void setIdProva(String idprova){this.idprova=idprova;}
    public void setTesto(String testo){this.testo=testo;}
    public void setNumero(Integer numero){this.numero=numero;}
}
