package org.opennms.netmgt.jasper.jrobin;

import java.util.Date;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRAbstractQueryExecuter;

public class JRobinQueryExecutor extends JRAbstractQueryExecuter {

    protected JRobinQueryExecutor(JRDataset dataset, Map parametersMap) {
        super(dataset, parametersMap);
        parseQuery();
    }

    public boolean cancelQuery() throws JRException {
        return false;
    }

    public void close() {
        
    }

    public JRDataSource createDatasource() throws JRException {
        try {
            return new RrdXportCmd().executeCommand(getQueryString());
        } catch (Throwable e) {
            throw new JRException("Error creating JRobinDataSource", e);
        }
    }

    @Override
    protected String getParameterReplacement(String parameterName) {
        Object parameterVal = getParameterValue(parameterName);
        if(parameterVal instanceof Date) {
            Date date = (Date) parameterVal;
            return String.valueOf(date.getTime()/1000);
        }

        return String.valueOf(parameterVal);
    }


}
