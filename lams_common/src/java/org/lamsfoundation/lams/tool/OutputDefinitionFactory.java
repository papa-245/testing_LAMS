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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool;

import java.util.Locale;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.util.ILoadedMessageSourceService;
import org.lamsfoundation.lams.util.MessageService;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * This class forms the basic implementation of an output definition factory, which is the class in a tool that 
 * creates the output definitions for a tool. Each tool that has outputs should create its own factory class that
 * extends this class and uses the methods implemented in this class to create the actual ToolOutputDefinition objects.
 * 
 * The implemented factory (in the tool) needs to supply either (a) its own messageService bean (a Spring bean normally 
 * defined in the tool's applicationContext file in the toolMessageService property or (b) the name of its I18N language package/filename AND
 * the the loadedMessageSourceService (which is defined as a Spring bean in the core context). One of the two options (but not 
 * both) is required so that the getDescription() method can get the internationalised description of the output definition 
 * from the tool's internationalisation files. If neither the messageService or the I18N name is not supplied then the name 
 * if the output definition will appear in the description field.
 * 
 * Example definitions for tool factories:
 * 	<bean id="mcOuputDefinitionFactory" class="org.lamsfoundation.lams.tool.mc.service.MCOutputDefinitionFactory">
 *		<property name="loadedMessageSourceService"><ref bean="loadedMessageSourceService"/></property>
 *		<property name="languageFilename"><value>org.lamsfoundation.lams.tool.mc.ApplicationResources</value></property>
 *	</bean>
 *
 *  <bean id="forumOuputDefinitionFactory" class="org.lamsfoundation.lams.tool.forum.service.ForumOutputDefinitionFactory">
 *		<property name="toolMessageService"><ref bean="forumMessageService"/></property>
 *	</bean>
 *
 */
public abstract class OutputDefinitionFactory {

	protected Logger log = Logger.getLogger(OutputDefinitionFactory.class);

	private MessageService toolMessageService;

	private ILoadedMessageSourceService loadedMessageSourceService;
	private String languageFilename;
	protected final String KEY_PREFIX = "output.desc."; 
	
	/** Create a map of the tool output definitions, suitable for returning from the method 
	 * getToolOutputDefinitions() in the ToolContentManager interface. The class for the toolContentObject
	 * will be unique to each tool e.g. for Multiple Choice tool it will be the McContent object.
	 * 
	 * If the toolContentObject should not be null - if the toolContentId supplied in the call to 
	 * getToolOutputDefinitions(Long toolContentId) does not match to any existing content, the content
	 * should be the tool's default tool content.
	 * 
	 * @param toolContentObject
	 * @return SortedMap of ToolOutputDefinitions where the key is the name from each definition
	 * @throws ToolException
	 */
	public abstract SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject) throws ToolException;

	/** Tool specific toolMessageService, such as the forumMessageService in the Forum tool */
	public MessageService getToolMessageService() {
		return toolMessageService;
	}

	public void setToolMessageService(MessageService toolMessageService) {
		this.toolMessageService = toolMessageService;
	}


	/** Set the common loadedMessageSourceService, based on the bean defined in the common Spring context.
	 * If toolMessageService is not set, then the languageFilename 
	 * and loadedMessageSourceService should be set. */
	public ILoadedMessageSourceService getLoadedMessageSourceService() {
		return loadedMessageSourceService;
	}

	public void setLoadedMessageSourceService(
			ILoadedMessageSourceService loadedMessageSourceService) {
		this.loadedMessageSourceService = loadedMessageSourceService;
	}

	/** Set the filename/path for the tool's I18N files. If toolMessageService is not set, then the languageFilename 
	 * and loadedMessageSourceService should be set. */
	public String getLanguageFilename() {
		return languageFilename;
	}

	/** Get the filename and path for the tool's I18N files. */
	public void setLanguageFilename(String languageFilename) {
		this.languageFilename = languageFilename;
	}

	/** 
	 * Get the I18N description for this definitionName. If the tool has supplied a messageService, then this
	 * is used to look up the key and hence get the description. Otherwise if the tool has supplied a I18N 
	 * languageFilename then it is accessed via the shared toolActMessageService. If neither are supplied or 
	 * the key is not found, then any "." in the name are converted to space and this is used as the description.
	 * 
	 * The key must be in the format output.desc.[definition name]. For example a 
	 * definition name of "learner.mark" becomes output.desc.learner.mark.
	 */
	protected String getDescription(String definitionName) {
		MessageSource msgSource = null;
		if ( getToolMessageService() != null ) {
			msgSource = getToolMessageService().getMessageSource();
		}
		if ( msgSource == null && getLoadedMessageSourceService() != null && getLanguageFilename() != null) {
				msgSource = getLoadedMessageSourceService().getMessageService(getLanguageFilename());
		}
		if ( msgSource == null ) {
			log.warn("Unable to internationalise the description for the output definition "+definitionName+" as no MessageSource is available. "+
					"The tool's OutputDefinition factory needs to set either (a) messageSource or (b) loadedMessageSourceService and languageFilename.");
		}
		 
		String description = null;
		if ( msgSource != null ) {
			String key = KEY_PREFIX + definitionName;
			Locale locale = LocaleContextHolder.getLocale();
			try { 
				description = msgSource.getMessage(key,null,locale);
			} catch ( NoSuchMessageException e ) {
			}
		} 
		if ( description == null || description.length() == 0 ) {
			description = definitionName.replace('.', ' ');
		}
		
		return description;
	}

	/** Generic method for building a tool output definition. It will get the definition's description 
	 * from the I18N file using the getDescription() method.
	 * Only use if the other buildBlahDefinitions do not suit your needs. */
	protected ToolOutputDefinition buildDefinition(String definitionName, OutputType type, Object startValue, Object endValue, Object complexValue) {
		ToolOutputDefinition definition = new ToolOutputDefinition();
		definition.setName(definitionName);
		definition.setDescription(getDescription(definitionName));
		definition.setType(type);
		definition.setStartValue( startValue );
		definition.setEndValue( endValue );
		definition.setComplexDefinition( complexValue );
		return definition;
	}

	//The mark for a user's last attempt at answering the question(s).
	/** Build a tool definition designed for a range of integer values. 
	 * It will get the definition's description from the I18N file using the getDescription() method and
	 * set the type to OUTPUT_LONG. */
	protected ToolOutputDefinition buildRangeDefinition(String definitionName, Long startValue, Long endValue) {
		return buildDefinition(definitionName, OutputType.OUTPUT_LONG, startValue, endValue, null);
	}
	
	/** Build a tool definition designed for a range of string values. 
	 * It will get the definition's description from the I18N file using the getDescription() method and
	 * set the type to OUTPUT_LONG. */
	protected ToolOutputDefinition buildRangeDefinition(String definitionName, String startValue, String endValue) {
		return buildDefinition(definitionName, OutputType.OUTPUT_STRING, startValue, endValue, null);
	}

	/** Build a tool definition designed for a single double value, which is likely to be a statistic number of 
	 * questions answered
	 * It will get the definition's description from the I18N file using the getDescription() method and
	 * set the type to OUTPUT_LONG. */
	protected ToolOutputDefinition buildLongOutputDefinition(String definitionName) {
		return buildDefinition(definitionName, OutputType.OUTPUT_LONG, null, null, null);
	}

	/** Build a tool definition designed for a single double value, which is likely to be a statistic such as average
	 * number of posts. 
	 * It will get the definition's description from the I18N file using the getDescription() method and
	 * set the type to OUTPUT_DOUBLE. */
	protected ToolOutputDefinition buildDoubleOutputDefinition(String definitionName) {
		return buildDefinition(definitionName, OutputType.OUTPUT_DOUBLE, null, null, null);
	}

	/** Build a tool definition designed for a single boolean value, which is likely to be a test such as
	 * user has answered all questions correctly.
	 * It will get the definition's description from the I18N file using the getDescription() method and
	 * set the type to OUTPUT_BOOLEAN. */
	protected ToolOutputDefinition buildBooleanOutputDefinition(String definitionName) {
		return buildDefinition(definitionName, OutputType.OUTPUT_BOOLEAN, null, null, null);
	}

	/** Build a tool definition designed for a single String value.
	 * It will get the definition's description from the I18N file using the getDescription() method and
	 * set the type to OUTPUT_STRING. */
	protected ToolOutputDefinition buildStringOutputDefinition(String definitionName) {
		return buildDefinition(definitionName, OutputType.OUTPUT_STRING, null, null, null);
	}

	/** Build a tool definition for a complex value output. 
	 * It will get the definition's description from the I18N file using the getDescription() method and
	 * set the type to OUTPUT_COMPLEX. */
	protected ToolOutputDefinition buildComplexOutputDefinition(String definitionName) {
		return buildDefinition(definitionName, OutputType.OUTPUT_COMPLEX, null, null, null);
	}

}
