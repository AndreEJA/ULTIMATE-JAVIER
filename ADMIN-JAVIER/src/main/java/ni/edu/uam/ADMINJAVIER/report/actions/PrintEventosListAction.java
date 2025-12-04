package ni.edu.uam.ADMINJAVIER.report.actions;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.tab.Tab;

import javax.inject.Inject;
import javax.swing.table.TableModel;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PrintEventosListAction extends JasperReportBaseAction {

    @Inject
    private Tab tab;

    @Override
    protected JRDataSource getDataSource() throws Exception {
        TableModel model = tab.getTableModel();

        return new JRTableModelDataSource(model);
    }
    @Override
    protected String getJRXML() throws Exception {
        return "reports/JavierReportsEventos.jrxml";
    }

    @Override
    protected Map getParameters() throws Exception {
         return new HashMap<>();
    }
}
