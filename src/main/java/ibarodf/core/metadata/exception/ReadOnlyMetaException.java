package ibarodf.core.metadata.exception;

public class ReadOnlyMetaException extends Exception {
	public ReadOnlyMetaException(String attribut) {
		super(String.format("Exception : MetaData{0} is  read-only"));
	}
}
