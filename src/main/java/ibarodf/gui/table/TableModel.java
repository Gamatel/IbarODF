package ibarodf.gui.table;

import ibarodf.core.IbarOdfCore;
import ibarodf.core.metadata.MetadataComment;
import ibarodf.core.metadata.MetadataCreator;
import ibarodf.core.metadata.MetadataKeyword;
import ibarodf.core.metadata.MetadataSubject;
import ibarodf.core.metadata.MetadataTitle;
import ibarodf.core.metadata.exception.NoSuchMetadataException;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

import ibarodf.core.IbarOdfResultParser;

public class TableModel extends AbstractTableModel {
    private static final String HEADER_COLUMNS_NAMES1 = "Propriété";
    private static final String HEADER_COLUMNS_NAMES2 = "Valeur";
    private static final String[] HEADER_COLUMNS_NAMES = { HEADER_COLUMNS_NAMES1, HEADER_COLUMNS_NAMES2 };
    private static final String[] MODIFIABLE_PROPERTIES = { PropsName.PROPS_TITLE, PropsName.PROPS_CREATOR,
            PropsName.PROPS_SUBJECT, PropsName.PROPS_COMMENT, PropsName.PROPS_KEYWORDS };
    private static final String[] PROPS_REGULAR_FILE = { PropsName.PROPS_FILE_NAME, PropsName.PROPS_MIMETYPE,
            PropsName.PROPS_SIZE };
    private static final String[] PROPS_DIRECTORY = {PropsName.PROPS_NUMBER_OF_SUBDIRECTORY, PropsName.PROPS_NUMBER_OF_REGULAR_FILES, PropsName.PROPS_NUMBER_OF_ODF_FILES, PropsName.PROPS_NUMBER_OF_WRONG_FILES, PropsName.PROPS_TOTAL_NUMBER_OF_FILE};
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

