package de.uniba.dsg.cna.qmsurvey.application;

public class JsonRepresentation {

    private final String fileName;

    private final byte[] jsonContent;

    public JsonRepresentation(String fileName, byte[] jsonContent) {
        this.fileName = fileName;
        this.jsonContent = jsonContent;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getJsonContent() {
        return jsonContent;
    }
}
