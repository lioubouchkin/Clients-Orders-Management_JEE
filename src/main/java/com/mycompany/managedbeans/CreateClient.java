package com.mycompany.managedbeans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.dao.ClientDAOBean;
import com.mycompany.dao.ImageDAOBean;
import com.mycompany.entities.ClientBean;
import com.mycompany.entities.ImageBean;
import com.mycompany.tools.UploadedPartTools;

@ManagedBean
@ViewScoped
// @RequestScoped
public class CreateClient implements Serializable {

    private static final long serialVersionUID = 1L;
    private ClientBean client;
    private ImageBean image;
    private Part uploadedPhoto;
    private Logger logger = LoggerFactory.getLogger(CreateClient.class);
    private Map<Long, ClientBean> allClients;
    
    private static final int TAILLE_TAMPON = 10240; // 10 ko
//    private static final String FILE_LOCATION = "E:\\MyDocs\\Spring\\downloads\\finished\\";
    private static final String FILE_LOCATION = FacesContext.getCurrentInstance()
            .getExternalContext().getInitParameter("uploadsPath");
    private static final String ATT_ALL_CLIENTS = "clients";
    
    @EJB
    private ClientDAOBean clientDAO;
    @EJB
    private ImageDAOBean imageDAO;

    public CreateClient() {
	this.client = new ClientBean();
	this.image = null;
    }

    public void create() {
	// original file name with extension
	String fileName;
	/* Création ou récupération de la session */
	HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
		.getExternalContext().getSession(false);
	allClients = (Map<Long, ClientBean>)session.getAttribute(ATT_ALL_CLIENTS);
	
	fileName = (uploadedPhoto!=null) ? UploadedPartTools.getName(uploadedPhoto) : null;
	if (fileName != null && !fileName.isEmpty()) {
	    this.image = new ImageBean();
	    // filename bean property ( file name without the extension )
	    this.image.setNom(fileName.substring(0, fileName.indexOf(".")));
	    this.client.setImage(this.image);
	}
	// writing the fileImage onto the HardDrive if the file is selected
	if (this.client.getImage() != null) {
	    try {
		// initilization of imageId and imagePath,
		// writing the image to the HD;
		this.saveFile(fileName);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	clientDAO.create(this.client);
	FacesMessage message = new FacesMessage(
		"Succès de la création de client");
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /*
     * Méthode utilitaire qui a pour but d'écrire le fichier passé en paramètre
     * sur le disque, dans le répertoire donné et avec le nom donné.
     * @param fileName : file name with the extension
     */
    private void saveFile( String fileName) throws Exception {
        /* Prépare les flux. */
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        InputStream contenuFichier = null;
        byte[] tampon;
        int longueur;
        String newFileName;
        try {
            String hashFileName = this.getFileHash();
            newFileName = hashFileName + fileName.substring(fileName.indexOf("."));
            /* Ouvre les flux. 
             * file name : generated hash value plus original extension
             * */
            sortie = new BufferedOutputStream(
        	    new FileOutputStream(new File(FILE_LOCATION + newFileName) ),
                    TAILLE_TAMPON );
            // Image File content was all read (extracted) for writing the hash value
            contenuFichier = this.uploadedPhoto.getInputStream();
            entree = new BufferedInputStream( contenuFichier, TAILLE_TAMPON );
            /*
             * Lit le fichier reçu et écrit son contenu dans un fichier sur le
             * disque.
             */
            longueur = 0;
            tampon = new byte[TAILLE_TAMPON];
            while ( ( longueur = entree.read( tampon ) ) > 0 ) {
                sortie.write( tampon, 0, longueur );
            }
            this.image.setPath(newFileName);
        } finally {
            try {
                sortie.close();
            } catch ( IOException ignore ) {
            }
            try {
                entree.close();
            } catch ( IOException ignore ) {
            }
        }
    }
    
    // generates uploaded image hash value
    private String getFileHash() throws Exception {
	/* Prépare les flux. */
	BufferedInputStream entree = null;
	InputStream contenuFichier = null;
	byte[] tampon;
	int longueur;
	String hashName;
//this.logger.info("ENTER getFileHash()");
	// initialisation of an object to create a MD5 hash value for a piece of
	// data
	MessageDigest md = MessageDigest.getInstance("MD5");
	try {
	 // Image File content was all read (extracted) for writing the hash value
            contenuFichier = this.uploadedPhoto.getInputStream();
	    /* Ouvre les flux. */
	    entree = new BufferedInputStream(contenuFichier, TAILLE_TAMPON);
	    /*
	     * Lit le fichier reçu et écrit son contenu dans un fichier sur le
	     * disque.
	     */
	    tampon = new byte[TAILLE_TAMPON];
	    longueur = 0;
	    while ((longueur = entree.read(tampon)) > 0) {
		// generating a hash value for the processed data
		md.update(tampon, 0, longueur);
	    }

	    // completes the hash computation
	    byte[] mdbytes = md.digest();
	    // convert the byte to hex format method 1
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < mdbytes.length; i++) {
		sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
			.substring(1));
	    }
	    this.image.setId(sb.toString());

	    // set file name to generated hash value, so the file name is a unique one
	    hashName = sb.toString();
	} finally {
	    try {
		entree.close();
	    } catch (IOException ignore) {
	    }
	}
	return hashName;
    }
    
    public ClientBean getClient() {
	return client;
    }

    public Part getUploadedPhoto() {
	return uploadedPhoto;
    }

    public void setUploadedPhoto(Part uploadedPhoto) {
	this.uploadedPhoto = uploadedPhoto;
    }

    public void setClient(ClientBean client) {
	this.client = client;
    }
}
