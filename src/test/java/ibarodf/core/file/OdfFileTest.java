package ibarodf.core.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import org.junit.Test;

import ibarodf.core.meta.MetaDataTitle;


/**
 * Unit test for simple App.
 */
public class OdfFileTest {
	private final String path = "test.odt";

	@Test
	public void testOdfFileTest() throws Exception{
		testConstructor();
		testGetterFake();
		testGetterNotFake();
		testSetMetaData();
	}

	public void testConstructor() {

		try {
			OdfFile fileOdf = new OdfFile(path);
			assertEquals(path, fileOdf.getPath());

		} catch(Exception e){
			System.err.print(e.getMessage());
			assertNull(e);
		}
	}

	public void testGetterFake() throws Exception{
		OdfFile fileOdf = new OdfFile(path);
		final String fakeAttribut = "FakeTitle";
		boolean isFake = false; // the responce is yes
		
		try {
			fileOdf.getMetaData(fakeAttribut);
		} catch(Exception e){
			System.err.print(e.getMessage());
			isFake = true;
			assertNotNull(e);
		}

		assertTrue(isFake);
	}

	public void testGetterNotFake() throws Exception{
		OdfFile fileOdf = new OdfFile(path);
		final String fakeAttribut = MetaDataTitle.attr;
		boolean isFake = false; // the responce is yes
		
		try {
			fileOdf.getMetaData(fakeAttribut);
		} catch(Exception e){
			System.err.print(e.getMessage());
			isFake = true;
			assertNotNull(e);
		}

		assertFalse(isFake);
	}

	public void testSetMetaData() throws Exception {
		OdfFile fileOdf = new OdfFile(path);
		final String value = "Le Nouveau test d'Alexandre";

		fileOdf.setMetaData(MetaDataTitle.attr, value);
		final String testValue = fileOdf.getMetaData(MetaDataTitle.attr);
		assertEquals(value, testValue);
	}

	// TODO Tester saveChange saveCghangeInOtherFile:  <24-11-22, yourname> //

}



