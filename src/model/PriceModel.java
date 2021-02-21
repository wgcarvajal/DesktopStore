/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Price;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Wilson Carvajal
 */
public class PriceModel extends AbstractModel<Price>{
    
    public PriceModel()
    {
        super(Price.class);
    }
    
    public Price findCurrentByProdId(long prodId)
    {
        Session session = null;
        Transaction transaction = null;
        List<Price> results;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Criteria cr = session.createCriteria(Price.class,"aliasPrice");
                cr.createCriteria("aliasPrice.product", "aliasProduct");
                cr.add(Restrictions.eq("aliasProduct.prodId", prodId));
                cr.add(Restrictions.isNull("aliasPrice.priceFinalDate"));
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
