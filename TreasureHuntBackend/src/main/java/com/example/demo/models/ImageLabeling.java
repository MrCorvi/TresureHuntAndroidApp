package com.example.demo.models;

import java.io.Serializable;
import org.json.simple.JSONObject; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/* ImageLabeling class */
@Entity
@Table(name="imagelabeling")
public class ImageLabeling implements Serializable {
    
    @Id
    @Column(name = "idLabel", nullable = false)
    protected Integer idLabel; 
    @Column(name = "labelText", nullable = false)
    private String labelText;


    public ImageLabeling(){}

    public ImageLabeling(Integer idLabel, String labelText){
        this.idLabel = idLabel;
        this.labelText=labelText;
    }

    public ImageLabeling(String labelText){
        this.labelText=labelText;
    }

    public String getLabelText(){return labelText;}
    public Integer getIdLabel(){return idLabel;}
    
    public void setLabelText(String labelText){this.labelText= labelText;}
    public void setIdLabel(Integer idLabel){this.idLabel=idLabel;}

    @Override
    public String toString() {
        return "{ 'idLabel': "+ idLabel  +",\n 'labelText': " + labelText + " }\n";
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        jo.put("labelText", labelText);
        jo.put("idLabel", idLabel);

        return jo;
    }

    
}





