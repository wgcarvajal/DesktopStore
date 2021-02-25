/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Client;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author aranda
 */
public class ClientModel extends AbstractModel<Client>{
    
    public ClientModel()
    {
        super(Client.class);
    }
    
    public Client findByCliIdentity( String cliIdentity ){
        Session session = null;
        Transaction transaction = null;
        List<Client> results;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Criteria cr = session.createCriteria(Client.class,"aliasClient");
                cr.add(Restrictions.eq("aliasClient.cliIdentity", cliIdentity));
                results = cr.list();
                transaction.commit();
        } catch (Exception e) {
                results = null;
                if(transaction != null) {
                        transaction.rollback();
                }
        } finally {
                session.close();
        }
        return results!=null && results.size() > 0 ? results.get(0):null;
    }
    
}
