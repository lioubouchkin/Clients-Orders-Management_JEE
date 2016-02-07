package com.mycompany.managedbeans;

import java.io.Serializable;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;

import com.mycompany.dao.ClientDAOBean;
import com.mycompany.entities.ClientBean;

@ManagedBean
@SessionScoped
public class DeleteClient implements Serializable {

    private static final long serialVersionUID = 1L;
    private ClientBean client;
    private Map<Long, ClientBean> allClients;
    private static final String ATT_ALL_CLIENTS = "clients";
    
    @EJB
    private ClientDAOBean clientDAO;
    
    public String delete (Long clientId) {
	/* Création ou récupération de la session */
	HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
		.getExternalContext().getSession(false);
	this.allClients = (Map<Long, ClientBean>)session.getAttribute(ATT_ALL_CLIENTS);
	try {
	    if ( this.clientDAO.delete(clientId) == 1 ) {
		this.allClients.remove(clientId);
	    }
	} catch (IllegalStateException | SecurityException | SystemException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

}
