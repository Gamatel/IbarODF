package ibarodf.core.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfMetaDom;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;


/**
 * Unit test for simple App.
 */
public class MetaDataInitialCreatorTest {
    /**
     * Rigorous Test :-)
     */
	@Test
	public void testMetaDataCreator  () throws Exception {
		testSetter();
		testConstructor();
	}

	private String getAttributTitleInOdf(OdfDocument odf) throws Exception {
		OdfMetaDom metaDom = odf.getMetaDom();
		OdfOfficeMeta meta = new OdfOfficeMeta(metaDom);

		return meta.getInitialCreator();
	}

	private void testSetter() throws Exception{
		OdfDocument testOdf = OdfTextDocument.newTextDocument();
		OdfMetaDom metaDom = testOdf.getMetaDom();
		OdfOfficeMeta meta = new OdfOfficeMeta(metaDom);

		final String value1 = "un test";
		final String value2 = "un autre test";

		MetaDataInitialCreator metaData = new MetaDataInitialCreator(meta, value1);

		try {
			metaData.setValue(value2);
		} catch(Exception e){
			System.err.print(e.getMessage() + "\n");
			assertNull(e);
		}

		assertEquals(value2, metaData.getValue());
		assertEquals(value2, getAttributTitleInOdf(testOdf));
		assertEquals(metaData.getAttribut(), MetaDataInitialCreator.attr);
	}

	private void testConstructor() throws Exception{
		OdfDocument testOdf = OdfTextDocument.newTextDocument();
		OdfMetaDom metaDom = testOdf.getMetaDom();
		OdfOfficeMeta meta = new OdfOfficeMeta(metaDom);

		final String value = "test";

		MetaDataInitialCreator metaData = new MetaDataInitialCreator(meta, value);
		assertEquals(value, metaData.getValue());
		assertEquals(MetaDataInitialCreator.attr, metaData.getAttribut());
	}
}

