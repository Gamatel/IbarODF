package ibarodf.gui.table;

import ibarodf.core.file.AbstractGenericFile;
import ibarodf.core.file.Directory;
import ibarodf.core.metadata.MetadataComment;
import ibarodf.core.metadata.MetadataCreationDate;
import ibarodf.core.metadata.MetadataCreator;
import ibarodf.core.metadata.MetadataHyperlink;
import ibarodf.core.metadata.MetadataInitialCreator;
import ibarodf.core.metadata.MetadataKeyword;
import ibarodf.core.metadata.MetadataStats;
import ibarodf.core.metadata.MetadataSubject;
import ibarodf.core.metadata.MetadataTitle;
import ibarodf.core.metadata.object.Hyperlink;

public class PropsName {
    public static final String PROPS_FILE_NAME = AbstractGenericFile.FILE_NAME;
    public static final String PROPS_MIMETYPE = AbstractGenericFile.MIME_TYPE;
    public static final String PROPS_SIZE = AbstractGenericFile.SIZE;
    public static final String PROPS_TITLE = MetadataTitle.TITLE;
    public static final String PROPS_INITIAL_CREATOR = MetadataInitialCreator.INITIAL_CREATOR;
    public static final String PROPS_CREATOR = MetadataCreator.CREATOR;
    public static final String PROPS_SUBJECT = MetadataSubject.SUBJECT;
    public static final String PROPS_COMMENT = MetadataComment.COMMENTS;
    public static final String PROPS_KEYWORDS =  MetadataKeyword.KEYWORDS;
    public static final String PROPS_HYPERLINKS =  MetadataHyperlink.HYPERLINKS;
    public static final String PROPS_CREATION_DATE =  MetadataCreationDate.CREATION_DATE;

    public static final String PROPS_NUMBER_OF_SUBDIRECTORY =  Directory.SUBDIRECTORIES;
    public static final String PROPS_NUMBER_OF_REGULAR_FILES =  Directory.REGULAR_FILES;
    public static final String PROPS_NUMBER_OF_ODF_FILES =  Directory.ODF_FILES;
    public static final String PROPS_NUMBER_OF_WRONG_FILES =  Directory.WRONG_FILES;
    public static final String PROPS_TOTAL_NUMBER_OF_FILE =  Directory.TOTAL_NUMBER_OF_FILES;


    public static final String PROPS_STATS = MetadataStats.STATISTICS;
    public static final String PROPS_STATS_CELL_COUNT = MetadataStats.CELL_COUNT;
    public static final String PROPS_STATS_CHARACTER_COUNT = MetadataStats.CHARACTER_COUNT;
    public static final String PROPS_STATS_DRAWN_COUNT = MetadataStats.DRAWN_COUNT;
    public static final String PROPS_STATS_FRAME_COUNT = MetadataStats.FRAME_COUNT;
    public static final String PROPS_STATS_IMAGE_COUNT = MetadataStats.IMAGE_COUNT;
    public static final String PROPS_STATS_NONWHITESPACECHARACTER_COUNT = MetadataStats.NON_WHITE_SPACE_CHARACTER_COUNT;
    public static final String PROPS_STATS_OBJECT_COUNT = MetadataStats.OBJECT_COUNT;
    public static final String PROPS_STATS_OLEOBJECT_COUNT = MetadataStats.OLE_OBJECT_COUNT;
    public static final String PROPS_STATS_PAGE_COUNT = MetadataStats.PAGE_COUNT;
    public static final String PROPS_STATS_PARAGRAPH_COUNT = MetadataStats.PARAGRAPH_COUNT;
    public static final String PROPS_STATS_ROW_COUNT = MetadataStats.ROW_COUNT;
    public static final String PROPS_STATS_SENTENCE_COUNT = MetadataStats.SENTENCE_COUNT;
    public static final String PROPS_STATS_SYLLABLE_COUNT = MetadataStats.SYLLABLE_COUNT;
    public static final String PROPS_STATS_TABLE_COUNT = MetadataStats.TABLE_COUNT;
    public static final String PROPS_STATS_WORD_COUNT = MetadataStats.WORD_COUNT;
    public static final String PROPS_HYPERLINKS_REFERENCE = Hyperlink.REFERENCE;
}
