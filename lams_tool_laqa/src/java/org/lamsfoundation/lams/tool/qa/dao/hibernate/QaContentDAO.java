/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.dao.IQaContentDAO;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;


/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class QaContentDAO extends HibernateDaoSupport implements IQaContentDAO {
	 	static Logger logger = Logger.getLogger(QaContentDAO.class.getName());
	 	
	 	private static final String LOAD_QA_BY_SESSION = "select qa from QaContent qa left join fetch "
            + "qa.qaSessions session where session.qaSessionId=:sessionId";

    
	 	private static final String COUNT_USER_RESPONSED = "select distinct u.userId from QaQueUsr u left join fetch"
            + " u.qaQueContent as ques where ques.qaContent = :qa group by u.userId";
	 	
	 	
	 	public QaContentDAO() {}

	 	public QaContent getQaById(long qaId)
	    {
	        return (QaContent) this.getHibernateTemplate()
	                                   .load(QaContent.class, new Long(qaId));
	    }
	 	
	 	/**
	 	 * 
	 	 * return null if not found
	 	 */
	 	public QaContent loadQaById(long qaId)
	    {
	    	logger.debug(logger + " " + this.getClass().getName() +  " " + "loadQaById called with: " + qaId);
	        return (QaContent) this.getHibernateTemplate().get(QaContent.class, new Long(qaId));
	    }

	 	
	 	
	 	public void updateQa(QaContent qa)
	    {
	        this.getHibernateTemplate().update(qa);
	    }
	 	

	 	 
	     public QaContent getQaBySession(final Long sessionId)
	     {
	         return (QaContent) getHibernateTemplate().execute(new HibernateCallback()
                              {

                                  public Object doInHibernate(Session session) throws HibernateException
                                  {
                                      return session.createQuery(LOAD_QA_BY_SESSION)
                                                    .setLong("sessionId",
                                                     sessionId.longValue())
                                                    .uniqueResult();
                                  }
                              });
	     }

	    public void saveQa(QaContent qa) 
	    {
	    	this.getHibernateTemplate().save(qa);
	    }
	    
	    public void createQa(QaContent qa) 
	    {
	    	this.getHibernateTemplate().save(qa);
	    }
	    
	    public void UpdateQa(QaContent qa)
	    {
	    	this.getHibernateTemplate().update(qa);	
	    }

	    public int countUserResponsed(QaContent qa)
	    {
    	   return (getHibernateTemplate().findByNamedParam(COUNT_USER_RESPONSED,
                "qa",
                qa)).size();
	    }
	    
	    

	    /** GETS CALLED BY CONTRACT
	     */
	    public void removeAllQaSession(QaContent qaContent){
	    	logger.debug(logger + " " + this.getClass().getName() +  " " + "before removing all sessions: ");
	    	this.getHibernateTemplate().deleteAll(qaContent.getQaSessions());	
	    }
	    
	    public void removeQa(Long qaId)
	    {
	    	String query = "from qa in class org.lamsfoundation.lams.tool.qa.QaContent"
	            + " where qa.qaContentId = ?";
	            this.getHibernateTemplate().delete(query,qaId,Hibernate.LONG);
    	}
	    
	    public void deleteQa(QaContent qaContent)
	    {
	            this.getHibernateTemplate().delete(qaContent);
    	}

	    public void removeQaById(Long qaId)
	    {
	        String query = "from qa in class org.lamsfoundation.lams.tool.qa.QaContent"
	        + " where qa.qaContentId = ?";
	        this.getHibernateTemplate().delete(query,qaId,Hibernate.LONG);
	    }

	    
	    public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
	
	    
} 