        Collections.addAll(propsRegularFileSet, PROPS_REGULAR_FILE);
        fillingWithBlankRow();
    }

    public TableModel(JSONObject dataJson) {
        super();
        this.dataJson = dataJson;
        this.isAnOdfFile = IbarOdfResultParser.isOdfFile(dataJson);
        this.path = new File(((Path) IbarOdfResultParser.getPath(dataJson)).toUri());

        Collections.addAll(propsRegularFileSet, PROPS_REGULAR_FILE);
        initProperties();
        initLabels();
        if (isAnOdfFile) {
            loadPropertiesToColumnsForOdf();
        }else if(IbarOdfResultParser.isDirectory(dataJson)){
            loadPropertiesToColumnsForDirectory();
        }else{
            loadPropertiesToColumnsForRegular();
        }
        fillingWithBlankRow();
    }

    private void addKeywordToProperties() {
        try {
            List<Object> keywords = IbarOdfResultParser.getKeywordsList(dataJson);
            StringBuilder keywordsText = new StringBuilder();
            for (int index = 0, indexMax = keywords.size(); index < indexMax; index++) {
                if (index != indexMax - 1) {
                    keywordsText.append(keywords.get(index) + ",");
                } else {
                    keywordsText.append(keywords.get(index));
                }
            }
            propertiesHM.put(PropsName.PROPS_KEYWORDS, keywordsText.toString());
        } catch (NoSuchMetadataException e) {
            JOptionPane.showMessageDialog(null, e, "Can't access metadata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addHyperlinksToProperties() {
        try {
            JSONArray hyperlinks = IbarOdfResultParser.getHyperlink(dataJson);
            for (int index = 0, indexMax = hyperlinks.length(); index < indexMax; index++) {
                propertiesHM.put(PropsName.PROPS_HYPERLINKS_REFERENCE + (index + 1),
                        IbarOdfResultParser.getHyperlinkReference(hyperlinks.getJSONObject(index)).toString());
            }
        } catch (NoSuchMetadataException e) {
            JOptionPane.showMessageDialog(null, e, "Can't access metadata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStatisticsToProperties() {
        try {
            JSONArray statistics = IbarOdfResultParser.getJsonArrayOfStatistics(dataJson);
            JSONObject currentObject;
            String key;
            for (int index = 0, indexMax = statistics.length(); index < indexMax; index++) {
                currentObject = statistics.getJSONObject(index);
                key = currentObject.keys().next();
                propertiesHM.put(key, currentObject.get(key).toString());
            }
        } catch (NoSuchMetadataException e) {
            JOptionPane.showMessageDialog(null, e, "Can't access metadata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initProperties() {

        propertiesHM.put(PropsName.PROPS_FILE_NAME, IbarOdfResultParser.getFileName(dataJson));
        propertiesHM.put(PropsName.PROPS_SIZE, String.valueOf(IbarOdfResultParser.getSize(dataJson)));
        propertiesHM.put(PropsName.PROPS_MIMETYPE, IbarOdfResultParser.getMimeType(dataJson));
        if (IbarOdfResultParser.isDirectory(dataJson)){
            propertiesHM.put(PropsName.PROPS_NUMBER_OF_SUBDIRECTORY,""+IbarOdfResultParser.getNumberOfSubDirectories(dataJson));
            propertiesHM.put(PropsName.PROPS_NUMBER_OF_REGULAR_FILES,""+IbarOdfResultParser.getNumberOfRegularFile(dataJson));
            propertiesHM.put(PropsName.PROPS_NUMBER_OF_ODF_FILES,""+IbarOdfResultParser.getNumberOfOdfFiles(dataJson));
            propertiesHM.put(PropsName.PROPS_NUMBER_OF_WRONG_FILES,""+IbarOdfResultParser.getNumberOfWrongFiles(dataJson));
            propertiesHM.put(PropsName.PROPS_TOTAL_NUMBER_OF_FILE,""+IbarOdfResultParser.getTotalNumberOfFiles(dataJson));
        }else if (isAnOdfFile) {
            List<Object> listMetadata = IbarOdfResultParser.getCurrentOdfMetadata(dataJson);
            Iterator<Object> listMetadataIt = listMetadata.iterator();
            String currentKey;
            while (listMetadataIt.hasNext()) {
                currentKey = listMetadataIt.next().toString();
                try {
                    if (IbarOdfResultParser.isHyperlinkKey(currentKey)) {
                        addHyperlinksToProperties();
                    } else if (IbarOdfResultParser.isStatisticsKey(currentKey)) {
                        addStatisticsToProperties();
                    } else if (IbarOdfResultParser.isKeywordKey(currentKey)) {
                        addKeywordToProperties();
                    } else {
                        propertiesHM.put(currentKey,
                            IbarOdfResultParser.getMetadataByType(dataJson, currentKey).get(currentKey).toString());
                    }
                } catch (NoSuchMetadataException e) {
                    JOptionPane.showMessageDialog(null, e, "Can't access metadata", JOptionPane.ERROR_MESSAGE);
                }
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
        labelTextHM.put(PropsName.PROPS_HYPERLINKS_REFERENCE, "Hyperliens");
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

        labelTextHM.put(PropsName.PROPS_NUMBER_OF_SUBDIRECTORY, "Nombre de sous répertoires");
        labelTextHM.put(PropsName.PROPS_NUMBER_OF_REGULAR_FILES, "Nombre de fichiers Réguliers (non ODF)");
        labelTextHM.put(PropsName.PROPS_NUMBER_OF_ODF_FILES, "Nombre de fichier ODF");
        labelTextHM.put(PropsName.PROPS_NUMBER_OF_WRONG_FILES, "Nombre de fichier éronnés");
        labelTextHM.put(PropsName.PROPS_TOTAL_NUMBER_OF_FILE, "Nombre de total de fichier");

    
    }

    private void loadPropertiesToColumnsForOdf() {
        Collection<String> propertiesKeys = propertiesHM.keySet();
        String value;
        String labelText;
        boolean isLabelNull;
        for(String key:propertiesKeys){
            value = propertiesHM.get(key);
            if(key.contains(PropsName.PROPS_HYPERLINKS_REFERENCE)){
                labelText = labelTextHM.get(PropsName.PROPS_HYPERLINKS_REFERENCE);
            }else{
                labelText = labelTextHM.get(key);
            }
            isLabelNull = labelText == null;
            if (!isLabelNull) {
                columnProp.add(key);
                columnLabels.add(labelText);
                columnValue.add(value);
            }
        }
        loadMissingMetadata();
    }


    private void loadMissingMetadata(){
        String[] haveToHave = {MetadataTitle.TITLE, MetadataSubject.SUBJECT, MetadataKeyword.KEYWORDS,MetadataComment.COMMENTS,MetadataCreator.CREATOR};
        for(int index=0, indexMax=haveToHave.length; index<indexMax; index++){
            if(!propertiesHM.containsKey(haveToHave[index])){
                columnProp.add(haveToHave[index]);
                columnLabels.add(labelTextHM.get(haveToHave[index]));
                columnValue.add("");
            }
        }
        
    }
    
    private void loadPropertiesToColumnsForDirectory(){
        Collection<String> propertiesKeys = propertiesHM.keySet();
        Iterator<String> propertiesKeysIt = propertiesKeys.iterator(); 
        String currentKey;
        while(propertiesKeysIt.hasNext()){
            currentKey = propertiesKeysIt.next();
            columnProp.add(currentKey);
            columnLabels.add(labelTextHM.get(currentKey));
            columnValue.add(propertiesHM.get(currentKey));
        }


    }


    private void loadPropertiesToColumnsForRegular() {
        for (String key : propsRegularFileSet) {
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
    public String getValueAt(int i, int i1) {
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
