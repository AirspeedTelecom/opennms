
/**
 * <p>MatchingSnmpEndPointValidationExpression class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
package org.opennms.netmgt.provision.adapters.link.endpoint;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.opennms.netmgt.provision.adapters.link.EndPoint;
import org.opennms.netmgt.provision.adapters.link.EndPointStatusException;
import org.opennms.netmgt.snmp.SnmpValue;

@XmlRootElement(name="match-oid")
public class MatchingSnmpEndPointValidationExpression extends EndPointValidationExpressionImpl {
    @XmlAttribute(name="oid")
    private String m_oid = null;

    /**
     * <p>Constructor for MatchingSnmpEndPointValidationExpression.</p>
     */
    public MatchingSnmpEndPointValidationExpression() {
    }
    
    /**
     * <p>Constructor for MatchingSnmpEndPointValidationExpression.</p>
     *
     * @param regex a {@link java.lang.String} object.
     * @param oid a {@link java.lang.String} object.
     */
    public MatchingSnmpEndPointValidationExpression(String regex, String oid) {
        setValue(regex);
        m_oid = oid;
    }

    /** {@inheritDoc} */
    public void validate(EndPoint endPoint) throws EndPointStatusException {
        SnmpValue snmpValue = endPoint.get(m_oid);
        if(snmpValue == null) {
            throw new EndPointStatusException("unable to validate endpoint " + endPoint + ": could not retreive a value from snmp agent that matches " + m_value);
        }
        String value = snmpValue.toString();
        if(value != null && value.matches(m_value)) {
            return;
        }
        throw new EndPointStatusException("unable to validate endpoint " + endPoint + ": " + m_value + " does not match value (" + value + ")");
    }

    /**
     * <p>toString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return "match(" + m_value + ")";
    }
}
