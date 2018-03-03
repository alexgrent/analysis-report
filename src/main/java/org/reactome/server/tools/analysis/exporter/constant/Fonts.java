package org.reactome.server.tools.analysis.exporter.constant;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import org.apache.commons.io.IOUtils;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterRuntimeException;

import java.io.IOException;

/**
 * One font just only can be used in one specific PDF document, so fonts need to
 * be re-initialise before use it.
 *
 * @author Pascual/Chuan-Deng dengchuanbio@gmail.com
 * @see Document
 */
public class Fonts {

	public static PdfFont ITALIC;
	public static PdfFont REGULAR;
    public static PdfFont BOLD;
    public static PdfFont LIGHT;

	public static void reload() {
		// Every PDF must load the fonts again, as they are hold by one, and only one document
		try {
			byte[] bytes;
			bytes = IOUtils.toByteArray(Fonts.class.getResourceAsStream("OpenSans-Regular.ttf"));
			REGULAR = PdfFontFactory.createFont(bytes, true);
			bytes = IOUtils.toByteArray(Fonts.class.getResourceAsStream("OpenSans-Bold.ttf"));
			BOLD = PdfFontFactory.createFont(bytes, true);
			bytes = IOUtils.toByteArray(Fonts.class.getResourceAsStream("OpenSans-Light.ttf"));
			LIGHT = PdfFontFactory.createFont(bytes, true);
			bytes = IOUtils.toByteArray(Fonts.class.getResourceAsStream("OpenSans-Italic.ttf"));
			ITALIC = PdfFontFactory.createFont(bytes, true);
		} catch (IOException e) {
			throw new AnalysisExporterRuntimeException("Internal error. Couldn't read fonts", e);
		}
	}
}
