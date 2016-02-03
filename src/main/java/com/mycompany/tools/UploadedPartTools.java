package com.mycompany.tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.Part;

import eu.medsea.mimeutil.MimeUtil;

public class UploadedPartTools {

    /*
     * Méthode utilitaire qui a pour unique but d'analyser l'en-tête
     * "content-disposition", et de vérifier si le paramètre "filename" y est
     * présent. Si oui, alors le champ traité est de type File et la méthode
     * retourne son nom, sinon il s'agit d'un champ de formulaire classique et
     * la méthode retourne null.
     * @return String : original file name with extension
     * @return null : in case of error 
     */
    public static String getName( Part part ) {
	String fileName = null;
	/* Boucle sur chacun des paramètres de l'en-tête "content-disposition". */
	for ( String contentDisposition :
	    	part.getHeader("content-disposition").split(";") ) {
	    /* Recherche de l'éventuelle présence du paramètre "filename". */
	    if (contentDisposition.trim().startsWith("filename")) {
		/*
		 * Si "filename" est présent, alors renvoi de sa valeur,
		 * c'est-à-dire du nom de fichier sans guillemets.
		 */
		fileName = contentDisposition
			.substring(contentDisposition.indexOf('=') + 1).trim()
			.replace("\"", "");
		/*
		 * Antibug pour Internet Explorer, qui transmet le chemin du
		 * fichier local à la machine du client...
		 * 
		 * Ex : C:/dossier/sous-dossier/fichier.ext
		 * 
		 * On doit donc faire en sorte de ne sélectionner que le nom et
		 * l'extension du fichier, et de se débarrasser du superflu.
		 */
		return fileName.substring(fileName.lastIndexOf('/') + 1)
			.substring(fileName.lastIndexOf('\\') + 1);
	    }
	}
	/* Et pour terminer, si rien n'a été trouvé... */
	return fileName;
    }
    
    /**
     * 
     * @param part : the part to examine ;
     * @param fileType : file type (image/text/etc..) that is looked for ;
     * @return : true - if the selected image file extension is one of the allowed image extensions,
     * 		false - otherwise.
     */
    public static boolean formatAutorise ( Part part, String fileType ) {
	
	InputStream fileContent = null;
	try {
	    fileContent = part.getInputStream();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	/* Extraction du type MIME du fichier depuis l'InputStream nommé "contenu" */
	MimeUtil.registerMimeDetector( "eu.medsea.mimeutil.detector.MagicMimeMimeDetector" );
	Collection<?> mimeTypes = MimeUtil.getMimeTypes( new BufferedInputStream(fileContent) );

	/*
	 * Si le fichier est bien une image, alors son en-tête MIME
	 * commence par la chaîne "image"
	 */
	if ( mimeTypes.toString().startsWith( fileType ) ) {
	    return true;
	} else {
	    return false;
	}
    }
}
