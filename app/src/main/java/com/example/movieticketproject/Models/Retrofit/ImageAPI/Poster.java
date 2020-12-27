
package com.example.movieticketproject.Models.Retrofit.ImageAPI;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Poster implements Serializable
{

    private Double aspect_ratio;
    private String file_path;
    private Integer height;
    private String iso6391;
    private Integer voteAverage;
    private Integer voteCount;
    private Integer width;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 1564974352532513107L;

    public Double getAspectRatio() {
        return aspect_ratio;
    }

    public void setAspectRatio(Double aspectRatio) {
        this.aspect_ratio = aspectRatio;
    }

    public String getFilePath() {
        return file_path;
    }

    public void setFilePath(String filePath) {
        this.file_path = filePath;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public Integer getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Integer voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Poster{" +
                "aspectRatio=" + aspect_ratio +
                ", filePath='" + file_path + '\'' +
                ", height=" + height +
                ", iso6391='" + iso6391 + '\'' +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                ", width=" + width +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
