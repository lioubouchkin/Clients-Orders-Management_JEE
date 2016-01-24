package com.mycompany.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="image")
public class ImageBean implements Serializable {
    
    @Column
    // file name without extension
    private String nom;
    
    @Column
    @Id
    // MD5 hash value
    private String id;
    
    @Column
    // relative path, consists of 'id' + '.png'
    private String path;
    
    @OneToOne
    @JoinColumn(name="id_client")
    // foreign key references Client.id
    private ClientBean client;
    
    public ImageBean() {
    }
    
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public String toString() {
        return "nom: " + this.nom + "\n" +
        	"id: " + this.id + "\n" +
        	"path: " + this.path + "\n";
    }
    public ClientBean getClient() {
        return client;
    }
    public void setClient(ClientBean client) {
        this.client = client;
    }
}
