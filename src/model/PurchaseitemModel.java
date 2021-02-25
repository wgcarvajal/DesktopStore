/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Purchaseitem;
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
    
    public boolean deleteByPurIdAndProdId(Long purId,Long prodId)
    {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Query query = session.createQuery("DELETE FROM Purchaseitem p WHERE p.purchase.purId = "+(long)purId+" And p.product.prodId = "+(long)prodId);
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
    
    public List<Purchaseitem> findByPurId(Long purId)
    {
        Session session = null;
        Transaction transaction = null;
        List<Purchaseitem> results;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Criteria cr = session.createCriteria(Purchaseitem.class,"aliasPurchaseitem");
                cr.createCriteria("aliasPurchaseitem.purchase", "aliasPurchase");
                cr.createCriteria("aliasPurchaseitem.product", "aliasProduct");
                cr.createCriteria("aliasPurchaseitem.owner", "aliasOwner");
                cr.createCriteria("aliasProduct.unity", "aliasUnity");
                cr.createCriteria("aliasProduct.producttype", "aliasProducttype");
                cr.createCriteria("aliasProduct.owner", "aliasProductOwner");
                cr.add(Restrictions.eq("aliasPurchase.purId", purId));
                results = cr.list();
                transaction.commit();
        } catch (Exception e) {
                results = null;
                e.printStackTrace();
                if(transaction != null) {
                        transaction.rollback();
                }
        } finally {
                session.close();
        }
        return results;
    }
    
}
