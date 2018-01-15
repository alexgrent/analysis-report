package org.reactome.server.tools.analysis.exporter.playground.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.NoSuchPageSizeException;
import org.reactome.server.tools.analysis.exporter.playground.model.*;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * scale image's size to fit the analysis report's page size.
     *
     * @param report
     * @param image
     * @return
     */
    public static Image ImageAutoScale(AnalysisReport report, Image image) {
        float pageWidth = report.getPdfDocument().getDefaultPageSize().getWidth() - report.getLeftMargin() - report.getRightMargin();
        float stride = 0.01f;
        float scaling = 0.2f;
        do {
            image.scale(scaling, scaling);
            scaling -= stride;
        }
        while (pageWidth < image.getImageScaledWidth());
        return image;
    }

    public static String getTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }


    public static Paragraph setDestination(Paragraph paragraph, String destination) {
        paragraph.setProperty(Property.DESTINATION, destination);
        return paragraph;
    }

    /**
     * concat all stId into a long String as the http post's parameter entities.
     *
     * @param pathways all pathways need to show.
     * @return
     */
    public static StringBuilder stIdConcat(Pathway[] pathways) {
        StringBuilder stringBuilder = new StringBuilder(2 * pathways.length + 1);
        Stream.of(pathways).forEach(pathway -> stringBuilder.append(pathway.getStId()).append(','));
        return stringBuilder;
    }

    /**
     * merge the identifiers from all different pathways into a unique array.
     *
     * @param identifiersWasFounds
     * @return
     */
    // TODO: 09/01/18 this method need to optimize
    public static Map<String, Identifier> identifiersFilter(IdentifiersWasFound[] identifiersWasFounds) {
        // initial capacity of hash map is about : identifiersWasFounds * 1.33 + 1
        Map<String, Identifier> filteredIdentifiers = new HashMap<>((int) (identifiersWasFounds.length / 0.75) + 1);
        for (IdentifiersWasFound identifiersWasFound : identifiersWasFounds) {
            for (Identifier identifier : identifiersWasFound.getEntities()) {
                if (!filteredIdentifiers.containsKey(identifier.getId())) {
                    filteredIdentifiers.put(identifier.getId(), identifier);
                } else {
                    filteredIdentifiers.get(identifier.getId()).getMapsTo().addAll(identifier.getMapsTo());
                }
            }
        }

        for (Entry<String, Identifier> entry : filteredIdentifiers.entrySet()) {
            for (MapsTo mapsTo : entry.getValue().getMapsTo()) {
                if (!entry.getValue().getResourceMapsToIds().containsKey(mapsTo.getResource())) {
                    entry.getValue().getResourceMapsToIds().put(mapsTo.getResource(), mapsTo.getIds().toString());
                } else {
                    entry.getValue().getResourceMapsToIds().get(mapsTo.getResource()).concat(',' + mapsTo.getIds().toString());
                }
            }
        }
        return filteredIdentifiers;
    }

    // TODO: 06/12/17 this method may be deleted once the correct dataset structure was confirm
    public static DataSet getDataSet(String token, int numOfPathwaysToShow) throws Exception {
        DataSet dataSet = new DataSet();
        ResultAssociatedWithToken resultAssociatedWithToken = HttpClientHelper.getForObject(URL.RESULTASSCIATEDWITHTOKEN, ResultAssociatedWithToken.class, token);
        Identifier[] identifiersWasNotFounds = HttpClientHelper.getForObject(URL.IDENTIFIERSWASNOTFOUND, Identifier[].class, token);
        dataSet.setIdentifiersWasNotFounds(identifiersWasNotFounds);

        StringBuilder stIds = PdfUtils.stIdConcat(resultAssociatedWithToken.getPathways());
        IdentifiersWasFound[] identifiersWasFounds = HttpClientHelper.postForObject(URL.IDENTIFIERSWASFOUND, stIds.deleteCharAt(stIds.length() - 1).toString(), IdentifiersWasFound[].class, token);
        dataSet.setIdentifiersWasFounds(identifiersWasFounds);
        dataSet.setIdentifiersWasFiltered(PdfUtils.identifiersFilter(identifiersWasFounds));

        //reduce the size of pathway array to save memory.
        resultAssociatedWithToken.setPathways(Arrays.copyOf(resultAssociatedWithToken.getPathways(), numOfPathwaysToShow));
        dataSet.setResultAssociatedWithToken(resultAssociatedWithToken);
        dataSet.setNumOfPathwaysToShow(numOfPathwaysToShow);
        dataSet.setVersion(HttpClientHelper.getForObject(URL.VERSION, Integer.class, ""));
        return dataSet;
    }

//    public _DataSet getDataSet(String token) throws Exception {
//        return MAPPER.readValue(CLIENT.execute(GET).getEntity().getContent(),_DataSet.class);
//    }

    public static PdfFont createFont(String fontName) throws IOException {
        return PdfFontFactory.createFont(fontName);
    }

    public static Color createColor(String color) {
        return new DeviceRgb(Integer.valueOf(color.substring(1, 3), 16)
                , Integer.valueOf(color.substring(3, 5), 16)
                , Integer.valueOf(color.substring(5, 7), 16));
    }

    public static <T> T readValue(File src, Class<T> type) throws IOException {
        return MAPPER.readValue(src, type);
    }

    public static <T> T readValue(InputStream src, Class<T> type) throws IOException {
        return MAPPER.readValue(src, type);
    }

    public static PageSize createPageSize(String type) throws NoSuchPageSizeException {
        switch (type) {
            case "A0":
                return PageSize.A0;
            case "A1":
                return PageSize.A1;
            case "A2":
                return PageSize.A2;
            case "A3":
                return PageSize.A3;
            case "A4":
                return PageSize.A4;
            case "A5":
                return PageSize.A5;
            case "A6":
                return PageSize.A6;
            case "A7":
                return PageSize.A7;
            case "A8":
                return PageSize.A8;
            case "A9":
                return PageSize.A9;
            case "A10":
                return PageSize.A10;
            case "B0":
                return PageSize.B0;
            case "B1":
                return PageSize.B1;
            case "B2":
                return PageSize.B2;
            case "B3":
                return PageSize.B3;
            case "B4":
                return PageSize.B4;
            case "B5":
                return PageSize.B5;
            case "B6":
                return PageSize.B6;
            case "B7":
                return PageSize.B7;
            case "B8":
                return PageSize.B8;
            case "B9":
                return PageSize.B9;
            case "B10":
                return PageSize.B10;
            default:
                throw new NoSuchPageSizeException(String.format("Cant recognize page size : %s", type));
        }
    }

//    public _DataSet getDataSet() {
//        // TODO: 21/12/17 connect server by httpurlconnection
//        return new _DataSet();
//    }

}
