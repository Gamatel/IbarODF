package ibarodf.core.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class MetaDataAbstractTest  
{
    /**
     * Rigorous Test :-)
     */
	@Test
	public void testUnlockedMetaDataTest  () throws Exception {
		testConstructorUnLock();
		testSetterUnLock();
		testConstructorLock();
		testSetterLock();
	}

	private void testSetterUnLock() {
		final String attribut = "meta:keyword";
		final String value = "test";
		final String value2 = "autre test";

		MetaDataAbstract meta = new UnlockedMetaData(attribut, value);
		try {
			meta.setValue(value2);
		} catch(Exception e){
			System.err.print(e.getMessage() + "\n");
			assertNull(e);
		}

		assertEquals(meta.getAttribut(), attribut);
		assertEquals(meta.getValue(), value2);
	}

	private void testConstructorUnLock() {
		final String attribut = "meta:keyword";
		final String value = "test";

		MetaDataAbstract meta = new UnlockedMetaData(attribut, value);
		assertEquals(meta.getAttribut(), attribut);
		assertEquals(meta.getValue(), value);
	}

	private void testSetterLock() {
		final String attribut = "meta:keyword";
		final String value = "test";
		final String value2 = "autre test";

		MetaDataAbstract meta = new LockedMetaData(attribut, value);
		try {
			meta.setValue(value2);
		} catch(Exception e){
			System.err.print(e.getMessage() + "\n");
			assertNotNull(e);
		}

		assertEquals(meta.getAttribut(), attribut);
		assertEquals(meta.getValue(), value);
	}

	private void testConstructorLock() {
		final String attribut = "meta:keyword";
		final String value = "test";

		MetaDataAbstract meta = new LockedMetaData(attribut, value);
		assertEquals(meta.getAttribut(), attribut);
		assertEquals(meta.getValue(), value);
	}
}
