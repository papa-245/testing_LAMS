package org.lamsfoundation.lams.tool.forum.actions;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.forum.service.ForumManager;
import org.lamsfoundation.lams.tool.forum.core.GenericObjectFactoryImpl;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.forms.MessageForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 24/06/2005
 * Time: 10:54:09
 * To change this template use File | Settings | File Templates.
 */
public class LearningAction extends Action {
  private static Logger log = Logger.getLogger(LearningAction.class.getName());
  private ForumManager forumManager;

  public void setForumManager(ForumManager forumManager) {
      this.forumManager = forumManager;
  }

  public LearningAction() {
       this.forumManager = (ForumManager) GenericObjectFactoryImpl.getInstance().lookup("forumManager");
      //GenericObjectFactoryImpl.getInstance().configure(this);
  }
   public final ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
  		String param = mapping.getParameter();
	  	if (param.equals("openForum")) {
       		return getForum(mapping, form, request, response);
        }
	  	if (param.equals("editMessage")) {
       		return editMessage(mapping, form, request, response);
        }
		if (param.equals("openMessage")) {
       		return getMessage(mapping, form, request, response);
        }
        if (param.equals("deleteMessage")) {
       		return deleteMessage(mapping, form, request, response);
        }
       	if (param.equals("post")) {
       		return post(mapping, form, request, response);
        }
	  	if (param.equals("reply")) {
       		return replyToMessage(mapping, form, request, response);
        }
		return mapping.findForward("error");
    }

    public ActionForward post(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws PersistenceException {
        Long topicId =  new Long((String) request.getParameter("topicId"));
        Long parentId = new Long((String) request.getParameter("parentId"));
        Message parent = forumManager.getMessage(parentId);
        MessageForm messageForm = new MessageForm();
        Message reply = new Message();
        reply.setParent(parent);
        reply.setSubject(parent.getSubject());
        messageForm.setTopicId(topicId);
        messageForm.setParentId(parentId);
        messageForm.setMessage(reply);
        request.setAttribute("messageForm", messageForm);
        return mapping.findForward("success");
    }

    public ActionForward getForum(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
          throws IOException, ServletException, Exception {
      Long forumId = new Long((String) request.getParameter("forumId"));
      Forum forum = forumManager.getForum(forumId);
      List topicList = this.forumManager.getTopics(forum.getId());
      ForumForm forumForm = new ForumForm();
      forumForm.setForum(forum);
      request.setAttribute("forum", forumForm);
      request.setAttribute("forumTopics", topicList);
      return mapping.findForward("success");
  }

    public ActionForward editMessage(ActionMapping mapping,
                                                 ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
                 throws IOException, ServletException, PersistenceException {
       MessageForm messageForm = (MessageForm) form;
       Message message = messageForm.getMessage();
       this.forumManager.editMessage(message);
       return mapping.findForward("success");
     }

     public ActionForward replyToMessage(ActionMapping mapping,
                                                 ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
             throws IOException, ServletException, PersistenceException {
         MessageForm messageForm = (MessageForm) form;
         Message message = messageForm.getMessage();
         Long parentId = messageForm.getParentId();
         Message reply = this.forumManager.replyToMessage(parentId, message);
         request.setAttribute("messageId", parentId);
         request.setAttribute("topicId", messageForm.getTopicId());
         request.setAttribute("message", reply.getParent());
         return mapping.findForward("success");
     }

     public ActionForward deleteMessage(ActionMapping mapping,
                                                 ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
                 throws IOException, ServletException, Exception {
         Long messageId = new Long((String) request.getParameter("messageId"));
         this.forumManager.deleteMessage(messageId);
         return mapping.findForward("success");
     }

    public ActionForward getMessage(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
           throws IOException, ServletException, Exception {
       Long topicId = new Long((String) request.getParameter("topicId"));

       /*
       Long messageId = new Long((String) request.getParameter("parentId"));
       Message message = forumManager.getMessage(messageId);
       */

       Message message = forumManager.getMessage(topicId);
       request.setAttribute("message", message);
       return mapping.findForward("success");
   }



}
