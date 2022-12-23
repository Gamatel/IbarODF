package ibarodf.gui.table;

import ibarodf.core.IbarOdfCore;
import ibarodf.core.metadata.exception.NoSuchMetadataException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.*;

import ibarodf.core.IbarOdfResultParser;

public class TableModel extends AbstractTableModel {
    private static final String HEADER_COLUMNS_NAMES1 = "Propriété";
    private static final String HEADER_COLUMNS_NAMES2 = "Valeur";
    private static final String[] HEADER_COLUMNS_NAMES = {HEADER_COLUMNS_NAMES1, HEADER_COLUMNS_NAMES2};
    private static final String[] MODIFIABLE_PROPERTIES = {PropsName.PROPS_TITLE, PropsName.PROPS_CREATOR, PropsName.PROPS_SUBJECT, PropsName.PROPS_COMMENT, PropsName.PROPS_KEYWORDS};
    private static final String[] PROPS_REGULAR_FILE = {PropsName.PROPS_FILE_NAME,PropsName.PROPS_MIMETYPE, PropsName.PROPS_SIZE};
    private static final int NB_ROW_DEFAULT = 9;
    private final static int COLUMN_COUNT = 2;
    private final static int LABEL_COLUMN = 0;
    private final Map<String, String> propertiesHM = new LinkedHashMap<>();
    private final Map<String, String> labelTextHM = new HashMap<>();
    private final ArrayList<String> columnProp = new ArrayList<>();
    private final ArrayList<String> columnLabels = new ArrayList<>();
    private final ArrayList<String> columnValue = new ArrayList<>();
    private final JSONObject dataJson;
    private final Set<String> propsRegularFileSet = new HashSet<>();
    private boolean isAnOdfFile;
    private File path;

    public TableModel() {
        super();
        this.dataJson = new JSONObject("{}");

        fillingWithBlankRow();
    }

    public TableModel(JSONObject dataJson, boolean isAnOdfFile) {
        super();
        this.dataJson = dataJson;
        this.isAnOdfFile = isAnOdfFile;
        this.path = new File((String) IbarOdfResultParser.getPath(dataJson));

        Collections.addAll(propsRegularFileSet, PROPS_REGULAR_FILE);
        initProperties();
        initLabels();


        if (propertiesHM.containsKey(PropsName.PROPS_STATS))
            loadStatsJsonToProperties();
        if (propertiesHM.containsKey(PropsName.PROPS_HYPERLINKS))
            loadHyperTextJsonToProperties();

        if (isAnOdfFile)
            loadPropertiesToColumnsForOdf();
        else
            loadPropertiesToColumnsForRegular();

        fillingWithBlankRow();
    }

    private void initProperties() {

        propertiesHM.put(PropsName.PROPS_FILE_NAME, IbarOdfResultParser.getFileName(dataJson));
        propertiesHM.put(PropsName.PROPS_SIZE, String.valueOf(IbarOdfResultParser.getSize(dataJson)));
        propertiesHM.put(PropsName.PROPS_MIMETYPE, IbarOdfResultParser.getMimeType(dataJson));

       if (isAnOdfFile) {
           try {
               propertiesHM.put(PropsName.PROPS_TITLE, IbarOdfResultParser.getTitle(dataJson));
           } catch (NoSuchMetadataException e) {
               propertiesHM.put(PropsName.PROPS_TITLE, "");
           }

           try {
               propertiesHM.put(PropsName.PROPS_INITIAL_CREATOR, IbarOdfResultParser.getInitialCreator(dataJson));
           } catch (NoSuchMetadataException e) {
               propertiesHM.put(PropsName.PROPS_INITIAL_CREATOR, "");
           }
           try {
               propertiesHM.put(PropsName.PROPS_CREATOR, IbarOdfResultParser.getCreator(dataJson));
           } catch (NoSuchMetadataException e) {
               propertiesHM.put(PropsName.PROPS_CREATOR, "");
           }

           try {
               propertiesHM.put(PropsName.PROPS_SUBJECT, IbarOdfResultParser.getSubject(dataJson));
           } catch (NoSuchMetadataException e) {
               propertiesHM.put(PropsName.PROPS_SUBJECT, "");
           }

           try {
               propertiesHM.put(PropsName.PROPS_COMMENT, IbarOdfResultParser.getComments(dataJson));
           } catch (NoSuchMetadataException e) {
               propertiesHM.put(PropsName.PROPS_COMMENT, "");
           }

           try {
               propertiesHM.put(PropsName.PROPS_KEYWORDS, IbarOdfResultParser.getKeywordsList(dataJson).toString());
           } catch (NoSuchMetadataException e) {
               propertiesHM.put(PropsName.PROPS_KEYWORDS, "");
           }

           try {
               propertiesHM.put(PropsName.PROPS_CREATION_DATE, IbarOdfResultParser.getCreationDate(dataJson));
           } catch (NoSuchMetadataException e) {
               propertiesHM.put(PropsName.PROPS_CREATION_DATE, "");
           }

           try {
               propertiesHM.put(PropsName.PROPS_HYPERLINKS, IbarOdfResultParser.getHyperlink(dataJson).toString());
           } catch (NoSuchMetadataException e) {
               propertiesHM.put(PropsName.PROPS_HYPERLINKS, "");
           }

           try {
               propertiesHM.put(PropsName.PROPS_STATS, IbarOdfResultParser.getJsonArrayOfStatistics(dataJson).toString());
           } catch (NoSuchMetadataException e) {
               propertiesHM.put(PropsName.PROPS_STATS, "");
           }
       }
    }

