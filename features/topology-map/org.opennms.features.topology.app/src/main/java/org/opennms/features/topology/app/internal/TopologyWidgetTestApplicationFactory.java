/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.features.topology.app.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.opennms.features.topology.api.TopologyProvider;
import org.opennms.features.topology.app.internal.support.IconRepositoryManager;
import org.ops4j.pax.vaadin.AbstractApplicationFactory;
import org.ops4j.pax.vaadin.ScriptTag;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;

public class TopologyWidgetTestApplicationFactory extends AbstractApplicationFactory {
    
	private TopologyProvider m_topologyProvider;
	private CommandManager m_commandManager = new CommandManager();
	private IconRepositoryManager m_iconRepositoryManager = new IconRepositoryManager();
	private WidgetManager m_widgetManager;
	private WidgetManager m_treeWidgetManager;
    private String m_themeName = "reindeer";
	
	public CommandManager getCommandManager() {
        return m_commandManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        m_commandManager = commandManager;
    }

    @Override
	public Application createApplication(HttpServletRequest request) throws ServletException {
    	LoggerFactory.getLogger(getClass()).debug("createApplication() for servlet path {}", request.getServletPath());
		TopologyWidgetTestApplication application = new TopologyWidgetTestApplication(m_commandManager, m_topologyProvider, m_iconRepositoryManager);
		application.setTheme(m_themeName);
		
		if(m_widgetManager != null) {
		    application.setWidgetManager(m_widgetManager);
		}
		
		if(m_treeWidgetManager != null) {
		    application.setTreeWidgetManager(m_treeWidgetManager);
		}
		
        LoggerFactory.getLogger(getClass()).debug("Application is " + application);
        return application;
	}

	@Override
	public Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
		return TopologyWidgetTestApplication.class;
	}

    public IconRepositoryManager getIconRepositoryManager() {
        return m_iconRepositoryManager;
    }

    public void setIconRepositoryManager(IconRepositoryManager iconRepositoryManager) {
        m_iconRepositoryManager = iconRepositoryManager;
    }
    
    public void setTheme(String themeName) {
        m_themeName = themeName;
    }

	public TopologyProvider getTopologyProvider() {
		return m_topologyProvider;
	}

	public void setTopologyProvider(TopologyProvider topologyProvider) {
		m_topologyProvider = topologyProvider;
	}

    public WidgetManager getWidgetManager() {
        return m_widgetManager;
    }

    public void setWidgetManager(WidgetManager widgetManager) {
        m_widgetManager = widgetManager;
    }

    @Override
    public Map<String, String> getAdditionalHeaders() {
        final Map<String,String> headers = new HashMap<String,String>();
        headers.put("X-UA-Compatible", "chrome=1");
        return headers;
    }

    @Override
    public List<ScriptTag> getAdditionalScripts() {
        final List<ScriptTag> tags = new ArrayList<ScriptTag>();
        tags.add(new ScriptTag("http://ajax.googleapis.com/ajax/libs/chrome-frame/1/CFInstall.min.js", "text/javascript", null));
        tags.add(new ScriptTag(null, "text/javascript", "CFInstall.check({ mode: \"overlay\" });"));
        return tags;
    }

    public WidgetManager getTreeWidgetManager() {
        return m_treeWidgetManager;
    }

    public void setTreeWidgetManager(WidgetManager treeWidgetManager) {
        m_treeWidgetManager = treeWidgetManager;
    }
}