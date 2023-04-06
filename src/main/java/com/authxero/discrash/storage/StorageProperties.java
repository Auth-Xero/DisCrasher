package com.authxero.discrash.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    private String location = "user-uploads";
    private String outputDir = "temp-out";
    private String wwmDir = "wwm";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getWwmDir() {
        return wwmDir;
    }

    public void setWwmDirDir(String outputDir) {
        this.outputDir = wwmDir;
    }
}
