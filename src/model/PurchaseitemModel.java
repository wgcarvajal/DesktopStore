/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Purchaseitem;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Wilson Carvajal
 */
public class PurchaseitemModel extends AbstractModel<Purchaseitem>{
    
    public PurchaseitemModel()
    {
        super(Purchaseitem.class);
    }
    
    
    
    public boolean deleteByPurId(Long purId)
    {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Query query = session.createQuery("DELETE FROM Purchaseitem p WHERE p.purchase.purId = "+(long)purId);
                query.executeUpdate();
                transaction.commit();
        } catch (Exception e) {
                if(transaction != null) {
                        transaction.rollback();
                }
                return false;
        } finally {
                session.close();
        }
        return true;
    }
    
}
