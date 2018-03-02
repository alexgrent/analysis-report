package org.reactome.server.tools.analysis.exporter.util;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.graph.domain.result.DiagramResult;
import org.reactome.server.tools.analysis.exporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterException;
import org.reactome.server.tools.diagram.exporter.common.analysis.AnalysisException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonDeserializationException;
import org.reactome.server.tools.diagram.exporter.common.profiles.factory.DiagramJsonNotFoundException;
import org.reactome.server.tools.diagram.exporter.raster.RasterExporter;
import org.reactome.server.tools.diagram.exporter.raster.api.RasterArgs;
import org.reactome.server.tools.diagram.exporter.raster.ehld.exception.EhldException;

import java.awt.image.BufferedImage;

/**
 * Help to create the diagram image by invoking the Reactome {@link
 * RasterExporter}.
 *
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class DiagramHelper {
	private static RasterExporter exporter;

	/**
	 * create diagram image by using the RasterExporter{@see RasterExporter}.
	 *
	 * @param stId stable identifier of the diagram.
	 * @param asr  {@see AnalysisStoredResult} contains the diagram analysis
	 *             overlay information.
	 *
	 * @return diagram.
	 */
	public static BufferedImage getDiagram(String stId, AnalysisStoredResult asr, String resource) throws AnalysisExporterException {
		final DiagramResult diagramResult = GraphCoreHelper.getDiagramResult(stId);
		RasterArgs args = new RasterArgs(diagramResult.getDiagramStId(), "png");
		args.setSelected(diagramResult.getEvents());
		args.setWriteTitle(false);
		args.setResource(resource);
		try {
			return exporter.export(args, asr);
		} catch (DiagramJsonNotFoundException | AnalysisException | DiagramJsonDeserializationException | EhldException e) {
			System.out.println("!!" + diagramResult.getDiagramStId());
//			e.printStackTrace();
//			throw new AnalysisExporterException("Exception reading diagram", e);
		}
		return null;
	}

	/**
	 * Set the necessary file before use it.
	 *
	 * @param reportArgs args contains the diagram/ehld/analysis path and svg
	 *                   summary file.
	 */
	public static void setPaths(ReportArgs reportArgs) {
		exporter = new RasterExporter(reportArgs.getDiagramPath(), reportArgs.getEhldPath(), reportArgs.getAnalysisPath(), reportArgs.getSvgSummary());
	}
}