package com.example.bonairecontrol.Modelos;

public class ModelSettings {

    private int image;
    private String title;
    private String description;
    private String type;
    private String message;


    public ModelSettings(int image, String title, String description, String type, String message) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.type = type;
        this.message = message;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
