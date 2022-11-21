package ibarodf.core.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class UnlockedMetaDataTest  
{
    /**
     * Rigorous Test :-)
     */
	@Test
	public void testUnlockedMetaDataTest  () throws Exception {
		testConstructor();
		testSetter();
	}

	private void testConstructor() {
		final String attribut = "meta:keyword";
		final String value = "test";

		UnlockedMetaData meta = new UnlockedMetaData(attribut, value);
		assertEquals(meta.getAttribut(), attribut);
		assertEquals(meta.getValue(), value);
	}

	private void testSetter() {
		final String attribut = "meta:keyword";
		final String value = "test";
		final String value2 = "autre test";

		UnlockedMetaData meta = new UnlockedMetaData(attribut, value);
		try {
			meta.setValue(value2);
		} catch(Exception e){
			System.err.print(e.getMessage() + "\n");
			assertNull(e);
		}

		assertEquals(meta.getAttribut(), attribut);
		assertEquals(meta.getValue(), value2);
	}
}
