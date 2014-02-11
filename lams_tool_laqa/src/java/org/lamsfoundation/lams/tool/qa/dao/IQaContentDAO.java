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
package org.lamsfoundation.lams.tool.qa.dao;

import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaContent;

/**
 * 
 * @author Ozgur Demirtas
 * 
 */
public interface IQaContentDAO {
    public QaContent getQaByContentId(long qaId);

    public QaContent getQaBySession(Long sessionId);

    public void saveOrUpdateQa(QaContent qa);

    public void saveQa(QaContent qa);

    public void updateQa(QaContent qa);

    public void removeQa(Long qaContentId);

    public void deleteQa(QaContent qa);

    public void removeQaById(Long qaId);

    public void removeAllQaSession(QaContent content);

    public void deleteCondition(QaCondition condition);

    public void removeQuestionsFromCache(QaContent qaContent);
    
    public void delete(Object object);
}