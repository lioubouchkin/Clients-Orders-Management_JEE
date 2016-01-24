package com.mycompany.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mycompany.entities.ClientBean;
import com.mycompany.entities.CommandeBean;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class CommandeDAOBean {

    // dd/MM/yyyy HH:mm:ss
    private static final String SQL_SELECT = "SELECT c "
	    + "FROM CommandeBean c "
	    + "ORDER BY c.id";
    private static final String SQL_SELECT_PAR_ID = "SELECT id, id_client, date, montant, mode_paiement, "
	    + "statut_paiement, mode_livraison, statut_livraison "
	    + "FROM Commande " + "WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO Commande (id_client, date, montant, mode_paiement, "
	    + "statut_paiement, mode_livraison, statut_livraison) "
	    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_PAR_ID = "DELETE FROM CommandeBean c WHERE c.id = :id";
    private static final String PARAM_COMMANDE_ID = "id";
    
    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "bdd_tp_j2ee_PU" )
    private EntityManager em;
    
    @Resource
    private UserTransaction transaction;
    
    public void create(CommandeBean commande, ClientBean client) throws DAOException, IllegalStateException, SecurityException, SystemException {
//	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
	// "yyyy-MM-dd HH:mm:ss" MySQL datetime pattern
//	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	DateTime dateTime = new DateTime();
	
	commande.setDate(dateTime);
	client.addCommandes(commande);
	try {
	    transaction.begin();
	    em.persist(commande);
	    transaction.commit();
	} catch (Exception e) {
	    e.printStackTrace();
	    transaction.rollback();
	}
    }
    
    public int delete(Long commandeId) throws DAOException, IllegalStateException, SecurityException, SystemException {
	int status = 0;
	CommandeBean commande = em.find(CommandeBean.class, commandeId);
	ClientBean clientBean = commande.getClient();
	try {
	    transaction.begin();
	    em.remove(em.merge(commande));
	    transaction.commit();
	    clientBean.getCommandes().remove(commande);
	    // returns (1) if one row is deleted;
	    return 1;
//System.out.println(preparedStatement.toString());
	} catch (Exception e) {
	    transaction.rollback();
	    e.printStackTrace();
	    // returns (0) if no row is deleted,
	    return 0;
	}
    }
    
    public Map<Long, CommandeBean> mapCommande (Map<Long, CommandeBean> commandes) {
	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
	List<CommandeBean> ordersList = new ArrayList<CommandeBean>();
	
	Query requete = em.createQuery (SQL_SELECT);
	try {
	    ordersList = requete.getResultList();
	    // error while inserting into BDD
	    for ( CommandeBean cmn: ordersList ) {
		
		// adding the retrieved order to the cache memory
		if ( !commandes.containsKey(cmn.getId()) ) {
		    commandes.put( cmn.getId(), cmn);
		}
	    }
	} catch (Exception e) {
	   e.printStackTrace();
//	    throw new DAOException(e);
	}
	return commandes;
    }
}
