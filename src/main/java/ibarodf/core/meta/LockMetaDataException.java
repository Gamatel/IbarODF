package ibarodf.core.meta;

public class LockMetaDataException extends Exception {
	public LockMetaDataException (MetaDataAbstract meta) {
		super("This meta is not editable " + meta.toString() + ";");
	}	
}
