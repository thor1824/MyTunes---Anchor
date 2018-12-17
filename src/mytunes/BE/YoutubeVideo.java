/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class YoutubeVideo
{

    private String title;
    private String artist;
    private StringProperty titleProperty;
    private StringProperty artistProperty;
    private int positionID = 0;
    private final String youtubeID;
    private final int id;
    private final String duration;

    public YoutubeVideo(String title, String artist, String youtubeID, int id, String duration)
    {
        this.title = title;
        this.titleProperty = new SimpleStringProperty();
        this.titleProperty.setValue(title);
        this.artist = artist;
        this.artistProperty = new SimpleStringProperty();
        this.artistProperty.setValue(artist);
        this.youtubeID = youtubeID;
        this.id = id;
        this.duration = duration;
    }
    
    /**
     * Get the value of PositionID
     * 
     * @return 
     */
    public int getPositionID()
    {
        return positionID;
    }
    
    /**
     * Sets the value of PositionID
     * 
     * @param positionID 
     */
    public void setPositionID(int positionID)
    {
        this.positionID = positionID;
    }
    
    /**
     * Get the value of Title as a StringPorperty
     * 
     * @return 
     */
    public StringProperty getTitleProperty()
    {
        return titleProperty;
    }
    
    /**
     * Get the value of Artist as a StringPorperty
     * 
     * @return 
     */
    public StringProperty getArtistProperty()
    {
        return artistProperty;
    }
    
    /**
     * Get the value of Title
     * 
     * @return 
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Set the value of Title
     * 
     * @param title 
     */
    public void setTitle(String title)
    {
        this.title = title;
        titleProperty.setValue(title);
    }
    
    /**
     * Get the value of Artist
     * 
     * @return 
     */
    public String getArtist()
    {
        return artist;
    }
    
    /**
     * Set the value of artist
     * 
     * @param artist 
     */
    public void setArtist(String artist)
    {
        this.artist = artist;
        this.artistProperty.setValue(artist);
    }
    
    /**
     * Get the value of YoutubeID
     * 
     * @return 
     */
    public String getYoutubeID()
    {
        return youtubeID;
    }
    
    /**
     * Get the value of ID
     * 
     * @return 
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Get the value of duration
     * 
     * @return 
     */
    public String getDuration()
    {
        return duration;
    }
    
    /**
     * Get the Duration in the format Hours:minutes:seconds (not Supotted yet)
     * 
     * @return 
     */
    public String getFormattetDuration()
    {
        return "";
    }

}
