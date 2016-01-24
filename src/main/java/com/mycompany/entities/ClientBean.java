package com.mycompany.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.validator.FacesValidator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="client")
public class ClientBean implements Serializable {
    
    @Column(name="id", nullable=false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="nom", nullable=false, length=20)
    @NotNull(message="Veuillez saisir un nom d'utilisateur")
    @Size( min = 2, message = "Le nom de client doit contenir au moins 2 characters." )
    private String nom;
    
    @Column(name="prenom", length=20)
    @Size( min = 2, message = "Le pr\u00E9nom de client doit contenir au moins 2 characters." )
    private String prenom;
    
    @Column(name="email", length=60)
    @Pattern( regexp = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)",
	    message = "Merci de saisir une adresse mail valide" )
    private String eMail;
    
    @Column(name="adresse", nullable=false, length=200)
    @NotNull(message="Veuillez saisir l'adresse de livraison")
    @Size( min = 10, message = "L'adresse de livraison doit contenir au moins 10 characters." )
    private String adresseLivraison;
    
    @Column(name="telephone", nullable=false, length=10)	
    @NotNull
    private String telephone;
    
    // orphanRemoval=true and cascade=CascadeType.REMOVE are identical;
    // 	the disconnected Image instance is automatically removed :
    //		- whether the Client object is deleted,
    //		- or the image field of the Client object is set to null or
    //			to another Image Object;
    @OneToOne (mappedBy="client", orphanRemoval=true, cascade = CascadeType.PERSIST)
    private ImageBean image;
    
    // 'Commande' object via its property named 'client' is related
    //		to this class (Client)
    @OneToMany (mappedBy="client", orphanRemoval=true, cascade = CascadeType.PERSIST)
    private List<CommandeBean> commandes = new ArrayList<CommandeBean>();
    
    public ClientBean() {
    }
    
    public void addCommandes (CommandeBean commande) {
	commande.setClient(this);
	commandes.add(commande);
	
    }
    public List<CommandeBean> getCommandes () {
	return commandes;
    }
    
    public String getNom() {
	return nom;
    }

    public void setNom(String nom) {
	this.nom = nom;
    }

    public String getPrenom() {
	return prenom;
    }

    public void setPrenom(String prenom) {
	this.prenom = prenom;
    }

    public String geteMail() {
	return eMail;
    }

    public void seteMail(String eMail) {
	this.eMail = eMail;
    }

    public String getAdresseLivraison() {
	return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
	this.adresseLivraison = adresseLivraison;
    }

    public String getTelephone() {
	return telephone;
    }

    public void setTelephone(String telephone) {
	this.telephone = telephone;
    }
    
    public void setId(Long id) {
	this.id = id;
    }

    public Long getId() {
	return id;
    }
    
    public ImageBean getImage() {
        return image;
    }
    public void setImage(ImageBean img) {
	    img.setClient(this);
	    this.image = img;
    }

    @Override
    public String toString() {
	return "Id:" + this.id + "\n" + "Nom: " + this.nom + "\n " + "Prenom: " + this.prenom + "\n "
		+ "Télephone: " + this.telephone + "\n " + "Email: "
		+ this.eMail + "\n " + "Adresse livraison: "
		+ this.adresseLivraison + "\n ";
    }
}
