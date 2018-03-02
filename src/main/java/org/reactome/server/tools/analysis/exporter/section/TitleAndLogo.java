package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.constant.Images;
import org.reactome.server.tools.analysis.exporter.constant.URL;
import org.reactome.server.tools.analysis.exporter.element.Title;
import org.reactome.server.tools.analysis.exporter.factory.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TitleAndLogo implements Section {
	private static final String TITLE = "Pathway Analysis Report";

	public void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) {

		// add Reactome logo
		Image image = Images.getLogo();
		image.scaleToFit((report.getCurrentPageArea().getWidth() + report.getLeftMargin() + report.getRightMargin()) / 3, report.getTopMargin());
		image.setFixedPosition(5, report.getCurrentPageArea().getHeight() + report.getBottomMargin() - 5);
		image.setAction(PdfAction.createURI(URL.REACTOME));
		report.add(image);

		// add report title
		report.add(new Title(TITLE));
	}

}