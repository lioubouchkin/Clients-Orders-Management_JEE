package com.mycompany.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mycompany.tools.JodaDateTimeConverter;

@Entity
@Table(name="commande")
public class CommandeBean implements Serializable {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( columnDefinition = "TIMESTAMP" )
    @Converter( name = "dateTimeConverter", converterClass = JodaDateTimeConverter.class )
    @Convert( "dateTimeConverter" )
    private DateTime date;
    @Column
    private String montant;
    @Column(name="mode_paiement")
    private String modePaiement;
    @Column(name="statut_paiement")
    private String statutPaiement;
    @Column(name="mode_livraison")
    private String modeLivraison;
    @Column(name="statut_livraison")
    private String statutLivraison;
    @ManyToOne
    @JoinColumn(name="id_client")
    private ClientBean client;
    
    public CommandeBean() {
    }

    public String getMontant() {
	return montant;
    }

    public void setMontant(String montant) {
	this.montant = montant;
    }

    public String getModePayement() {
	return modePaiement;
    }

    public void setModePayement(String modePayement) {
	this.modePaiement = modePayement;
    }

    public String getStatutPayement() {
	return statutPaiement;
    }

    public void setStatutPayement(String statutPayement) {
	this.statutPaiement = statutPayement;
    }

    public String getModeLivraison() {
	return modeLivraison;
    }

    public void setModeLivraison(String modeLivraison) {
	this.modeLivraison = modeLivraison;
    }

    public String getStatutLivraison() {
	return statutLivraison;
    }

    public void setStatutLivraison(String statutLivraison) {
	this.statutLivraison = statutLivraison;
    }

    public ClientBean getClient() {
	return client;
    }

    public void setClient(ClientBean client) {
	this.client = client;
    }

    public DateTime getDate() {
	return date;
    }

    public void setDate(DateTime date) {
	this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }
    
    @Override
    public String toString() {
	return this.client == null ? "Client : null" : this.client.toString() 
		+ "Date: " + this.date + "\n"
		+ "Montant: " + this.montant + "\n" + "Mode livraison: "
		+ this.modeLivraison + "\n" + "Statut livraison: "
		+ this.statutLivraison + "\n" + "Mode paiement: "
		+ this.modePaiement + "\n" + "Statut paiement: "
		+ this.statutPaiement + "\n";
    }
    
    /**
     * @return String : date in the format "yyyy-MM-dd HH:mm:ss"
     */
    public String getFormattedDate () {
	// "yyyy-MM-dd HH:mm:ss" MySQL datetime pattern
	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	return formatter.print(this.date);
    }
}
