package com.example.bonairecontrol.Modelos;

public class ModelListDevices {

    private int image;
    private String title;
    private String statusTitle;
    private String statusData;
    private String lastReportTitle;
    private String lastReportData;
    private String idTitle;
    private String idData;
    private String bottomLeft;
    private String bottomRight;
    private String idParametroUi;

    public ModelListDevices(int image, String title, String statusTitle, String statusData,
                            String lastReportTitle, String lastReportData, String idTitle,
                            String idData, String bottomLeft, String bottomRight, String idParametroUi) {
        this.image = image;
        this.title = title;
        this.statusTitle = statusTitle;
        this.statusData = statusData;
        this.lastReportTitle = lastReportTitle;
        this.lastReportData = lastReportData;
        this.idTitle = idTitle;
        this.idData = idData;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.idParametroUi = idParametroUi;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

    public String getStatusData() {
        return statusData;
    }

    public void setStatusData(String statusData) {
        this.statusData = statusData;
    }

    public String getLastReportTitle() {
        return lastReportTitle;
    }

    public void setLastReportTitle(String lastReportTitle) {
        this.lastReportTitle = lastReportTitle;
    }

    public String getLastReportData() {
        return lastReportData;
    }

    public void setLastReportData(String lastReportData) {
        this.lastReportData = lastReportData;
    }

    public String getIdTitle() {
        return idTitle;
    }

    public void setIdTitle(String idTitle) {
        this.idTitle = idTitle;
    }

    public String getIdData() {
        return idData;
    }

    public void setIdData(String idData) {
        this.idData = idData;
    }

    public String getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(String bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public String getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(String bottomRight) {
        this.bottomRight = bottomRight;
    }

    public String getIdParametroUi() {
        return idParametroUi;
    }

    public void setIdParametroUi(String idParametroUi) {
        this.idParametroUi = idParametroUi;
    }
}
