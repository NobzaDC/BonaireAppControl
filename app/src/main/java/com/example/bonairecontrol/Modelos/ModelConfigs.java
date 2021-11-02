package com.example.bonairecontrol.Modelos;

import java.io.Serializable;

public class ModelConfigs implements Serializable {

    private int id;
    private int img_type_configs;
    private String str_title_configs;
    private String str_description_configs;

    public ModelConfigs() {
    }

    public ModelConfigs(int id, int img_type_configs, String str_title_configs, String str_description_configs) {
        this.id = id;
        this.img_type_configs = img_type_configs;
        this.str_title_configs = str_title_configs;
        this.str_description_configs = str_description_configs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImg_type_configs() {
        return img_type_configs;
    }

    public void setImg_type_configs(int img_type_configs) {
        this.img_type_configs = img_type_configs;
    }

    public String getStr_title_configs() {
        return str_title_configs;
    }

    public void setStr_title_configs(String str_title_configs) {
        this.str_title_configs = str_title_configs;
    }

    public String getStr_description_configs() {
        return str_description_configs;
    }

    public void setStr_description_configs(String str_description_configs) {
        this.str_description_configs = str_description_configs;
    }
}
