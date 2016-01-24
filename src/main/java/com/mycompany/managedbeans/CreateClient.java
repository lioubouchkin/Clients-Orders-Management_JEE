package com.mycompany.managedbeans;

import javax.ejb.EJB;
import javax.ejb.EJBHome;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.eclipse.persistence.internal.cache.Clearable;

import com.mycompany.dao.ClientDAOBean;
import com.mycompany.dao.ImageDAOBean;
import com.mycompany.entities.ClientBean;

@ManagedBean
@ViewScoped
//@RequestScoped
public class CreateClient {

    ClientBean client;
    
    
    @EJB
    private ClientDAOBean clientDAO;
    @EJB
    private ImageDAOBean imageDAO;
    
    public CreateClient() {
	client = new ClientBean();
    }
    
    public void create () {
	/* Création ou récupération de la session */
//	HttpSession session = (HttpSession)FacesContext.getCurrentInstance().
//		getExternalContext().getSession(false);
//	FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	
	clientDAO.create(client);
	FacesMessage message = new FacesMessage("Succès de la création de client");
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public ClientBean getClient() {
        return client;
    }
}
