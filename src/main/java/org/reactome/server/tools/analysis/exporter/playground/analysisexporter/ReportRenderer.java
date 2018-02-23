package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.ResourceSummary;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.analysis.core.result.utils.TokenUtils;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRenderReportException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile.PdfProfile;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.section.*;
import org.reactome.server.tools.analysis.exporter.playground.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.FireworksHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
class ReportRenderer {

    private static final Long DEFAULT_SPECIES = 48887L;
    private static final String PROFILE = "profiles/compact.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportRenderer.class);
    private static final PdfProfile profile = loadPdfProfile();

    /**
     * this method is to render the report with data set.
     *
     * @param reportArgs  {@see ReportArgs}
     * @param destination {@see PdfWriter}
     * @throws Exception when fail to create the PDF document.
     */
    protected static void render(ReportArgs reportArgs, OutputStream destination) throws Exception {
        DiagramHelper.setPaths(reportArgs);
        FireworksHelper.setPaths(reportArgs);

        AnalysisStoredResult analysisStoredResult = new TokenUtils(reportArgs.getAnalysisPath()).getFromToken(reportArgs.getToken());

        if (reportArgs.getSpecies() == null) {
            reportArgs.setSpecies(DEFAULT_SPECIES);
            LOGGER.warn("use default species.");
        }

        if (!analysisStoredResult.getResourceSummary().contains(new ResourceSummary(reportArgs.getResource(), null))) {
            String resource = getDefaultResource(analysisStoredResult);
            LOGGER.warn("No such resource:{} in this analysis result,use {} instead.", reportArgs.getResource(), resource);
            reportArgs.setResource(resource);
        }

        SpeciesFilteredResult speciesFilteredResult = analysisStoredResult.filterBySpecies(reportArgs.getSpecies(), reportArgs.getResource());

        checkReportArgs(analysisStoredResult, speciesFilteredResult, reportArgs, profile);
        AnalysisReport report = new AnalysisReport(profile, reportArgs, destination);

//        System.out.println("content:" + report.getCurrentPageArea().getWidth() + "x" + report.getCurrentPageArea().getHeight());
        FontSize.setUp(report.getProfile().getFontSize());
        List<Section> sections = new ArrayList<>(6);
        sections.add(new TitleAndLogo());
        sections.add(new Administrative());
        sections.add(new Introduction());
        sections.add(new Summary());
        sections.add(new Overview());

        try {
            for (Section section : sections) {
                section.render(report, analysisStoredResult, speciesFilteredResult);
            }
        } catch (Exception e) {
            throw new FailToRenderReportException("Fail to render report.", e);
        } finally {
            report.close();
            report.getPdfDocument().close();
        }
    }

    /**
     * load the {@see PdfProfile} config information to control PDF layout.
     */
    private static PdfProfile loadPdfProfile() {
        try {
            InputStream resource = ReportRenderer.class.getResourceAsStream(PROFILE);
            return MAPPER.readValue(resource, PdfProfile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new PdfProfile();
        }
    }

    private static void checkReportArgs(AnalysisStoredResult analysisStoredResult, SpeciesFilteredResult speciesFilteredResult, ReportArgs reportArgs, PdfProfile profile) {
        if (profile.getPathwaysToShow() > speciesFilteredResult.getPathways().size()) {
            profile.setPathwaysToShow(speciesFilteredResult.getPathways().size());
            LOGGER.warn("There just have {} in your analysis result.", speciesFilteredResult.getPathways().size());
        }
        if (reportArgs.getPagination() > speciesFilteredResult.getPathways().size() - 1) {
            reportArgs.setPagination(0);
            LOGGER.warn("Pagination must less than pathwaysToShow.");
        }
        if (reportArgs.getPagination() + profile.getPathwaysToShow() > speciesFilteredResult.getPathways().size()) {
            profile.setPathwaysToShow(speciesFilteredResult.getPathways().size() - reportArgs.getPagination());
        }
    }

    private static String getDefaultResource(AnalysisStoredResult result) {
        final List<ResourceSummary> summary = result.getResourceSummary();
        if (summary.size() == 2)
            return summary.get(1).getResource();
        else return summary.get(0).getResource();
    }
}