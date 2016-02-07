package com.mycompany.filters;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.dao.ClientDAOBean;
import com.mycompany.dao.CommandeDAOBean;
import com.mycompany.entities.ClientBean;
import com.mycompany.entities.CommandeBean;

// session data is updated at the request to the following urlPatterns
@WebFilter(filterName="LoadClientsToSessionFilter", urlPatterns={"/listerClients.xhtml"})
//@WebFilter(filterName="LoadClientsToSessionFilter", urlPatterns={"/*"})
public class LoadClientsToSessionFilter implements Filter {

    private Map<Long, ClientBean> allClients;
    private Map<Long, CommandeBean> allOrders;
    // 'stateless' session bean injection
    @EJB
    private ClientDAOBean clientDAO;
    @EJB
    private CommandeDAOBean commandeDAO;
    private FilterConfig config;

    public static final String CONF_DAO_FACTORY = "daofactory";
    private static final String ATT_ALL_CLIENTS = "clients";
    private static final String ATT_ALL_ORDERS = "commandes";
    private Logger logger = LoggerFactory.getLogger(LoadClientsToSessionFilter.class);

    @Override
    public void destroy() {
    }

    // cache initialization with clients from the DB
    //		at the application startup (any application URI is requested);
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
	    FilterChain chain) throws IOException, ServletException {
	/* Cast des objets request et response */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        /* Non-filtrage des ressources statiques */
        String chemin = request.getRequestURI().substring( request.getContextPath().length() );
        if ( chemin.startsWith( "/include" ) ) {
            // continuation of executing the filter
            chain.doFilter( request, response );
//            return;
        }
        
        /* Création ou récupération de la session */
	HttpSession session = request.getSession();
//        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().
//        	 getExternalContext().getSession(false);
	
	// add an attribute to a session, if not exists
	if ( session.getAttribute(ATT_ALL_CLIENTS) == null ) {
	    this.allClients = new HashMap<Long, ClientBean>();
	    session.setAttribute(ATT_ALL_CLIENTS, allClients);

	    // get an existing attribute
	} else {
	    this.allClients = (Map<Long, ClientBean>)(session.getAttribute(ATT_ALL_CLIENTS));
	    this.allClients.clear();
	}
	
	// add an attribute to a session, if not exists
	if ( session.getAttribute(ATT_ALL_ORDERS) == null ) {
	    allOrders = new HashMap<Long, CommandeBean>();
	    session.setAttribute(ATT_ALL_ORDERS, allOrders);
	    
	    // get an existing attribute
	} else {
	    allOrders = (Map<Long, CommandeBean>)(session.getAttribute(ATT_ALL_ORDERS));
	    allOrders.clear();
	}
        this.allClients = this.clientDAO.mapClients(allClients);
        this.allOrders = this.commandeDAO.mapCommande(allOrders);
logger.info("clients mapped : " + this.allClients.size());
        chain.doFilter( request, response );
    }

    // load all clients from the BDD to the cache memory,
    // 		is executed only once at filter initialization;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//	this.config = filterConfig;
//	ServletContext context = this.config.getServletContext();

//	this.clientDAO = ((DAOFactory) context.getAttribute(CONF_DAO_FACTORY)).getClientDAO();
//	this.commandeDAO = ((DAOFactory) context.getAttribute(CONF_DAO_FACTORY)).getOrderDAO();
	
    }

    public void setFilterConfig(FilterConfig config) {
	this.config = config;
    }

    public FilterConfig getFilterConfig() {
	return config;
    }

}
