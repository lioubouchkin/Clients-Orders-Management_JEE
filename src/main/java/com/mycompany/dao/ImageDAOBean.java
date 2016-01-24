package com.mycompany.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mycompany.entities.ClientBean;
import com.mycompany.entities.ImageBean;


@Stateless
public class ImageDAOBean {

    private static final String SQL_SELECT = "SELECT id, nom, path, id_client "
	    + "FROM image ORDER BY id";
    private static final String SQL_SELECT_PAR_ID = "SELECT id, nom, path "
	    + "FROM image WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO image (id, nom, path, id_client) "
	    + "VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE_PAR_ID = "DELETE FROM image WHERE id = ?";
    
    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "bdd_tp_j2ee_PU" )
    private EntityManager em;
    
    public void create(ImageBean image, ClientBean client) throws DAOException {
		image.setClient(client);
		try {
			em.persist(image);
			// System.out.println(preparedStatement.toString());
			// error while inserting into BDD
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
