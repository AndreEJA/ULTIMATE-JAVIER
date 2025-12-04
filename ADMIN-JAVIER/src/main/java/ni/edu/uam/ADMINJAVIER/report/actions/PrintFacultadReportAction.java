package ni.edu.uam.ADMINJAVIER.report.actions;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.tab.Tab;

import javax.inject.Inject;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintFacultadReportAction extends JasperReportBaseAction {

    @Inject
    private Tab tab;

    @Override
    protected JRDataSource getDataSource() throws Exception {
        return new JRTableModelDataSource(tab.getTableModel());
    }

    @Override
    protected String getJRXML() throws Exception {
        return "JavierReportsFacultad.jrxml";
    }

    @Override
    protected Map getParameters() throws Exception {
        return new HashMap<>();
    }
}


