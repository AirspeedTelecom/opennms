package org.opennms.tools.rrd.converter;

import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.builder.ToStringBuilder;

class RrdEntry implements Cloneable {
    private TreeMap<String, Double> m_entryMap;
    private long m_timestamp;
    private List<String> m_dsNames;

    RrdEntry(final long timestamp, final List<String> dsNames) {
        m_entryMap = new TreeMap<String,Double>();
        m_timestamp = timestamp;
        m_dsNames = dsNames;
    }
    
    public Double getValue(final String dsName) {
        return m_entryMap.get(dsName);
    }

    public void setValue(final String dsName, final double sample) {
        m_entryMap.put(dsName, sample);
    }

    public long getTimestamp() {
        return m_timestamp;
    }

    public void setTimestamp(final long timestamp) {
        m_timestamp = timestamp;
    }

    public List<String> getDsNames() {
        return m_dsNames;
    }

    public void coalesceWith(final RrdEntry otherEntry) {
        for (final String key : getDsNames()) {
            final Double myValue = m_entryMap.get(key);
            if (isNumber(myValue)) {
                continue;
            }
            final Double otherValue = otherEntry.getValue(key);
            if (isNumber(otherValue)) {
                setValue(key, otherValue);
            } else {
                setValue(key, Double.NaN);
            }
        }
    }
    
    private boolean isNumber(final Double number) {
        return number != null && !Double.isNaN(number);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("timestamp", m_timestamp)
            .append("entries", m_entryMap)
            .toString();
    }
}