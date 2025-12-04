package ni.edu.uam.ADMINJAVIER.report.actions;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.tab.Tab;

import javax.inject.Inject;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintCandidatoReportAction extends JasperReportBaseAction {

    @Inject
    private Tab tab;

    // Bean que Jasper va a usar
    public static class CandidatoRow {
        private String evento;
        private String facultad;
        private String nombre;
        private String pareja;

        public String getEvento() { return evento; }
        public void setEvento(String evento) { this.evento = evento; }

        public String getFacultad() { return facultad; }
        public void setFacultad(String facultad) { this.facultad = facultad; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getPareja() { return pareja; }
        public void setPareja(String pareja) { this.pareja = pareja; }
    }

    @Override
    protected JRDataSource getDataSource() throws Exception {
        TableModel model = tab.getTableModel();

        // Buscamos los índices de las columnas por el texto del encabezado
        int idxEvento = -1, idxFacultad = -1, idxNombre = -1, idxPareja = -1;
        for (int i = 0; i < model.getColumnCount(); i++) {
            String col = model.getColumnName(i);
            if ("Evento".equalsIgnoreCase(col))   idxEvento   = i;
            else if ("Facultad".equalsIgnoreCase(col)) idxFacultad = i;
            else if ("Name".equalsIgnoreCase(col))      idxNombre   = i;
            else if ("Pareja".equalsIgnoreCase(col))    idxPareja   = i;
        }

        // Por si algo fallara, que no reviente con -1
        if (idxEvento   == -1) idxEvento   = 0;
        if (idxFacultad == -1) idxFacultad = 1;
        if (idxNombre   == -1) idxNombre   = 2;
        if (idxPareja   == -1) idxPareja   = 3;

        List<CandidatoRow> data = new ArrayList<>();

        for (int r = 0; r < model.getRowCount(); r++) {
            CandidatoRow row = new CandidatoRow();
            row.setEvento(String.valueOf(model.getValueAt(r, idxEvento)));
            row.setFacultad(String.valueOf(model.getValueAt(r, idxFacultad)));
            row.setNombre(String.valueOf(model.getValueAt(r, idxNombre)));
            row.setPareja(String.valueOf(model.getValueAt(r, idxPareja)));
            data.add(row);
        }

        return new JRBeanCollectionDataSource(data);
    }

    @Override
    protected String getJRXML() throws Exception {
        // Tus jrxml están en /reports según el proyecto
        return "reports/JavierReportsCandidato.jrxml";
    }

    @Override
    protected Map<String, Object> getParameters() throws Exception {
        return new HashMap<>();
    }
}
