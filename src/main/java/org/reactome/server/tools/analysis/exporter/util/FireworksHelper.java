package org.reactome.server.tools.analysis.exporter.util;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.ReportArgs;
import org.reactome.server.tools.fireworks.exporter.FireworksExporter;
import org.reactome.server.tools.fireworks.exporter.common.api.FireworkArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * Help to create the fireworks image by invoking the Reactome {@link
 * FireworksExporter}.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class FireworksHelper {

	private static final String SPECIES = "Homo_sapiens";
	private static final double QUALITY = 2.5;
	private static final Logger LOGGER = LoggerFactory.getLogger(FireworksHelper.class);
	private static FireworksExporter exporter;

	/**
	 * get the fireworks image from {@link FireworksExporter} by given analysis
	 * token.
	 *
	 * @param analysisStoredResult {@link AnalysisStoredResult}.
	 *
	 * @return fireworks.
	 *
	 * @see FireworksExporter
	 */
	public static BufferedImage getFireworks(AnalysisStoredResult analysisStoredResult) {

		FireworkArgs args = new FireworkArgs(SPECIES, "png");
		args.setFactor(QUALITY);
		args.setWriteTitle(false);
		args.setProfile(FireworksColor.COPPER_PLUS.getColor());

		try {
			return exporter.renderRaster(args, analysisStoredResult);
		} catch (Exception pascual) {
			LOGGER.error("Failed to create fireworks for token: {}", analysisStoredResult.getSummary().getToken());
			return null;
		}
	}

	/**
	 * Set the necessary file before use it.
	 *
	 * @param reportArgs args contains the fireworks json file and analysis
	 *                   binary file path.
	 */
	public static void setPaths(ReportArgs reportArgs) {
		exporter = new FireworksExporter(reportArgs.getFireworksPath(), reportArgs.getAnalysisPath());
	}
}