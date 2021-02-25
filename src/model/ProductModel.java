/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Product;
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
public class ProductModel extends AbstractModel<Product>{
    
    public ProductModel()
    {
        super(Product.class);
    }
    
    
    public Product findByBarCode(String prodBarCode)
    {
        Session session = null;
        Transaction transaction = null;
        List<Product> results;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Criteria cr = session.createCriteria(Product.class,"aliasProduct");
                cr.createCriteria("aliasProduct.unity", "aliasUnity");
                cr.createCriteria("aliasProduct.producttype", "aliasProducttype");
                cr.createCriteria("aliasProduct.owner", "aliasOwner");
                cr.add(Restrictions.eq("aliasProduct.prodBarCode", prodBarCode));
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
    
    public List findAllProducts()
    {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        List list = null;
        try {
                transaction = session.beginTransaction();
                Query query = session.createQuery("SELECT p,pr,b,c,u FROM Product p INNER JOIN p.prices pr "
                        + "INNER JOIN p.brand b "
                        + "INNER JOIN p.category c "
                        + "INNER JOIN p.unity u WHERE pr.product.prodId = p.prodId And pr.priceFinalDate IS NULL") ;
                list = query.list();
                transaction.commit();
        } catch (Exception e) {
                if(transaction != null) {
                        transaction.rollback();
                }
        } finally {
                session.close();
        }
        return list;
    }
    
}
