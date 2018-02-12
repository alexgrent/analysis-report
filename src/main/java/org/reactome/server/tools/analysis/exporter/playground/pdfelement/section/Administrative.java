package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.util.Arrays;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Administrative implements Section {

    private static final int numOfIdentifiersToShow = 5;
    private static final String[] ADMINISTRATIVE = PdfUtils.getText("src/main/resources/texts/administrative.txt");

    public void render(AnalysisReport report) throws Exception {
        report.addNormalTitle("Administrative", FontSize.H2, 0)
                .addParagraph(new Paragraph().setFontSize(FontSize.H5)
                        .setMarginLeft(MarginLeft.M2)
                        .add(String.format(ADMINISTRATIVE[0], report.getDataSet().getReportArgs().getToken().toLowerCase()))
                        .add(PdfUtils.createUrlLinkIcon(report.getDataSet().getLinkIcon(), FontSize.H5, URL.ANALYSIS + report.getDataSet().getReportArgs().getToken()))
                        .add(String.format(ADMINISTRATIVE[1]
                                , Arrays.toString(report.getDataSet().getIdentifierNotFounds().stream().limit(numOfIdentifiersToShow).toArray()).replaceAll("[\\[\\]]", "")
                                , report.getDataSet().getDBVersion()
                                , PdfUtils.getTimeStamp())));
    }
}