package ni.edu.uam.ADMINJAVIER.report.actions;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.openxava.actions.JasperReportBaseAction;

import java.util.HashMap;
import java.util.Map;

public class PrintVotoDetalleReportAction extends JasperReportBaseAction {
    @Override
    protected JRDataSource getDataSource() throws Exception {
        return new JREmptyDataSource();
    }

    @Override
    protected String getJRXML() throws Exception {
        return "JavierReportsVotoDetalle.jrxml";
    }

    @Override
    protected Map getParameters() throws Exception {
        return new HashMap<>();
    }

}