    private void initLabels() {
        labelTextHM.put(PropsName.PROPS_FILE_NAME, "Nom du fichier");
        labelTextHM.put(PropsName.PROPS_SIZE, "Taille du fichier en ko");
        labelTextHM.put(PropsName.PROPS_MIMETYPE, "MIME type");
        labelTextHM.put(PropsName.PROPS_TITLE, "Titre");
        labelTextHM.put(PropsName.PROPS_INITIAL_CREATOR, "Créateur initial");
        labelTextHM.put(PropsName.PROPS_CREATOR, "Créateur");
        labelTextHM.put(PropsName.PROPS_SUBJECT, "Sujet");
        labelTextHM.put(PropsName.PROPS_COMMENT, "Commentaire");
        labelTextHM.put(PropsName.PROPS_KEYWORDS, "Mots clés");
        labelTextHM.put(PropsName.PROPS_HYPERLINKS_REFERENCE, "Hyper liens");
        labelTextHM.put(PropsName.PROPS_CREATION_DATE, "Date de création");

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

    private void loadStatsJsonToProperties() {
        String statsJsonStr = propertiesHM.get(PropsName.PROPS_STATS);
        JSONArray statsJsonArray = new JSONArray(statsJsonStr);

        int i = 0;
        while (!statsJsonArray.isNull(i)) {
           JSONObject statJsonObj = statsJsonArray.getJSONObject(i);
           Set<String> keys = statJsonObj.keySet();

           for(String key: keys) {
               propertiesHM.put(key, statJsonObj.getNumber(key).toString());
           }

           i++;
        }

        propertiesHM.put(PropsName.PROPS_STATS,"");
    }

    private void loadHyperTextJsonToProperties() {
        String hyperLinksJsonStr = propertiesHM.get(PropsName.PROPS_HYPERLINKS);
        JSONArray hyperLinksJsonArray = new JSONArray(hyperLinksJsonStr);
        StringBuilder hyperLinksBuffer = new StringBuilder();

        int i = 0;
        while (!hyperLinksJsonArray.isNull(i)) {
            JSONObject hyperLinksJsonObj = hyperLinksJsonArray.getJSONObject(i);
            String currentHyperLinks = hyperLinksJsonObj.getString(PropsName.PROPS_HYPERLINKS_REFERENCE);
            hyperLinksBuffer.append(currentHyperLinks).append("\n\n");

            i++;
        }

        propertiesHM.put(PropsName.PROPS_HYPERLINKS_REFERENCE, hyperLinksBuffer.toString());
        propertiesHM.put(PropsName.PROPS_HYPERLINKS,"");
    }

    private void loadPropertiesToColumnsForOdf() {
        Collection<String> propertiesKeys = propertiesHM.keySet();

        for(String key:propertiesKeys){
            String value = propertiesHM.get(key);
            String labelText = labelTextHM.get(key);
            boolean isLabelNull = labelText == null;

            if (!isLabelNull) {
                columnProp.add(key);
                columnLabels.add(labelText);
                columnValue.add(value);
            }
        }
    }

    private void loadPropertiesToColumnsForRegular() {
        for(String key:propsRegularFileSet){
            String value = propertiesHM.get(key);
            String labelText = labelTextHM.get(key);

            columnProp.add(key);
            columnLabels.add(labelText);
            columnValue.add(value);
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
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
        String prop = columnProp.get(rowIndex);
        String newValue = (String) aValue;
        columnValue.set(rowIndex, newValue);

        try {
            switch (prop) {
                case PropsName.PROPS_TITLE -> IbarOdfCore.changeTheTitleOfAnOdfFile(path.toPath(), newValue);
                case PropsName.PROPS_CREATOR -> IbarOdfCore.changeTheCreatorOfAnOdfFile(path.toPath(), newValue);
                case PropsName.PROPS_SUBJECT -> IbarOdfCore.changeTheSubjectOfAnOdfFile(path.toPath(), newValue);
                case PropsName.PROPS_COMMENT -> IbarOdfCore.changeTheCommentsOfAnOdfFile(path.toPath(), newValue);
                case PropsName.PROPS_KEYWORDS -> IbarOdfCore.changeTheKeywordsOfAnOdfFile(path.toPath(), newValue);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Welcome to Swing!");
        }
    }
}
