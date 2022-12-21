package ibarodf.gui.table;

import org.json.JSONObject;

import javax.swing.table.AbstractTableModel;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public class TableModel extends AbstractTableModel {
    private static final String[] HEADER_COLUMNS_NAMES = {"Propriété", "Valeur"};
    private static final String[] MODIFIABLE_PROPERTIES = {"Title", "Creator", "Subject", "Comment", "Keywords"};
    private static final int NB_ROW_DEFAULT = 9;
    private final Map<String, String> propertiesHM = new LinkedHashMap<>();
    private final Map<String, String> labelTextHM = new HashMap<>();
    private final ArrayList<String> columnProp = new ArrayList<>();
    private final ArrayList<String> columnLabels = new ArrayList<>();
    private final ArrayList<String> columnValue = new ArrayList<>();
    private final JSONObject dataJson;
    private final static int COLUMN_COUNT = 2;
    private final static int LABEL_COLUMN = 0;

    public TableModel() {
        super();
        this.dataJson = new JSONObject("{}");

        fillingWithBlankRow();
    }

    public TableModel(JSONObject dataJson) {
        super();
        this.dataJson = dataJson;

        initProperties();
        initLabels();
        loadJsonToProperties();
        loadPropertiesToColumns();
        fillingWithBlankRow();
    }

    private void initProperties() {
        propertiesHM.put(PropsName.PROPS_MIMETYPE, "");
        propertiesHM.put(PropsName.PROPS_TITLE, "");
        propertiesHM.put(PropsName.PROPS_INITIALCREATOR, "");
        propertiesHM.put(PropsName.PROPS_CREATOR, "");
        propertiesHM.put(PropsName.PROPS_SUBJECT, "");
        propertiesHM.put(PropsName.PROPS_COMMENT, "");
        propertiesHM.put(PropsName.PROPS_KEYWORDS, "");
        propertiesHM.put(PropsName.PROPS_HYPERLINKS, "");
        propertiesHM.put(PropsName.PROPS_CREATIONDATE, "");

        propertiesHM.put(PropsName.PROPS_STATS, "");
        propertiesHM.put(PropsName.PROPS_STATS_CELL_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_CHARACTER_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_DRAWN_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_FRAME_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_IMAGE_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_NONWHITESPACECHARACTER_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_OBJECT_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_OLEOBJECT_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_PAGE_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_PARAGRAPH_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_ROW_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_SENTENCE_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_SYLLABLE_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_TABLE_COUNT, "");
        propertiesHM.put(PropsName.PROPS_STATS_WORD_COUNT, "");
    }

    private void initLabels() {
        labelTextHM.put(PropsName.PROPS_MIMETYPE, "MIME type");
        labelTextHM.put(PropsName.PROPS_TITLE, "Titre");
        labelTextHM.put(PropsName.PROPS_INITIALCREATOR, "Créateur initial");
        labelTextHM.put(PropsName.PROPS_CREATOR, "Créateur");
        labelTextHM.put(PropsName.PROPS_SUBJECT, "Sujet");
        labelTextHM.put(PropsName.PROPS_COMMENT, "Commentaire");
        labelTextHM.put(PropsName.PROPS_KEYWORDS, "Mots clés");
        labelTextHM.put(PropsName.PROPS_HYPERLINKS, "Hyper liens");
        labelTextHM.put(PropsName.PROPS_CREATIONDATE, "Date de création");

        labelTextHM.put(PropsName.PROPS_STATS, "Statistiques");
        labelTextHM.put(PropsName.PROPS_STATS_CELL_COUNT, "Nombre de cellules");
        labelTextHM.put(PropsName.PROPS_STATS_CHARACTER_COUNT, "Nombre de charactère");
        labelTextHM.put(PropsName.PROPS_STATS_DRAWN_COUNT, "Nombre de dessins");
        labelTextHM.put(PropsName.PROPS_STATS_FRAME_COUNT, "Nombre de diapos");
        labelTextHM.put(PropsName.PROPS_STATS_IMAGE_COUNT, "Nombre d'images");
        labelTextHM.put(PropsName.PROPS_STATS_NONWHITESPACECHARACTER_COUNT, "Nombre de charactère hors espaces");
        labelTextHM.put(PropsName.PROPS_STATS_OBJECT_COUNT, "Nombre d'objets");
        labelTextHM.put(PropsName.PROPS_STATS_OLEOBJECT_COUNT, "C'est bizarre");
        labelTextHM.put(PropsName.PROPS_STATS_PAGE_COUNT, "Nombre de pages");
        labelTextHM.put(PropsName.PROPS_STATS_PARAGRAPH_COUNT, "Nombre de paragraphes");
        labelTextHM.put(PropsName.PROPS_STATS_ROW_COUNT, "Nombre de lignes");
        labelTextHM.put(PropsName.PROPS_STATS_SENTENCE_COUNT, "Nombre de phrases");
        labelTextHM.put(PropsName.PROPS_STATS_SYLLABLE_COUNT, "Nombre de syllabe");
        labelTextHM.put(PropsName.PROPS_STATS_TABLE_COUNT, "Nombre de table");
        labelTextHM.put(PropsName.PROPS_STATS_WORD_COUNT, "Nombre de mots");
    }

    private void loadJsonToProperties() {
        Collection<String> propertiesKeys = propertiesHM.keySet();
        boolean propsContainStats = !dataJson.isNull(PropsName.PROPS_STATS);

        if (!dataJson.isEmpty()) {
            for(String key:propertiesKeys){
                if (!dataJson.isNull(key))
                    propertiesHM.put(key, dataJson.getString(key));
            }
        }

        if (propsContainStats)
            loadStatsJsonToProperties();
    }

    private void loadStatsJsonToProperties() {
        String statsJsonStr = propertiesHM.get(PropsName.PROPS_STATS);
        JSONObject statsJson = new JSONObject(statsJsonStr);
        Collection<String> propertiesKeys = propertiesHM.keySet();

        for(String key:propertiesKeys){
            if (!statsJson.isNull(key))
                propertiesHM.put(key, statsJson.getString(key));
        }
    }

    private void loadPropertiesToColumns() {
        Collection<String> propertiesKeys = propertiesHM.keySet();

        for(String key:propertiesKeys){
            String value = propertiesHM.get(key);
            boolean isValueNull = value.equals("");
            System.out.println(value);

            if (!isValueNull) {
                String labelText = labelTextHM.get(key);

                columnProp.add(key);
                columnLabels.add(labelText);
                columnValue.add(value);
            }
        }
    }


   void fillingWithBlankRow() {
        int nbRow = columnLabels.size();

       for (int i = 0; i < NB_ROW_DEFAULT - nbRow; i++) {
           columnProp.add("");
           columnLabels.add("");
           columnValue.add("");
       }
   }

    private boolean isPropModifiable(String prop) {
        for (String modifiableProperty : MODIFIABLE_PROPERTIES)
            if (prop.equals(modifiableProperty))
                return true;
        return false;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == LABEL_COLUMN)
            return false;

        String prop = columnProp.get(row);

        System.out.println(isPropModifiable(prop));

        return isPropModifiable(prop);
    }

    @Override
    public int getRowCount() {
        return columnValue.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        ArrayList<String> column = i1 == 0 ? columnLabels : columnValue;

        return column.get(i);
    }

    @Override
    public String getColumnName(int column) {
        return HEADER_COLUMNS_NAMES[column];
    }
// @Override
   // public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
   //     super.setValueAt(aValue, rowIndex, columnIndex);
   //     String prop = (String) getValueAt(rowIndex, LABEL_COLUMN);
   //
   //    switch (prop) {
   //        case MODIFIABLE_PROPERTIES[0]:
   //            IbarODFCore.
   //
   //    }
   // }
}
