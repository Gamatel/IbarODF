package ibarodf.core.meta;

public class LockedMetaData extends MetaDataAbstract {
	public LockedMetaData(String attribut, String value) {
		super(attribut, value);
	}

	@Override
	public void setValue(String value) throws LockMetaDataException {
		throw new LockMetaDataException(this);		
	}
}

