//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2010 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 2010 Jan 06: Created file.  -jeffg@opennms.org
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//
package org.opennms.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>Abstract StringReplaceOperation class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public abstract class StringReplaceOperation {
    protected String m_pattern;
    protected String m_replacement;
    
    /**
     * <p>Constructor for StringReplaceOperation.</p>
     *
     * @param spec a {@link java.lang.String} object.
     */
    public StringReplaceOperation(String spec) {
        if (spec == null) spec = "";
        Matcher specMatcher = Pattern.compile("^s/([^/]+)/([^/]*)/$").matcher(spec);
        if (specMatcher.matches()) {
            m_pattern = specMatcher.group(1);
            m_replacement = specMatcher.group(2);
        } else {
            throw new IllegalArgumentException("Specification '" + spec + "' is invalid; must be of the form s/pattern/replacement/ with no trailing modifiers");
        }
    }
    
    /**
     * <p>getPattern</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPattern() {
        return m_pattern;
    }
    
    /**
     * <p>getReplacement</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getReplacement() {
        return m_replacement;
    }
    
    /**
     * <p>toString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return "Class: " + getClass().getName() + "; Pattern: " + m_pattern + "; Replacement: " + m_replacement;
    }
    
    /**
     * <p>replace</p>
     *
     * @param input a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public abstract String replace(String input);
    
    /**
     * <p>log</p>
     *
     * @return a {@link org.opennms.core.utils.ThreadCategory} object.
     */
    protected ThreadCategory log() {
        return ThreadCategory.getInstance();
    }
}
