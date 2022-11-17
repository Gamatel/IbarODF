package ibarodf.core.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class LockedMetaDataTest  
{
    /**
     * Rigorous Test :-)
     */
	@Test
	public void testLockedMetaDataTest  () throws Exception {
		testConstructeur();
		testSetter();
	}

	private void testConstructeur() {
		final String attribut = "meta:keyword";
		final String value = "test";

		LockedMetaData meta = new LockedMetaData(attribut, value);
		assertEquals(meta.getAttribut(), attribut);
		assertEquals(meta.getValue(), value);
	}

	private void testSetter() {
		final String attribut = "meta:keyword";
		final String value = "test";

		LockedMetaData meta = new LockedMetaData(attribut, value);

		try {
			meta.setValue("Bonjour");
		} catch(Exception e){
			System.err.print(e.getMessage() + "\n");
			assertNotNull(e);
		}

		assertEquals(meta.getAttribut(), attribut);
		assertEquals(meta.getValue(), value);
	}
}
