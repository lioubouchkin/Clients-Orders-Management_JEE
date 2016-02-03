package com.mycompany.validators;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

import com.mycompany.tools.UploadedPartTools;
/**
 * max authorized file size is 1 Mb = 1048576 byte;
 * 
 * 
 * @author pasha
 *
 */
@FacesValidator( value="com.mycompany.validators.UploadedImageValidator" )
public class UploadedImageValidator implements Validator {

    private static final String FILE_TYPE = "image";
    private static final String SIZE_ERROR = "Les données envoyées sont trop volumineuses (1 Mo max).";
    private static final String FILE_TYPE_ERROR = "Le format accepté est .jpeg, .jpg, .png .";
//    private static final String FILE_TYPE = "image";
    
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
	    throws ValidatorException {
	List <FacesMessage> msgs = new ArrayList<FacesMessage>();
	Part file = (Part) value;
	if (file != null) {
	    if (file.getSize() > 1048576) {
		msgs.add(new FacesMessage(SIZE_ERROR));
	    }
	    if ( !UploadedPartTools.formatAutorise(file, FILE_TYPE) ) {
		msgs.add(new FacesMessage(FILE_TYPE_ERROR));
	    }
	    if (!msgs.isEmpty()) {
		throw new ValidatorException(msgs);
	    }
	}
    }

}
