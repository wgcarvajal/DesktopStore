/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Purchase;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Wilson Carvajal
 */
public class PurchaseModel extends AbstractModel<Purchase>{
    
    public PurchaseModel()
    {
        super(Purchase.class);
    }
    
    public List<Purchase> findSaleForResume(Long  cashierId)
    {
        Session session = null;
        Transaction transaction = null;
        List<Purchase> results;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Criteria cr = session.createCriteria(Purchase.class,"aliasPurchase");
                cr.createCriteria("aliasPurchase.user", "aliasUser");
                cr.createAlias("aliasPurchase.client", "aliasClient",Criteria.LEFT_JOIN);
                cr.add(Restrictions.eq("aliasPurchase.purState", 0));
                cr.add(Restrictions.eq("aliasPurchase.user.usId", cashierId));
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
        return results;
    }
    
    
    public List<Purchase> findPurshaseUsIdAndDay(Long cashierId,Date initialDate, Date endDate)
    {
       Session session = null;
        Transaction transaction = null;
        List<Purchase> results;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Criteria cr = session.createCriteria(Purchase.class,"aliasPurchase");
                cr.createCriteria("aliasPurchase.user", "aliasUser");
                cr.add(Restrictions.ge("aliasPurchase.purDate", initialDate));
                cr.add(Restrictions.le("aliasPurchase.purDate", endDate));
                cr.add(Restrictions.eq("aliasPurchase.user.usId", cashierId));
                cr.add(Restrictions.eq("aliasPurchase.purState", 1));
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
        return results;
    }
}
