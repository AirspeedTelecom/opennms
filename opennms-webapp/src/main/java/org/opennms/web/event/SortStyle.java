package org.opennms.web.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/** Convenience class to determine sort style of a query. */
public enum SortStyle {
    SEVERITY("severity"),
    TIME("time"),
    NODE("node"),
    INTERFACE("interface"),
    SERVICE("service"),
    POLLER("poller"),
    ID("id"),
    REVERSE_SEVERITY("rev_severity"),
    REVERSE_TIME("rev_time"),
    REVERSE_NODE("rev_node"),
    REVERSE_INTERFACE("rev_interface"),
    REVERSE_SERVICE("rev_service"),
    REVERSE_POLLER("rev_poller"),
    REVERSE_ID("rev_id");

    private static final Map<String, SortStyle> m_sortStylesString;
    
    private String m_shortName;
    
    static {
        m_sortStylesString = new HashMap<String, SortStyle>();
        for (SortStyle sortStyle : SortStyle.values()) {
            m_sortStylesString.put(sortStyle.getShortName(), sortStyle);
            
        }
    }
    
    private SortStyle(String shortName) {
        m_shortName = shortName;
    }

    public String toString() {
        return ("SortStyle." + getName());
    }

    public String getName() {
        return name();
    }

    public String getShortName() {
        return m_shortName;
    }

    public static SortStyle getSortStyle(String sortStyleString) {
        Assert.notNull(sortStyleString, "Cannot take null parameters.");

        return m_sortStylesString.get(sortStyleString.toLowerCase());
    }

    /**
     * Convenience method for getting the SQL <em>ORDER BY</em> clause related
     * to a given sort style.
     */
    protected String getOrderByClause() {
        String clause = null;
    
        switch (this) {
        case SEVERITY:
            clause = " ORDER BY SEVERITY DESC";
            break;
    
        case REVERSE_SEVERITY:
            clause = " ORDER BY SEVERITY ASC";
            break;
    
        case TIME:
            clause = " ORDER BY EVENTTIME DESC";
            break;
    
        case REVERSE_TIME:
            clause = " ORDER BY EVENTTIME ASC";
            break;
    
        case NODE:
            clause = " ORDER BY NODELABEL ASC";
            break;
    
        case REVERSE_NODE:
            clause = " ORDER BY NODELABEL DESC";
            break;
    
        case INTERFACE:
            clause = " ORDER BY IPADDR ASC";
            break;
    
        case REVERSE_INTERFACE:
            clause = " ORDER BY IPADDR DESC";
            break;
    
        case SERVICE:
            clause = " ORDER BY SERVICENAME ASC";
            break;
    
        case REVERSE_SERVICE:
            clause = " ORDER BY SERVICENAME DESC";
            break;
    
        case POLLER:
            clause = " ORDER BY EVENTDPNAME ASC";
            break;
    
        case REVERSE_POLLER:
            clause = " ORDER BY EVENTDPNAME DESC";
            break;
    
        case ID:
            clause = " ORDER BY ALARMID DESC";
            break;
    
        case REVERSE_ID:
            clause = " ORDER BY ALARMID ASC";
            break;
    
        default:
            throw new IllegalArgumentException("Unknown SortStyle: " + this);
        }
    
        return clause;
    }
}
