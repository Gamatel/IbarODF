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
public class MetaDataTitleTest  
{
    /**
     * Rigorous Test :-)
     */
	@Test
	public void testMetaDataTitle  () throws Exception {
		testSetter();
		testConstructor();
	}

	private String getAttributTitleInOdf(OdfDocument odf) throws Exception {
		OdfMetaDom metaDom = odf.getMetaDom();
		OdfOfficeMeta meta = new OdfOfficeMeta(metaDom);

		return meta.getTitle();
	}

	private void testSetter() throws Exception{
		OdfDocument testOdf = OdfTextDocument.newTextDocument();
		OdfMetaDom metaDom = testOdf.getMetaDom();
		OdfOfficeMeta meta = new OdfOfficeMeta(metaDom);

		final String value1 = "un test";
		final String value2 = "un autre test";

		MetaDataTitle metaDataTitle = new MetaDataTitle(meta, value1);

		try {
			metaDataTitle.setValue(value2);
		} catch(Exception e){
			System.err.print(e.getMessage() + "\n");
			assertNull(e);
		}

		assertEquals(value2, metaDataTitle.getValue());
		assertEquals(value2, getAttributTitleInOdf(testOdf));
		assertEquals(metaDataTitle.getAttribut(), MetaDataTitle.attr);
	}

	private void testConstructor() throws Exception{
		OdfDocument testOdf = OdfTextDocument.newTextDocument();
		OdfMetaDom metaDom = testOdf.getMetaDom();
		OdfOfficeMeta meta = new OdfOfficeMeta(metaDom);

		final String value = "test";

		MetaDataTitle metaDataTitle = new MetaDataTitle(meta, value);
		assertEquals(value, metaDataTitle.getValue());
		assertEquals(MetaDataTitle.attr, metaDataTitle.getAttribut());
	}
}




