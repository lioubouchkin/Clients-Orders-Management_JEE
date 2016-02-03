package com.mycompany.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.entities.ClientBean;
import com.mycompany.entities.CommandeBean;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ClientDAOBean {
    private Logger logger = LoggerFactory.getLogger(ClientDAOBean.class);
    
    private static final String SQL_SELECT = 
	    "SELECT c FROM ClientBean c ORDER BY c.nom";
    
    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "bdd_tp_j2ee_PU" )
    private EntityManager em;
    
    @Resource
    private UserTransaction transaction;

    // Enregistrement d'un nouvel utilisateur
    public void create ( ClientBean client ) throws DAOException {
        try {
            transaction.begin();
            em.persist( em.merge(client) );
            transaction.commit();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
    
    // Recherche d'un utilisateur à partir de son id
    public ClientBean read (Long clientId) throws DAOException {
	ClientBean client = null;
        try {
            client = em.find(ClientBean.class, clientId);
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return client;
    }
    
    // returns (0) if no row is deleted,
    // 	(1) if one row is deleted;
    // Container Managed Persistence Context with BeanManagedTransaction
    public int delete(Long clientId) throws IllegalStateException, SecurityException, SystemException {
	ClientBean client = em.find(ClientBean.class, clientId);
	try {
	    transaction.begin();
	    // merging an entity will reflect the latest state of the entity
	    //		with all its cascading associations;
	    em.remove(em.merge(client));
	    transaction.commit();
	    return 1;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    transaction.rollback();
	    return 0;
	}
    }
    
    /**
     * 
     * @param clientId : of the entity to update
     * @param obj : entity's field to update
     * @throws IllegalStateException
     * @throws SecurityException
     * @throws SystemException
     */
    public void update ( Long clientId, Object... obj ) throws IllegalStateException, SecurityException, SystemException {
	try {
	    ClientBean client = em.find(ClientBean.class, clientId);
	    transaction.begin();
	    client.getCommandes().add( (CommandeBean)obj[0] );
	    transaction.commit();
	} catch (Exception e) {
	    e.printStackTrace();
	    transaction.rollback();
	}
    }
    
    public Map<Long, ClientBean> mapClients ( Map <Long, ClientBean> clients ) {
	List <ClientBean> clientsList = new ArrayList<ClientBean>();
	
	Query requete = em.createQuery (SQL_SELECT);
	try {
	   clientsList = requete.getResultList();
//logger.info("mapping of the clients"); 
	   for ( ClientBean c : clientsList ) {
		// adding the retrieved client to the cache memory
		if ( !clients.containsKey(c.getId()) ) {
//logger.info(c.toString());
		    clients.put( c.getId(), c);
//		    System.out.println(c.toString());
		}
	   }
	} catch (Exception e) {
	   e.printStackTrace();
	}
	return clients;
    }
}
