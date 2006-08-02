/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.dao.IToolImportSupportDAO;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ToolImportSupportDAO extends HibernateDaoSupport implements IToolImportSupportDAO
{
	private static final String LOAD_BY_OLD_SIG = "from tis in class ToolImportSupport where tis.supportsToolSignature=:supportsToolSignature";
	
	/** Get all the ToolImportSupport objects which record support for the given old tool signature */
    public List getToolSignatureWhichSupports(final String oldToolSignature) {
        return (List) getHibernateTemplate().execute(new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException
            {
                return session.createQuery(LOAD_BY_OLD_SIG)
                              .setString("supportsToolSignature",oldToolSignature)
                              .list();
            }
        });

    }

}
