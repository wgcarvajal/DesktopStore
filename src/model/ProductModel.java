/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Product;
import java.util.List;
import org.hibernate.Criteria;
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
    
}
