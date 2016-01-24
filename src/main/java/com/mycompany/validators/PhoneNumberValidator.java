package com.mycompany.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator( value="com.mycompany.validators.PhoneNumberValidator" )
public class PhoneNumberValidator implements Validator {

    private static final String NUMBERS_ONLY = "Merci de saisir chiffres seulement.";
    private static final String INVALID_LENGTH = "Le numéro de téléphone doit contenir au moins 10 chiffres.";
    private static final String PHONE_PATTERN = "\\d{10,}";
    private static final String EMPTY_FIELD = "Merci de saisir un num\u00E9ro de t\u00E9l\u00E9phone.";
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
	    throws ValidatorException {
	String phone = (String) value;
	if ( phone==null ) {
	    throw new ValidatorException(
		    new FacesMessage( FacesMessage.SEVERITY_WARN, EMPTY_FIELD, null ) );
	} else if ( (phone.replaceAll("\\s", "").length()<10) ) {
	    throw new ValidatorException(
		    new FacesMessage( FacesMessage.SEVERITY_WARN, INVALID_LENGTH, null ) );
	} else if ( !phone.replaceAll("\\s", "").matches(PHONE_PATTERN) ) {
	    throw new ValidatorException(
		    new FacesMessage( FacesMessage.SEVERITY_WARN, NUMBERS_ONLY, null ) );
	}
    }
}
