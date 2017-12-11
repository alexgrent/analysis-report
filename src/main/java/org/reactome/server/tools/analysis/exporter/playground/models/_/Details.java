package org.reactome.server.tools.analysis.exporter.playground.models._;

import java.util.Arrays;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Details {
    private String displayName;
    private String stId;
    private String created;
    private String modified;
    private String authored;
    private String edited;
    private String releaseDate;
    private String speciesName;
    private String compartment;
    private boolean hasDiagram;
    // TODO: 06/12/17 need to add the parents' stId to get the diagram : private String parentStId;
    private String summation;
    private boolean isInDisease;
    private String disease;
    private boolean isInferred;
    private InferredFrom[] inferredFrom;
    private LiteratureReference[] literatureReference;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getAuthored() {
        return authored;
    }

    public void setAuthored(String authored) {
        this.authored = authored;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getCompartment() {
        return compartment;
    }

    public void setCompartment(String compartment) {
        this.compartment = compartment;
    }

    public boolean isHasDiagram() {
        return hasDiagram;
    }

    public void setHasDiagram(boolean hasDiagram) {
        this.hasDiagram = hasDiagram;
    }

    public String getSummation() {
        return summation;
    }

    public void setSummation(String summation) {
        this.summation = summation;
    }

    public boolean isInDisease() {
        return isInDisease;
    }

    public void setIsInDisease(boolean inDisease) {
        isInDisease = inDisease;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public boolean isInferred() {
        return isInferred;
    }

    public void setIsInferred(boolean inferred) {
        isInferred = inferred;
    }

    public InferredFrom[] getInferredFrom() {
        return inferredFrom;
    }

    public void setInferredFrom(InferredFrom[] inferredFrom) {
        this.inferredFrom = inferredFrom;
    }

    public LiteratureReference[] getLiteratureReference() {
        return literatureReference;
    }

    public void setLiteratureReference(LiteratureReference[] literatureReference) {
        this.literatureReference = literatureReference;
    }

    @Override
    public String toString() {
        return "Details{" +
                "displayName='" + displayName + '\'' +
                ", stId='" + stId + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", authored='" + authored + '\'' +
                ", edited='" + edited + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", speciesName='" + speciesName + '\'' +
                ", compartment='" + compartment + '\'' +
                ", hasDiagram=" + hasDiagram +
                ", summation='" + summation + '\'' +
                ", isInDisease=" + isInDisease +
                ", disease='" + disease + '\'' +
                ", isInferred=" + isInferred +
                ", inferredFrom=" + Arrays.toString(inferredFrom) +
                ", literatureReference=" + Arrays.toString(literatureReference) +
                '}';
    }
}