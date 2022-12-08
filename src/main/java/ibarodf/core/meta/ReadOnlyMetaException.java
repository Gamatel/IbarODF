package ibarodf.core.meta;

public class ReadOnlyMetaException extends Exception {
	ReadOnlyMetaException(String attribut) {
		super(String.format("MetaData{0} is  read-only"));
	}
}
