/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Pricepurchase;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Wilson Carvajal
 */
public class PricePurchaseModel extends AbstractModel<Pricepurchase>{
    
    public PricePurchaseModel()
    {
        super(Pricepurchase.class);
    }
    
    public Pricepurchase findCurrentByProdId(long prodId)
    {
        Session session = null;
        Transaction transaction = null;
        List<Pricepurchase> results;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Criteria cr = session.createCriteria(Pricepurchase.class,"aliasPricepurchase");
                cr.createCriteria("aliasPricepurchase.product", "aliasProduct");
                cr.add(Restrictions.eq("aliasProduct.prodId", prodId));
                cr.add(Restrictions.isNull("aliasPricepurchase.pricePurFinalDate"));
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
        return (results!=null && !results.isEmpty())?results.get(0):null;
    }
    
}
