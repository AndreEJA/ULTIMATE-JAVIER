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

public class PrintFacultadReportAction extends JasperReportBaseAction {

    @Inject
    private Tab tab;

    // Bean que Jasper va a usar
    public static class FacultadRow {
        private String facultad;
        private String descripcion;
        private String estado;

        public String getFacultad() { return facultad; }
        public void setFacultad(String facultad) { this.facultad = facultad; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }

    @Override
    protected JRDataSource getDataSource() throws Exception {
        TableModel model = tab.getTableModel();

        // Debug opcional
        System.out.println("=== Columnas del módulo Facultad ===");
        for (int i = 0; i < model.getColumnCount(); i++) {
            System.out.println("Columna " + i + ": " + model.getColumnName(i));
        }

        int idxName  = -1;
        int idxDesc  = -1;
        int idxState = -1;

        for (int i = 0; i < model.getColumnCount(); i++) {
            String col = model.getColumnName(i);
            if ("Name".equalsIgnoreCase(col)) {
                idxName = i;
            } else if ("Descripcion".equalsIgnoreCase(col) || "Description".equalsIgnoreCase(col)) {
                idxDesc = i;
            } else if ("State".equalsIgnoreCase(col)) {
                idxState = i;
            }
        }

        List<FacultadRow> data = new ArrayList<>();

        for (int r = 0; r < model.getRowCount(); r++) {
            FacultadRow row = new FacultadRow();

            // Si no existe alguna columna, deja el valor vacío en lugar de reventar
            String nombre      = idxName  != -1  ? String.valueOf(model.getValueAt(r, idxName))  : "";
            String descripcion = idxDesc  != -1  ? String.valueOf(model.getValueAt(r, idxDesc))  : "";
            String estado      = idxState != -1  ? String.valueOf(model.getValueAt(r, idxState)) : "";

            row.setFacultad(nombre);
            row.setDescripcion(descripcion);
            row.setEstado(estado);

            data.add(row);
        }

        return new JRBeanCollectionDataSource(data);
    }

    @Override
    protected String getJRXML() throws Exception {
        return "JavierReportsFacultad.jrxml";
    }

    @Override
    protected Map<String, Object> getParameters() throws Exception {
        return new HashMap<>();
    }
}
