/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.SimpleStyleableStringProperty;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class YoutubeVideo {
    private String title;
    private String artist;
    private StringProperty titleProperty;
    private StringProperty artistProperty;
    private int positionID = 0;
    private final String youtubeID;
    private final int id;
    private final String duration;
    

    public YoutubeVideo(String title, String artist, String youtubeID, int id, String duration) {
        this.title = title;
        this.titleProperty =  new SimpleStringProperty();
        this.titleProperty.setValue(title);
        this.artist = artist;
        this.artistProperty = new SimpleStringProperty();
        this.artistProperty.setValue(artist);
        this.youtubeID = youtubeID;
        this.id = id;
        this.duration = duration;
    }

    public int getPositionID()
    {
        return positionID;
    }

    public void setPositionID(int positionID)
    {
        this.positionID = positionID;
    }
    
    
    
    public StringProperty getTitleProperty() {
        return titleProperty;
    }

    public StringProperty getArtistProperty() {
        return artistProperty;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titleProperty.setValue(title);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
        this.artistProperty.setValue(artist);
    }

    public String getYoutubeID() {
        return youtubeID;
    }

    public int getId() {
        return id;
    }

    public String getDuration() {
        return duration;
    }

    public String getFormattetDuration() {
        return "";
    }
    
    
    
}
