/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.survey.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveySessionDAO;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.springframework.stereotype.Repository;

@Repository
public class SurveySessionDAOHibernate extends LAMSBaseDAO implements SurveySessionDAO{
	
	private static final String FIND_BY_SESSION_ID = "from " + SurveySession.class.getName() + " as p where p.sessionId=?";
	private static final String FIND_BY_CONTENT_ID = "from " + SurveySession.class.getName() + " as p where p.survey.contentId=?";
	
	public SurveySession getSessionBySessionId(Long sessionId) {
		List list = doFind(FIND_BY_SESSION_ID,sessionId);
		if(list == null || list.size() == 0)
			return null;
		return (SurveySession) list.get(0);
	}
	@SuppressWarnings("unchecked")
	public List<SurveySession> getByContentId(Long toolContentId) {
		return (List<SurveySession>) doFind(FIND_BY_CONTENT_ID,toolContentId);
	}
	
	public void delete(SurveySession session) {
		getSession().delete(session);
	}
	public void deleteBySessionId(Long toolSessionId) {
		this.removeObject(SurveySession.class,toolSessionId);
	}

}
