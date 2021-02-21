/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.User;
import entities.Usergroup;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author aranda
 */
public class UserModel extends AbstractModel<User>{
    
    public UserModel()
    {
        super(User.class);
    }
    
    
    public Usergroup findbyUserNameAndPassword(String usUserName,String usPassword)
    {
        Session session = null;
        Transaction transaction = null;
        List<Usergroup> results;
        session = sessionFactory.openSession();
        try {
                transaction = session.beginTransaction();
                Criteria cr = session.createCriteria(Usergroup.class,"aliasUsergroup");
                cr.createCriteria("aliasUsergroup.user", "aliasUser");
                cr.createCriteria("aliasUsergroup.groupp", "aliasGroupp");
                cr.add(Restrictions.eq("aliasUser.usUserName", usUserName));
                cr.add(Restrictions.eq("aliasUser.usPassword", usPassword));
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
